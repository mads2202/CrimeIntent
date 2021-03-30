package com.mads2202.crimeintent

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import java.util.*

class DatePickerFragment : DialogFragment() {
    lateinit var mDatePicker:DatePicker
    companion object{
        val ARG_DATE="date"
        val EXTRA_DATE = "com.bignerdranch.android.criminalintent.date"
        fun newInstance(date:Date):DatePickerFragment{
            var args=Bundle()
            args.putSerializable(ARG_DATE,date)
            var fragment=DatePickerFragment()
            fragment.arguments=args
            return fragment
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var date=arguments!!.getSerializable(ARG_DATE) as Date
        var calendar=Calendar.getInstance()
        calendar.time=date
        var year=calendar.get(Calendar.YEAR)
        var month=calendar.get(Calendar.MONTH)
        var day=calendar.get(Calendar.DAY_OF_MONTH)
        var view=LayoutInflater.from(activity).inflate(R.layout.date_picker,null)
        mDatePicker=view!!.findViewById(R.id.dialog_date_picker)
        mDatePicker.init(year,month,day,null)
        return AlertDialog.Builder(activity)
            .setTitle(R.string.date_picker_title)
            .setView(view)
            .setPositiveButton(android.R.string.ok, object:DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    var year=mDatePicker.year
                    var month=mDatePicker.month
                    var day=mDatePicker.dayOfMonth
                    var date=GregorianCalendar(year,month,day).time
                    sendResult(Activity.RESULT_OK,date)
                }

            })
            .setNegativeButton(android.R.string.cancel, null)
            .create()
    }
    fun sendResult(resultCode:Int, date:Date){
        if(targetFragment==null)
            return
        else{
            var intent= Intent()
        intent.putExtra(EXTRA_DATE,date)
        targetFragment!!.onActivityResult(targetRequestCode,resultCode,intent)}
    }
}