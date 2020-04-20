package com.android.battery.saver.ui.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.android.battery.saver.R
import com.android.battery.saver.activities.SpinnerActivity
import com.android.battery.saver.helper.Preferences
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_configuration.view.*

class ConfigurationFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_configuration, container, false)
        val readTA = view.readTA
        val timeWindow = view.timeWindow
        val amountToIncrease = view.increaseFrequency
        val reduceFrequencyAmount = view.reduceFrequency
        val impatienceLevel = view.impatienceLevel
        val training = view.training
        val spinner: Spinner = view.findViewById(R.id.governors_spinner)
        spinner.onItemSelectedListener = SpinnerActivity(view, context!!)
        val button = view.save
        button.setOnClickListener {
            val governor = spinner.selectedItem.toString()
            Preferences.setGovernor(context!!, governor)
            if (governor == "userspace") {
                Preferences.setReadTAInterval(context!!,
                        readTA.editText?.text.toString().toInt())
                Preferences.setDecreaseCPUInterval(context!!,
                        timeWindow.editText?.text.toString().toInt())
                Preferences.setMarginToIncreaseCpu(context!!,
                        amountToIncrease.editText?.text.toString().toInt())
                Preferences.setDecreaseCPUFrequency(context!!,
                        reduceFrequencyAmount.editText?.text.toString().toInt())
                Preferences.setImpatienceLevel(context!!, impatienceLevel.editText?.text.toString().toInt())
                Preferences.setTrainer(context!!, training.isChecked)
                Preferences.setIteration(context!!, 0)
            }

            Snackbar.make(view, "$governor selected", Snackbar.LENGTH_SHORT).show()
        }
        return view
    }
}