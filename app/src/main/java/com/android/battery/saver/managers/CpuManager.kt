package com.android.battery.saver.managers

import android.content.Context
import android.content.ContextWrapper
import android.os.Environment
import android.util.Log
import com.android.battery.saver.SearchAlgorithms
import com.android.battery.saver.activities.MainActivity
import com.android.battery.saver.dao.TestInfoDAOImpl
import com.android.battery.saver.dao.UserComplainDAOImpl
import com.android.battery.saver.event.OnCpuResetThreshold
import com.android.battery.saver.event.OnCpuScaleAllDownEvent
import com.android.battery.saver.event.OnCpuScaleAllUpEvent
import com.android.battery.saver.event.OnSetAllCoreToMaxEvent
import com.android.battery.saver.helper.ReadWriteFile
import com.android.battery.saver.logger.Logger
import com.android.battery.saver.model.CpuCoreModel
import com.android.battery.saver.model.TestsInfoModel
import com.android.battery.saver.model.UserComplainModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.ceil
import kotlin.random.Random


object CpuManager {
    private const val pathToCpu: String = "/sys/devices/system/cpu/cpu"
    private const val pathToCpuBoost: String = "/sys/module/cpu_boost/parameters/"
    private val numberOfCores: Int = Runtime.getRuntime().availableProcessors()
    private val cpuCores = Collections.synchronizedMap(HashMap<Int, CpuCoreModel>())
    private var totalOfFrequencies: Int = 0

