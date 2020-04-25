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
        val frequency = mView.increaseFrequency
        val reduceFrequencyAmount = mView.reduceFrequency
        val impatienceLevel = mView.impatienceLevel
        val training = mView.training
        if (parent?.getItemAtPosition(pos).toString() == "userspace") {
            readTA.visibility = View.VISIBLE
            timeWindow.visibility = View.VISIBLE
            frequency.visibility = View.VISIBLE
            reduceFrequencyAmount.visibility = View.VISIBLE
            impatienceLevel.visibility = View.VISIBLE
            training.visibility = View.VISIBLE
        } else {
            readTA.visibility = View.GONE
            timeWindow.visibility = View.GONE
            frequency.visibility = View.GONE
            reduceFrequencyAmount.visibility = View.GONE
            impatienceLevel.visibility = View.GONE
            training.visibility = View.GONE
        }
        mView.save.isEnabled = pos != 0
    }
}