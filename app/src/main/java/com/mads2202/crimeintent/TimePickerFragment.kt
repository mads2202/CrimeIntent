package com.mads2202.crimeintent

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import java.time.LocalDateTime
import java.util.*

class TimePickerFragment : DialogFragment() {
    lateinit var mTimePicker:TimePicker
    companion object{
        val TIME_ARG="TimeArgs"
        val EXTRA_TIME = "com.bignerdranch.android.criminalintent.date"
        fun newInstance(date: Date):TimePickerFragment{
            var bundle=Bundle()
            bundle.putSerializable(TIME_ARG,date)
            var fragment=TimePickerFragment()
            fragment.arguments=bundle
            return fragment
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var view= LayoutInflater.from(activity).inflate(R.layout.time_picker,null)
        mTimePicker=view.findViewById(R.id.time_picker_fragment)
        var date=arguments!!.getSerializable(TIME_ARG) as Date
        var hour=DateFormat.format("HH",date).toString().toInt()
        var minute=DateFormat.format("mm",date).toString().toInt()

        Log.d("Mytag","$hour")
        mTimePicker.setIs24HourView(true)
        mTimePicker.hour=hour
        mTimePicker.minute=minute
        var intent=Intent()
        date.hours=mTimePicker.hour
        date.minutes=mTimePicker.minute
        intent.putExtra(EXTRA_TIME,date)
        targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK,intent)
        return AlertDialog.Builder(activity).setTitle("Выберите время").setPositiveButton(android.R.string.ok,object:DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                var intent=Intent()
                date.hours=mTimePicker.hour
                date.minutes=mTimePicker.minute
                intent.putExtra(EXTRA_TIME,date)
                targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK,intent)
            }
        })
            .setView(view)
            .create()
    }
}