    init {
        initializeHash()
        EventBus.getDefault().register(this)
    }


    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onCpuEvent(onCpuResetThreshold: OnCpuResetThreshold) {
        println("resetting threshold")
        for (i in 0 until numberOfCores) {
            cpuCores[i]!!.threshold = 0
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onCpuEvent(onSetAllCoreToMaxEvent: OnSetAllCoreToMaxEvent) {
        setAllCoresToMax()
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onCpuEvent(onCpuScaleAllDownEvent: OnCpuScaleAllDownEvent) {
        if (scaleAllCpuDown(onCpuScaleAllDownEvent.decreaseCpuFrequency)) {
            println("decreasing cpu")
            val testInfoModel = TestsInfoModel(onCpuScaleAllDownEvent.currentApp, getAllCoresFrequencies(),
                    getAllCoresThreshold(), onCpuScaleAllDownEvent.readInterval, onCpuScaleAllDownEvent.iteration,
                    onCpuScaleAllDownEvent.decreaseCpuInterval, onCpuScaleAllDownEvent.decreaseCpuFrequency,
                    onCpuScaleAllDownEvent.increaseCpuFrequency, onCpuScaleAllDownEvent.UImpatienceLevel)
            val testInfoDAOImpl = TestInfoDAOImpl(MainActivity.appContext)
            testInfoDAOImpl.insert(testInfoModel)
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onCpuEvent(onCpuScaleAllUpEvent: OnCpuScaleAllUpEvent) {

        setThreshold()



        scaleAllCpuUp(onCpuScaleAllUpEvent.increase)
        val userComplainModel = UserComplainModel(onCpuScaleAllUpEvent.currentApp, getAllCoresFrequencies(),
                getAllCoresThreshold(), onCpuScaleAllUpEvent.readInterval, onCpuScaleAllUpEvent.iteration,
                onCpuScaleAllUpEvent.decreaseCpuInterval, onCpuScaleAllUpEvent.decreaseCpuFrequency,
                onCpuScaleAllUpEvent.increaseCpuFrequency, onCpuScaleAllUpEvent.UImpatienceLevel)
        UserComplainDAOImpl(MainActivity.appContext).insert(userComplainModel)

    }

    @Synchronized
    private fun initializeHash() {
        stopMpDecision()
        for (i in 0 until numberOfCores) {
            //Check if core is on/off
            if (!checkIfCoreIsOnFromInternalFile(i)) {
                //Turn core to get its data
                turnCoreOn(i)
            }
            //Retrieve possible frequencies
            val frequencies = getCoreAvailableFrequencies(i)
            //Retrieve current frequency
            val curFreq = getCurrentFrequencyFromInternalFile(i)
            val governor = getCoreGovernorFromInternalFile(i)
            cpuCores[i] = CpuCoreModel(i, frequencies, curFreq, governor,
                    SearchAlgorithms.binarySearch(curFreq, frequencies),
                    true, 0)
            totalOfFrequencies += frequencies.size
        }
        startMpDecision()
    }

    @Synchronized
    private fun setInputBooster(governor: String) {
        var frequency = 0
        if (governor != "userspace") {
            frequency = cpuCores[0]!!.frequencies[(cpuCores[0]!!.frequencies.size / 2)]
        }
        val path = "echo $frequency > $pathToCpuBoost/input_boost_freq"
        try {
            Log.d(Logger.DEBUG, "Writing $frequency to input_boos_freq")
            val proc = Runtime.getRuntime().exec(arrayOf("su", "-c", path))
            proc.waitFor()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * This method is called when the service is being stopped and we are giving Android
     * the full control over the device again
     */
    @Synchronized
    private fun returnDeviceControlToAndroid() {
        val defaultGovernor = getDefaultGovernor()
        for (i in 0 until numberOfCores) {
            //if core is off
            if (!cpuCores[i]?.status!!) {
                turnCoreOn(i)
            }
            //Write the default governor to it
            writeGovernorToCore(i, defaultGovernor)
        }
        setInputBooster(defaultGovernor)
        startMpDecision()
    }

    @Synchronized
    private fun getCoreGovernorFromInternalFile(core: Int): String {
        return ReadWriteFile.returnStringFromProcess(
                Runtime.getRuntime().exec(
                        arrayOf(
                                "su",
                                "-c",
                                "cat /sys/devices/system/cpu/cpu$core/cpufreq/scaling_governor"
                        )
                )
        ).replace("\n".toRegex(), "")
    }

    @Synchronized
    private fun initializeUImpatience() {
        stopMpDecision()
        setInputBooster("userspace")
        for (i in 0 until numberOfCores) {
            //Check if core is on/off
            if (!checkIfCoreIsOnFromInternalFile(i)) {
                //Turn core to get its data
                turnCoreOn(i)
            }
            //Write userspace to core
            writeGovernorToCore(i, "userspace")
            //Write max freq possible
            writeMaxFreqToCore(i, cpuCores[0]!!.frequencies[cpuCores[0]!!.frequencies.size - 1])
            //Write min freq possible
            writeMinFreqToCore(i, cpuCores[0]!!.frequencies[0])
            //Retrieve current frequency
        }
    }

    /**
     * Set the governor to what the user selected
     */
    @Synchronized
    fun setGovernorFromSpinner(governor: String) {
        stopMpDecision()
        setInputBooster(governor)
        if (governor != "userspace") {
            for (i in 0 until numberOfCores) {
                if (!checkIfCoreIsOnFromInternalFile(i))
                    turnCoreOn(i)
                writeGovernorToCore(i, governor)
                //Write max freq possible
                writeMaxFreqToCore(i, cpuCores[0]!!.frequencies[cpuCores[0]!!.frequencies.size - 1])
                //Write min freq possible
                writeMinFreqToCore(i, cpuCores[0]!!.frequencies[0])
            }
            startMpDecision()

        } else {
            initializeUImpatience()
        }
    }

    fun scaleCpuToApp(cpuFrequencies: ArrayList<Int>, cpuThreshold: ArrayList<Int>) {
        for (i in 0 until numberOfCores) {
            if (cpuFrequencies[i] == 0) {
                turnCoreOff(i)
            } else {
                if (!cpuCores[i]!!.status)
                    turnCoreOn(i)
                setCoreFrequency(i, cpuFrequencies[i])
            }
            cpuCores[i]!!.threshold = cpuThreshold[i]
        }
    }

    @Synchronized
    fun getFrequencyFromCore(core: Int): Int {
        return cpuCores[core]!!.currentFrequency
    }

    @Synchronized
    fun getFreqPosFromCore(core: Int): Int {
        return cpuCores[core]!!.freqPos
    }

    @Synchronized
    fun getAllCoresFrequencies(): ArrayList<Int> {
        val arr = ArrayList<Int>()
        for (i in 0 until numberOfCores) {
            arr.add(cpuCores[i]!!.currentFrequency)
        }
        return arr
    }

    @Synchronized
    fun getAllCoresThreshold(): ArrayList<Int> {
        val arr = ArrayList<Int>()
        for (i in 0 until numberOfCores) {
            arr.add(cpuCores[i]!!.threshold)
        }
        return arr
    }

    @Synchronized
    private fun setAllCoresToMax() {
        for (i in 0 until numberOfCores) {
            if (!cpuCores[i]!!.status) {
                turnCoreOn(i)
            }
            setCoreFrequency(i, cpuCores[i]!!.frequencies[cpuCores[i]!!.frequencies.size - 1])
        }
    }

    fun stop() {
        returnDeviceControlToAndroid()
    }

    private fun writeMaxFreqToCore(core: Int, freq: Int) {
        val path = "echo $freq > $pathToCpu$core/cpufreq/scaling_max_freq"
        try {
            Log.d(Logger.DEBUG, "Writing max freq to core $core")
            val proc = Runtime.getRuntime().exec(arrayOf("su", "-c", path))
            proc.waitFor()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun writeMinFreqToCore(core: Int, freq: Int) {
        val path = "echo $freq > $pathToCpu$core/cpufreq/scaling_min_freq"
        try {
            Log.d(Logger.DEBUG, "Writing min freq to core $core")
            val proc = Runtime.getRuntime().exec(arrayOf("su", "-c", path))
            proc.waitFor()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val proc = Runtime.getRuntime().exec(
                arrayOf(
                        "su", "-c",
                        "cat /sys/devices/system/cpu/cpu$core/cpufreq/scaling_min_freq"
                )
        )

        if (ReadWriteFile.returnStringFromProcess(proc)
                        .replace("\n".toRegex(), "")
                        .toInt() != freq) {
            throw Exception("Min internal file != from uimpatience")
        }
    }

    /**
     * Check for threshold if any. Do not need to check all cores because they WILL be set at same time
     * return True if has threshold, False if no threshold set
     */
    @Synchronized
    private fun configHasThreshold(): Boolean {
        return cpuCores[0]!!.threshold != 0
    }

    /**
     * Set the threshold to be the speed of each core when the user clicked on the notification
     */
    @Synchronized
    private fun setThreshold() {
        for (i in 0 until numberOfCores) {
            cpuCores[i]!!.threshold = cpuCores[i]!!.currentFrequency
        }
    }

    @Synchronized
    private fun getAcumulativeSpeedFromCores(): Int {
        var sum = 0
        for (i in 0 until numberOfCores) {
            if (cpuCores[i]!!.status)
                sum += cpuCores[i]!!.freqPos + 1
        }
        return sum
    }

    /**
     * This ramp up the cpu as the following expression: (maxSystemFreqs - actualSystemFreqs)/2
     */
    fun rampCpuUp(increase: Double) {
        val totalAcumulative = getAcumulativeSpeedFromCores()
        var totalToIncrease = ceil((totalOfFrequencies - totalAcumulative).toDouble() * increase)
        var i = 0
        while (totalToIncrease > 0.0) {
            if (!cpuCores[i]!!.status)
                turnCoreOn(i)
            //Core already at max speed, nothing to do
            if (cpuCores[i]!!.freqPos == cpuCores[i]!!.frequencies.size - 1) {
                i++
                continue
            }
            val toMax = cpuCores[i]!!.frequencies.size - 1 - cpuCores[i]!!.freqPos
            if (totalToIncrease - toMax > 0) {
                setCoreFrequency(i, cpuCores[i]!!.frequencies[cpuCores[i]!!.freqPos + toMax])
            } else {
                setCoreFrequency(i, cpuCores[i]!!.frequencies[cpuCores[i]!!.freqPos + totalToIncrease.toInt()])
            }
            totalToIncrease -= toMax
            i++
        }
    }

    @Synchronized
    private fun scaleAllCpuDown(amountOfFrequencyToReduce: Int): Boolean {
        // In this case, ALL cores will have the same speed, then get the freqPos of any core
        // Will lead to the same freqPos for ALL cores
        val freqPos = cpuCores[0]!!.freqPos
        if (!configHasThreshold() /*|| (0 until 10).random() < 1*/) {
            if (freqPos - amountOfFrequencyToReduce >= 0) {
                for (i in 0 until numberOfCores) {
                    setCoreFrequency(i, cpuCores[0]!!.frequencies[freqPos - amountOfFrequencyToReduce])
                }
            } else {
                //set all to minimum
                for (i in 0 until numberOfCores) {
                    setCoreFrequency(i, cpuCores[0]!!.frequencies[0])
                }
            }
            return true
        }

        return false
    }

    @Synchronized
    private fun scaleAllCpuUp(increase: Int): Boolean {
        val freqPos = cpuCores[0]!!.freqPos
	
        if (freqPos + increase < cpuCores[0]!!.frequencies.size) {
            for (i in 0 until numberOfCores) {
                setCoreFrequency(i, cpuCores[0]!!.frequencies[freqPos + increase])
            }
            return true
        } else {
            //set to maximum
            setAllCoresToMax()
        }
        return false
    }

    /**
     * Method that scale down the CPU
     * This will decrease sequentially. It reduces all the frequency of a core before moving to another one
     */
    fun scaleCpuDown(amountOfFrequencyToReduce: Int) {
        //Todo: check if cpu can decrease before reaching threshold
        val r = Random.nextInt(0, 11)
        if (!configHasThreshold() /*|| r < 9*/) {
            var reduceFromNext = false
            var differBetweenCores = 0
            var i = numberOfCores - 1
            while (i > 0 && !reduceFromNext) {
                //Check if cpu is on
                if (cpuCores[i]!!.status) {
                    //Needs to check where in the array of frequency the core is set to
                    val freqPos = cpuCores[i]!!.freqPos
                    //If the frequency to be reduced reaches the minimum of the core and there still
                    //value to decrease
                    if (freqPos - amountOfFrequencyToReduce < 0) {
                        turnCoreOff(i)
                        reduceFromNext = true
                        differBetweenCores = amountOfFrequencyToReduce - freqPos
                    } else {
                        setCoreFrequency(i, cpuCores[i]!!.frequencies[freqPos - amountOfFrequencyToReduce])
                        break
                    }
                }
                i--
            }
            if (i == 0 && !reduceFromNext) {
                val freqPos = cpuCores[i]!!.freqPos
                if (freqPos - amountOfFrequencyToReduce < 0)
                    setCoreFrequency(i, cpuCores[i]!!.frequencies[0])
                else
                    setCoreFrequency(i, cpuCores[i]!!.frequencies[freqPos - amountOfFrequencyToReduce])
            }
            if (reduceFromNext) {
                //frequencies.size = 0 to N (included). We use -1 to get the last position of the vector
                setCoreFrequency(i, cpuCores[i]!!.frequencies[cpuCores[i]!!.frequencies.size - (differBetweenCores + 1)])
            }
        }

    }


    /**
     * This method should NOT be used frequently due to the fact it's an IO operation
     * You should use this ONLY when the system is initializing
     * Otherwise you should use checkCoreStatus
     */
    private fun checkIfCoreIsOnFromInternalFile(core: Int): Boolean {
        val status = ReadWriteFile.returnStringFromProcess(
                Runtime.getRuntime().exec(
                        arrayOf(
                                "su",
                                "-c",
                                "cat /sys/devices/system/cpu/cpu$core/online"
                        )
                )
        ).replace("\n".toRegex(), "")
        //Core is on? Then status == 1
        return status == "1"
    }

    private fun getCurrentFrequencyFromInternalFile(core: Int): Int {
        val proc = Runtime.getRuntime().exec(
                arrayOf(
                        "su", "-c",
                        "cat /sys/devices/system/cpu/cpu$core/cpufreq/scaling_cur_freq"
                )
        )

        return ReadWriteFile.returnStringFromProcess(proc)
                .replace("\n".toRegex(), "")
                .toInt()
    }

    /**
     * Return a possible default governor. Possible here means:
     * if interactive governor is present, then return it
     * else
     * return ondemand (exists in all phones)
     */
    private fun getDefaultGovernor(): String {
        val governors = ReadWriteFile.returnStringFromProcess(
                Runtime.getRuntime().exec(
                        arrayOf(
                                "su",
                                "-c",
                                "cat /sys/devices/system/cpu/cpu0/cpufreq/scaling_available_governors"
                        )
                )
        )
        return if (governors.contains("interactive")) {
            "interactive"
        } else {
            "ondemand"
        }
    }

    /**
     * Self-explanotory method. This will turn off the Qualcomm mpdecision
     * Mpdecision: https://elementalx.org/the-truth-about-kernels-and-battery-life/
     */
    private fun stopMpDecision() {
        val stopMpDecision = "stop mpdecision"
        try {
            Log.d(Logger.DEBUG, "Trying to stop mpdecision")
            val proc = Runtime.getRuntime().exec(arrayOf("su", "-c", stopMpDecision))
            proc.waitFor()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        Log.d(Logger.DEBUG, "mpdecision stopped")
    }

    /**
     * Self-explanotory method. This will turn on the Qualcomm mpdecision
     * Mpdecision: https://elementalx.org/the-truth-about-kernels-and-battery-life/
     */
    private fun startMpDecision() {
        val startMpDecision = "start mpdecision"
        try {
            Log.d(Logger.DEBUG, "Trying to start mpdecision")
            val proc = Runtime.getRuntime().exec(arrayOf("su", "-c", startMpDecision))
            proc.waitFor()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        Log.d(Logger.DEBUG, "mpdecision started")
    }

    /**
     * Returns how many CPU cores are available in the device
     */
    fun getNumberOfCores(): Int {
        return numberOfCores
    }

    /**
     * Self-explanotory method. It will write in the cpu scaling_governor "userspace"
     * so UImpatience can take control of the specific core.
     *
     * @param core
     */
    @Synchronized
    private fun writeGovernorToCore(core: Int, governor: String) {
        try {
            val path = "echo $governor > $pathToCpu$core/cpufreq/scaling_governor"
            val proc = Runtime.getRuntime().exec(arrayOf("su", "-c", path))
            proc.waitFor()
            cpuCores[core]?.governor = governor
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    /**
     * execute a CAT command to scaling_available_frequencies and the frequencies of the core
     * @param the core number
     * @return arrayList containing the frequencies of the core
     */
    private fun getCoreAvailableFrequencies(core: Int): ArrayList<Int> {
        val frequenciesArray = ArrayList<Int>()
        val proc = Runtime.getRuntime().exec(
                arrayOf(
                        "su", "-c",
                        "cat /sys/devices/system/cpu/cpu$core/cpufreq/scaling_available_frequencies"
                )
        )
        ReadWriteFile.returnStringFromProcess(proc)
                .split("[ \t]".toRegex())
                .forEach {
                    if (it != "\n") {
                        frequenciesArray.add(it.toInt())
                    }
                }
        return frequenciesArray
    }

    @Synchronized
    private fun turnCoreOn(core: Int) {
        val path = String.format("echo 1 > $pathToCpu%d/online", core)
        val proc = Runtime.getRuntime().exec(arrayOf("su", "-c", path))
        proc.waitFor()
        if (cpuCores[core] != null) {
            cpuCores[core]?.status = true
            //TODO: what value to set when turning core on?
        }
    }

    @Synchronized
    private fun turnCoreOff(core: Int) {
        val path = String.format("echo 0 > $pathToCpu%d/online", core)
        val proc = Runtime.getRuntime().exec(arrayOf("su", "-c", path))
        proc.waitFor()
        if (cpuCores[core] != null) {
            cpuCores[core]?.status = false
            cpuCores[core]?.currentFrequency = 0
            cpuCores[core]?.freqPos = -1
        }
    }

    /**
     * Set a given frequency to the specific core.
     * @param core
     * @param speed
     */
    @Synchronized
    private fun setCoreFrequency(core: Int, speed: Int) {
        try {
            val path = String.format("echo %d > $pathToCpu$core/cpufreq/scaling_setspeed", speed)
            val proc = Runtime.getRuntime().exec(arrayOf("su", "-c", path))
            proc.waitFor()
            //!IMPORTANT
            //Update the current core frequency
            cpuCores[core]?.currentFrequency = speed
            cpuCores[core]?.freqPos = SearchAlgorithms.binarySearch(speed, cpuCores[core]!!.frequencies)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun setToMinSpeed() {
        for (i in 0 until numberOfCores) {
            if (i < numberOfCores / 2) {
                turnCoreOn(i)
                setCoreFrequency(i, cpuCores[i]!!.frequencies[0])
            } else {
                turnCoreOff(i)
            }
        }
    }

    /**
     * Method will setup cores for new configuration in @param arrayConfiguration
     *
     * @param arrayConfiguration
     */
    fun adjustConfiguration(arrayConfiguration: ArrayList<String>) {
        var x: Int

        //This means that default setup should be loaded
        //All cores but 0 are turned off
        //Core 0 frequency is set to be its middle frequency of all possible frequencies
        //Set all cores to the highest speed.
        if (arrayConfiguration.size == 0) {
            setCoreFrequency(0, cpuCores[0]!!.frequencies[cpuCores[0]!!.frequencies.size / 2])
            for (i in 1 until numberOfCores) {
                turnCoreOff(i)
            }

        } else {
            //i starts at 2 because index 0 represents the name of the running app
            //and index 1 represents the brightness level
            var i: Int = 2
            x = 0
            while (i < numberOfCores + 2) {
                //Write on core X the frequency represented by index i in arrayConfiguration
                setCoreFrequency(x, arrayConfiguration[i].toInt())
                i++
                x++
            }
        }
    }
}
