package com.mads2202.crimeintent

import android.content.Intent
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
import androidx.fragment.app.FragmentManager
import java.util.*


class CrimeFragment : Fragment() {
    private lateinit var mCrime: Crime
    private lateinit var mTitleField: EditText
    private lateinit var mDateButton: Button
    private lateinit var mSolvedCheckBox: CheckBox
    lateinit var mFirstElementButton: Button
    lateinit var mLastElementButton: Button

    companion object {
        val EXTRA_CRIME_ID = "com.mads2202.crimeintent.crime_id"
        val DIALOG_DATE="DialogDate"
        val REQUEST_DATE=0
        fun newInstance(id: UUID): CrimeFragment {
            var args: Bundle = Bundle()
            args.putSerializable(EXTRA_CRIME_ID, id)
            var fragment: CrimeFragment = CrimeFragment()
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var id: UUID = arguments!!.getSerializable(CrimePaperActivity.EXTRA_CRIME_ID) as UUID
        if (CrimeLab.getCrime(id) == null)
            mCrime = Crime()
        else
            mCrime = CrimeLab.getCrime(id)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_crime, container, false)
        mTitleField = mView.findViewById(R.id.crime_title)
        mDateButton = mView.findViewById(R.id.crime_date)
        mSolvedCheckBox = mView.findViewById(R.id.crime_solved)
        fillFragment(mCrime)
        mFirstElementButton = mView.findViewById(R.id.first_element_button)
        mFirstElementButton.setOnClickListener {
            CrimePaperActivity.mViewPager.currentItem=0

        }
        mLastElementButton = mView.findViewById(R.id.last_element_button)
        mLastElementButton.setOnClickListener {
            CrimePaperActivity.mViewPager.currentItem=CrimeLab.mCrimeList.size-1
        }
        return mView
    }

    fun  fillFragment(mCrime: Crime) {
        mTitleField.setText(mCrime.mTitle)
        mTitleField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mCrime.mTitle = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }
        })

        mDateButton.text = mCrime.mDate.toString()
        mDateButton.setOnClickListener {
            var fragmentManager=fragmentManager
            var datePickerFragment=DatePickerFragment.newInstance(mCrime.mDate)
            datePickerFragment.setTargetFragment(this,REQUEST_DATE)
            datePickerFragment.show(fragmentManager!!, DIALOG_DATE)
        }

        mSolvedCheckBox.setChecked(mCrime.mIsSolved)
        mSolvedCheckBox.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
            mCrime.mIsSolved = b
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==REQUEST_DATE){
           var date= data!!.getSerializableExtra(DatePickerFragment.EXTRA_DATE) as Date
            mCrime.mDate=date
            mDateButton.text = mCrime.mDate.toString()
        }
    }
}