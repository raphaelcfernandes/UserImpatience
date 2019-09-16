package com.android.battery.saver.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.battery.saver.R
import com.android.battery.saver.UI.PageHolderAdapter
import com.android.battery.saver.logger.Logger
import com.android.battery.saver.managers.CpuManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val adapter = PageHolderAdapter(applicationContext, supportFragmentManager)
        pager.adapter = adapter
        tab_layout.setupWithViewPager(pager)

//        var granted = false
//        val appOps = this
//            .getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager

//
//        val mode = appOps.checkOpNoThrow(
//            AppOpsManager.OPSTR_GET_USAGE_STATS,
//            android.os.Process.myUid(), this.packageName
//        )
//        if (mode == AppOpsManager.MODE_DEFAULT) {
//            granted =
//                this.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) === PackageManager.PERMISSION_GRANTED
//        } else {
//            granted = mode == AppOpsManager.MODE_ALLOWED
//        }
//        val builder = AlertDialog.Builder(this)
        //TODO
//        if (!granted) {
//            builder.setMessage("We need one more permission.\n You will be redirect to the settings to grant the user stats permission.")
//                .setTitle("Permission Required.")
//            builder.setPositiveButton("OK") { dialog, id ->
//                val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
//                startActivity(intent)
//        }
    }

}
