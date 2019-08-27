package com.android.battery.saver.UI.tabs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.battery.saver.R
import com.android.battery.saver.database.AppDbHelper
import com.android.battery.saver.managers.CpuManager
import com.android.battery.saver.services.BackgroundService

class MainFragment : Fragment() {
    private var cpu = CpuManager
    private lateinit var db: AppDbHelper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        activity?.startService(
            Intent(
                activity,
                BackgroundService::class.java
            )
        ) //Start the background
        return view

    }
}