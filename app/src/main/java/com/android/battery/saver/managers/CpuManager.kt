package com.android.battery.saver.managers

import android.util.Log
import com.android.battery.saver.SearchAlgorithms
import com.android.battery.saver.helper.ReadWriteFile
import com.android.battery.saver.logger.Logger
import com.android.battery.saver.model.CpuCoreModel
import java.io.IOException
import kotlin.random.Random

object CpuManager {
    private const val pathToCpu: String = "/sys/devices/system/cpu/cpu"
    private val numberOfCores: Int = Runtime.getRuntime().availableProcessors()
    private val cpuCores = HashMap<Int, CpuCoreModel>()
    private var totalOfFrequencies: Int = 0
    private const val amountOfFrequencyToReduce = 2

    init {
        start()
    }

    fun scaleCpuToApp(cpuFrequencies: ArrayList<Int>, cpuThreshold: ArrayList<Int>) {
        for (i in 0 until numberOfCores) {
            if (cpuFrequencies[i] == 0) {
                turnCoreOff(i)
            } else {
                if(!cpuCores[i]!!.status)
                    turnCoreOn(i)
                setCoreFrequency(i, cpuFrequencies[i])
            }
            cpuCores[i]!!.threshold = cpuThreshold[i]
        }
    }

    fun getFrequencyFromCore(core: Int): Int {
        return cpuCores[core]!!.currentFrequency
    }

    fun getThresholdFromCore(core: Int): Int {
        return cpuCores[core]!!.threshold
    }

    fun getAllCoresFrequencies(): ArrayList<Int> {
        val arr = ArrayList<Int>()
        for (i in 0 until numberOfCores) {
            arr.add(cpuCores[i]!!.currentFrequency)
        }
        return arr
    }

    fun getAllCoresThreshold(): ArrayList<Int> {
        val arr = ArrayList<Int>()
        for (i in 0 until numberOfCores) {
            arr.add(cpuCores[i]!!.threshold)
        }
        return arr
    }

