package com.android.battery.saver.activities

import android.content.Context
import android.view.View
import android.widget.AdapterView
import kotlinx.android.synthetic.main.fragment_configuration.view.*

class SpinnerActivity(view: View, context: Context) : AdapterView.OnItemSelectedListener {
    private var mView = view
    private var mContext = context
    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, v: View?, pos: Int, id: Long) {
        val readTA = mView.readTA
        val timeWindow = mView.timeWindow
        if (parent?.getItemAtPosition(pos).toString() == "UImpatience") {
            readTA.visibility = View.VISIBLE
            timeWindow.visibility = View.VISIBLE
        } else {
            readTA.visibility = View.INVISIBLE
            timeWindow.visibility = View.INVISIBLE
        }
        mView.save.isEnabled = pos != 0
    }
}