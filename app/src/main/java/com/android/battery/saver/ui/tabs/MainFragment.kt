package com.android.battery.saver.ui.tabs

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.android.battery.saver.NotificationBar
import com.android.battery.saver.R
import com.android.battery.saver.helper.Preferences
import com.android.battery.saver.managers.CpuManager
import com.android.battery.saver.services.BackgroundService
import com.android.battery.saver.ui.BubbleButton
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {
    private val n: NotificationBar = NotificationBar()
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        val activate = view.findViewById(R.id.activate) as Button
        val deactivate = view.findViewById(R.id.deactivate) as Button
        if (Preferences.getBackgroundServiceStatus(context!!)?.toBoolean() == true) {
            activate.isEnabled = false
            deactivate.isEnabled = true
        }
        activate.setOnClickListener {
            if (Preferences.getGovernor(context!!) == null) {
                Snackbar.make(view, R.string.noGovernorSelected, Snackbar.LENGTH_LONG).show()
            } else {
                activate.isEnabled = false
                deactivate.isEnabled = true
                val governor = Preferences.getGovernor(context!!)
//                val governor = "UImpatience"
                CpuManager.setGovernorFromSpinner(governor!!)

                if (Preferences.getGovernor(context!!) == "UImpatience") {
                    Preferences.setBackgroundServiceStatus(context!!, true)
                    activity?.startService(Intent(activity, BackgroundService::class.java))
                    n.createSpeedUpNotification(context!!)
                }
            }
        }

        deactivate.setOnClickListener {
            if (Preferences.getGovernor(context!!) == "UImpatience") {
                activity?.stopService(Intent(activity, BackgroundService::class.java))
                n.removeNotification(context!!)
            }
            activate.isEnabled = true
            deactivate.isEnabled = false
            Preferences.clearPreferences(context!!)
        }

//        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
//        startActivity(intent)
        return view
    }
}