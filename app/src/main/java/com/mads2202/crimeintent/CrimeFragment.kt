package com.mads2202.crimeintent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import androidx.fragment.app.Fragment


class CrimeFragment: Fragment() {
    private lateinit var  mCrime:Crime
    private lateinit var mTitleField:EditText
    private lateinit var mDateButton:Button
    private lateinit var mSolvedCheckBox:CheckBox
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCrime= Crime()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView= inflater.inflate(R.layout.fragment_crime,container,false)
        mTitleField=mView.findViewById(R.id.crime_title)
        mTitleField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mCrime.mTitle=s.toString()
            }
            override fun afterTextChanged(s: Editable?) {
                //
            }
        })
        mDateButton=mView.findViewById(R.id.crime_date)
        mDateButton.text=mCrime.mDate.toString()
        mDateButton.isEnabled = false
        mSolvedCheckBox=mView.findViewById(R.id.crime_solved)
        mSolvedCheckBox.setOnCheckedChangeListener{ compoundButton: CompoundButton, b: Boolean -> mCrime.mIsSolved=true}
        return mView
    }
}