    fun setAllCoresToMax() {
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

    fun start() {
        //stop mpdecision
        stopMpDecision()
        for (i in 0 until numberOfCores) {
            //Check if core is on/off
            if (!checkIfCoreIsOnFromInternalFile(i)) {
                //Turn core to get its data
                turnCoreOn(i)
            }
            //Retrieve possible frequencies
            val frequencies = getCoreAvailableFrequencies(i)
            //Write userspace to core
            writeGovernorToCore(i, "userspace")
            //Retrieve current frequency
            val curFreq = getCurrentFrequencyFromInteralFile(i)
            cpuCores[i] = CpuCoreModel(i, frequencies, curFreq, "userspace",
                    SearchAlgorithms.binarySearch(curFreq, frequencies),
                    true, 0)
            totalOfFrequencies += frequencies.size
        }
    }

    /**
     * Check for threshold if any. Do not need to check all cores because they WILL be set at same time
     */
    private fun configHasThreshold(): Boolean {
        return cpuCores[0]!!.threshold != 0
    }

    /**
     * Set the threshold to be the speed of each core when the user clicked on the notification
     */
    fun setThreshold() {
        for (i in 0 until numberOfCores) {
            cpuCores[i]!!.threshold = cpuCores[i]!!.currentFrequency
        }
    }

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
    fun rampCpuUp() {
        val totalAcumulative = getAcumulativeSpeedFromCores()
        var totalToIncrease = Math.ceil((totalOfFrequencies - totalAcumulative).toDouble() / 2)
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

    /**
     * Method that scale down the CPU
     * This will decrease sequentially. It reduces all the frequency of a core before moving to another one
     */
    fun scaleCpuDown() {
        //Todo: check if cpu can decrease before reaching threshold
        val r = Random.nextInt(0, 11)
        if (!configHasThreshold() || r < 9) {
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
     * This method is called when the service is being stopped and we are giving Android
     * the full control over the device again
     */
    private fun returnDeviceControlToAndroid() {
        val defaultGovernor = getDefaultGovernor()
        for (i in 0 until numberOfCores) {
            //if core is off
            if (!cpuCores[i]?.status!!) {
                turnCoreOn(i)
            }
            //Write the default governor to it
            writeGovernorToCore(i, defaultGovernor)
            //Turn off core
            //Do I really need to do this?
//            turnCoreOff(i)
        }
        startMpDecision()
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
        return status.toBoolean()
    }

    private fun getCurrentFrequencyFromInteralFile(core: Int): Int {
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
            Log.d(Logger.DEBUG, "Default could be interactive")
            "interactive"
        } else {
            Log.d(Logger.DEBUG, "Default could be ondemand")
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

    private fun turnCoreOn(core: Int) {
        val path = String.format("echo 1 > $pathToCpu%d/online", core)
        val proc = Runtime.getRuntime().exec(arrayOf("su", "-c", path))
        proc.waitFor()
        if (cpuCores[core] != null) {
            cpuCores[core]?.status = true
            //TODO: what value to set when turning core on?
        }
    }

    private fun turnCoreOff(core: Int) {
        val path = String.format("echo 0 > $pathToCpu%d/online", core)
        val proc = Runtime.getRuntime().exec(arrayOf("su", "-c", path))
        proc.waitFor()
        if (cpuCores[core] != null) {
            cpuCores[core]?.status = false
            cpuCores[core]?.currentFrequency = 0
            cpuCores[core]?.freqPos = 0
        }
    }

    /**
     * Set a given frequency to the specific core.
     * @param core
     * @param speed
     */
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

    private fun getCoreFrequency(core: Int): Int {
        return cpuCores[core]?.currentFrequency!!
    }

    //TODO: find better name for this
    /**
     * This sums up the current frequency position of the available frequencies of each core
     * and return as a sum for all cores. Example:
     * There are 10 values for each core and 4 cores in the device
     * Each core is running at the middle frequency (frequency of position 5 in the list)
     * Then, this will return 20
     */
    private fun calculation(): Int {
        var sum = 0
        var i = 0
        while (i < numberOfCores && cpuCores[i]?.status!!) {
            sum += SearchAlgorithms.binarySearch(
                    cpuCores[i]?.currentFrequency!!,
                    cpuCores[i]?.frequencies!!
            )
            i++
        }
        return sum
    }

    /**
     * Method use to set the SeekBar Percentage in FeedBackPopUpWindow based on how many cores are on
     * and their respective frequencies
     * It finds THE OVERALL use of the system in percentage
     * @return
     */
    fun getSumNumberCore(): Int {
        return (calculation() * 100) / totalOfFrequencies
    }

    fun setToMinSpeed() {
        for (i in 0 until numberOfCores) {
            if(i < numberOfCores/2) {
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

    /**
     * Get the current frequency of each core, 0 if it's off
     *
     * @return arrayList of frequencies where each index is the frequency related to that core
     */
    fun getArrayListCoresSpeed(): ArrayList<Int> {
        val arrayList = ArrayList<Int>()
        for (i in 0 until numberOfCores) {
            arrayList.add(i, cpuCores[i]!!.currentFrequency)
        }
        return arrayList
    }

    /**
     * Method that receives how many values it should increase in frequencies IN A GLOBAL state
     * So, if you have 48 possible frequencies and converter = 12, then at least 2 cores should be
     * turned on
     *
     * @param converter
     */
    private fun setArrayListOfSpeedFromUserInput(converter: Int) {
        var converter = converter
        var i = 0
        var flag = true
        val arrayList = ArrayList<Int>()
        //It will iterate for every core
        //To understand this while imagine the following scenario:
        //You have 4 cores with 12 possible frequencies for each. Suppose that core 0 is on at max
        //and converter = 15.
        //First it will add to vector 12 and will subtract from converter 12, which left 3
        //Next iteration it will reach the second if add 3 to array and break.
        //This means that core 1 will be turned on and frequency will be set at maximum
        //While core 2 (because there still 3 left in the array) will be set to frequency in
        //third position of clockLevels matrix
        while (i < numberOfCores) {
            val amountOfValuesPerCore = cpuCores[i]!!.frequencies.size
            if (converter >= amountOfValuesPerCore) {
                arrayList.add(i, amountOfValuesPerCore)
                flag = false
            } else if (converter in 1 until amountOfValuesPerCore) {
                arrayList.add(i, converter)
                break
            } else if (converter == 0 && flag) {
                arrayList.add(i, 0)
                break
            } else
                break
            i++
            converter -= amountOfValuesPerCore
        }
        i = 0
        while (i < arrayList.size) {
            //Turn core i on and set it to maximum frequency
            val amountOfValuesPerCore = cpuCores[i]!!.frequencies.size
            if (arrayList[i] == amountOfValuesPerCore) {
                setCoreFrequency(i, cpuCores[i]!!.frequencies[arrayList[i] - 1])
            } else {
                setCoreFrequency(i, cpuCores[i]!!.frequencies[arrayList[i]])
            }
            i++
        }
        //Turn other cores off
        if (i < numberOfCores) {
            while (i < numberOfCores) {
                turnCoreOff(i)
                i++
            }
        }
    }

}