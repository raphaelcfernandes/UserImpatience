package com.android.battery.saver.UI.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.android.battery.saver.R
import com.android.battery.saver.activities.SpinnerActivity
import com.android.battery.saver.helper.Preferences
import kotlinx.android.synthetic.main.fragment_configuration.view.*

class ConfigurationFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_configuration, container, false)
        val readTA = view.readTA
        val timeWindow = view.timeWindow
        val amountToIncrease = view.increaseFrequency
        val reduceFrequencyAmount = view.reduceFrequency
        val spinner: Spinner = view.findViewById(R.id.governors_spinner)
        spinner.onItemSelectedListener = SpinnerActivity(view, context!!)
        val button = view.save
        button.setOnClickListener {
            val governor = spinner.selectedItem.toString()
            Preferences.setGovernor(context!!, governor)
            if (governor == "UImpatience") {
                Preferences.setReadTAInterval(context!!,
                        readTA.editText?.text.toString())
                Preferences.setDecreaseCPUInterval(context!!,
                        timeWindow.editText?.text.toString().toInt())
                Preferences.setMarginToIncreaseCpu(context!!,
                        amountToIncrease.editText?.text.toString())
                Preferences.setDecreaseCPUFrequency(context!!,
                        reduceFrequencyAmount.editText?.text.toString().toInt())
            }
        }
        return view
    }
}