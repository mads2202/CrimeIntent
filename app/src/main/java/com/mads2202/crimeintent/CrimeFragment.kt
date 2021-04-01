package com.mads2202.crimeintent

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import java.text.DateFormat
import java.util.*


class CrimeFragment : Fragment() {
    private lateinit var mCrime: Crime
    private lateinit var mTitleField: EditText
    private lateinit var mDateButton: Button
    private lateinit var mTimeButton:Button
    private lateinit var mSolvedCheckBox: CheckBox
    lateinit var mFirstElementButton: Button
    lateinit var mLastElementButton: Button

    companion object {
        val EXTRA_CRIME_ID = "com.mads2202.crimeintent.crime_id"
        val DIALOG_DATE="DialogDate"
        val DIALOG_TIME="DialogTime"
        val REQUEST_DATE=0
        val REQUEST_TIME=1
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
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var mView = inflater.inflate(R.layout.fragment_crime, container, false)
        mTitleField = mView.findViewById(R.id.crime_title)
        mDateButton = mView.findViewById(R.id.crime_date)
        mSolvedCheckBox = mView.findViewById(R.id.crime_solved)
        mTimeButton=mView.findViewById(R.id.time_button)
        fillFragment(mCrime)
        mFirstElementButton = mView.findViewById(R.id.first_element_button)
        mFirstElementButton.setOnClickListener {
            CrimePaperActivity.mViewPager.currentItem=0

        }
        mLastElementButton = mView.findViewById(R.id.last_element_button)
        mLastElementButton.setOnClickListener {
            CrimePaperActivity.mViewPager.currentItem=CrimeLab.getCrimes()!!.size-1

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

        mDateButton.text = android.text.format.DateFormat.format("EEEE, dd MMMM, yyyy",mCrime.mDate)
        mDateButton.setOnClickListener {
            var fragmentManager=fragmentManager
            var datePickerFragment=DatePickerFragment.newInstance(mCrime.mDate)
            datePickerFragment.setTargetFragment(this,REQUEST_DATE)
            datePickerFragment.show(fragmentManager!!, DIALOG_DATE)
        }
        mTimeButton.text=android.text.format.DateFormat.format("HH:mm:ss z",mCrime.mDate)
        mTimeButton.setOnClickListener {
            var timePickerFragment=TimePickerFragment.newInstance(mCrime.mDate)
            timePickerFragment.setTargetFragment(this, REQUEST_TIME)
            timePickerFragment.show(fragmentManager!!,DIALOG_TIME)
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
            mDateButton.text = android.text.format.DateFormat.format("EEEE, dd MMMM, yyyy",mCrime.mDate)
        }
        if(requestCode== REQUEST_TIME){
            var date= data!!.getSerializableExtra(TimePickerFragment.EXTRA_TIME) as Date
            mCrime.mDate=date
            mTimeButton.text=android.text.format.DateFormat.format("HH:mm:ss z",mCrime.mDate)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.crime_fragment_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete_crime_item->{
               CrimeLab.deleteCrime(mCrime)
                activity?.finish()
            return true}
            else->return super.onOptionsItemSelected(item)
        }
    }
}