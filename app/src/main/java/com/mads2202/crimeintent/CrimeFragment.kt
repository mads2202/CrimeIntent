package com.mads2202.crimeintent

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.mads2202.crimeintent.database.IncreasedPhotoDialog
import java.io.File
import java.util.*


class CrimeFragment : Fragment() {
    private lateinit var mCrime: Crime
    private lateinit var mTitleField: EditText
    private lateinit var mDateButton: Button
    private lateinit var mTimeButton:Button
    private lateinit var mSolvedCheckBox: CheckBox
    private lateinit var mFirstElementButton: Button
    private lateinit var mLastElementButton: Button
    private lateinit var mSendReportButton: Button
    lateinit var mSuspectButton: Button
    private lateinit var mPhotoButton: ImageButton
    private lateinit var mPhotoView: ImageView
    var callbacks:Callbacks?=null
    var mPhotoFile: File?= null

    companion object {
        private const val EXTRA_CRIME_ID = "com.mads2202.crimeintent.crime_id"
        const val DIALOG_DATE="DialogDate"
        const val DIALOG_TIME="DialogTime"
        const val DIALOG_PHOTO="DialogPhoto"
        const val REQUEST_DATE=0
        const val REQUEST_TIME=1
        const val REQUEST_CONTACT=2
        const val REQUEST_PHOTO=3
        fun newInstance(id: UUID): CrimeFragment {
            val args: Bundle = Bundle()
            args.putSerializable(EXTRA_CRIME_ID, id)
            val fragment: CrimeFragment = CrimeFragment()
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var id: UUID = arguments!!.getSerializable(CrimePaperActivity.EXTRA_CRIME_ID) as UUID
        if (CrimeLab.getCrime(id) == null){
            mCrime = Crime(UUID.randomUUID())
            CrimeLab.addCrime(mCrime)
            callbacks!!.onCrimeUpdated(mCrime)
        }
        else
            mCrime = CrimeLab.getCrime(id)!!
        mPhotoFile=CrimeLab.getPhotoFile(mCrime, activity!!)
        callbacks!!.onCrimeUpdated(mCrime)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mView = inflater.inflate(R.layout.fragment_crime, container, false)
        mPhotoButton=mView.findViewById(R.id.crime_camera)
        val intent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        mPhotoButton.setOnClickListener{
            val uri=FileProvider.getUriForFile(
                activity!!,
                "com.mads2202.crimeintent.criminalintent.fileprovider",
                mPhotoFile!!
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
            var cameraActivities= activity!!.packageManager.queryIntentActivities(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY
            )
            for (item in cameraActivities){
                activity!!.grantUriPermission(
                    item.activityInfo.packageName,
                    uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            }
            startActivityForResult(intent, REQUEST_PHOTO)
        }
        mPhotoView=mView.findViewById(R.id.crime_photo)
        mPhotoView.setOnClickListener{
            if(mPhotoFile!=null ){
              val increasedPhotoDialog=  IncreasedPhotoDialog.newInstance(BitmapFactory.decodeFile(mPhotoFile!!.path))
                increasedPhotoDialog.show(fragmentManager!!, DIALOG_PHOTO)

            }

        }
        updatePhotoView()


        mTitleField = mView.findViewById(R.id.crime_title)
        mDateButton = mView.findViewById(R.id.crime_date)
        mSolvedCheckBox = mView.findViewById(R.id.crime_solved)
        mTimeButton=mView.findViewById(R.id.time_button)
        mSendReportButton=mView.findViewById(R.id.send_report_button)
        mSendReportButton.setOnClickListener {
            var intent=Intent(Intent.ACTION_SEND)
            intent.type="text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport())
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_suspect_text))
            intent=Intent.createChooser(intent, R.string.crime_report.toString())
            startActivity(intent)
        }
        mSuspectButton=mView.findViewById(R.id.choose_suspect_button)
        mSuspectButton.setOnClickListener {
            val intent=Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            startActivityForResult(intent, REQUEST_CONTACT)
        }
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
                CrimeLab.updateCrime(mCrime)
                callbacks!!.onCrimeUpdated(mCrime)
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }
        })

        mDateButton.text = android.text.format.DateFormat.format(
            "EEEE, dd MMMM, yyyy",
            mCrime.mDate
        )
        callbacks!!.onCrimeUpdated(mCrime)
        mDateButton.setOnClickListener {
            val fragmentManager=fragmentManager
            val datePickerFragment=DatePickerFragment.newInstance(mCrime.mDate)
            datePickerFragment.setTargetFragment(this, REQUEST_DATE)
            datePickerFragment.show(fragmentManager!!, DIALOG_DATE)
            callbacks!!.onCrimeUpdated(mCrime)
        }
        mTimeButton.text=android.text.format.DateFormat.format("HH:mm:ss z", mCrime.mDate)
        mTimeButton.setOnClickListener {
            val timePickerFragment=TimePickerFragment.newInstance(mCrime.mDate)
            timePickerFragment.setTargetFragment(this, REQUEST_TIME)
            timePickerFragment.show(fragmentManager!!, DIALOG_TIME)
            callbacks!!.onCrimeUpdated(mCrime)
        }

        mSolvedCheckBox.setChecked(mCrime.mIsSolved)
        mSolvedCheckBox.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
            mCrime.mIsSolved = b
            callbacks!!.onCrimeUpdated(mCrime)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==REQUEST_DATE){
           val date= data!!.getSerializableExtra(DatePickerFragment.EXTRA_DATE) as Date
            mCrime.mDate=date
            CrimeLab.updateCrime(mCrime)
            callbacks!!.onCrimeUpdated(mCrime)
            mDateButton.text = android.text.format.DateFormat.format(
                "EEEE, dd MMMM, yyyy",
                mCrime.mDate
            )
        }
        if(requestCode== REQUEST_TIME){
            val date= data!!.getSerializableExtra(TimePickerFragment.EXTRA_TIME) as Date
            mCrime.mDate=date
            CrimeLab.updateCrime(mCrime)
            callbacks!!.onCrimeUpdated(mCrime)
            mTimeButton.text=android.text.format.DateFormat.format("HH:mm:ss z", mCrime.mDate)
        }
        if (requestCode== REQUEST_CONTACT){
            val contactUri: Uri? =data!!.data
            val queryStrings= arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
            val cursor: Cursor? =activity!!.contentResolver.query(
                contactUri!!,
                queryStrings,
                null,
                null,
                null
            )
            if(cursor!!.count==0)
                return
            else{
                cursor.moveToFirst()
                val suspect: String = cursor.getString(0)
                mCrime.suspect = suspect
                mSuspectButton.text = suspect
                cursor.close()
            }
        }
        if(requestCode== REQUEST_PHOTO){
            val uri=FileProvider.getUriForFile(activity!!,"com.mads2202.crimeintent.criminalintent.fileprovider",mPhotoFile!!)
            activity!!.revokeUriPermission(uri,Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            updatePhotoView()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.crime_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete_crime_item -> {
                CrimeLab.deleteCrime(mCrime)
                callbacks!!.onCrimeUpdated(mCrime)
                activity?.finish()
                return true
            }
            else->return super.onOptionsItemSelected(item)
        }
    }
    private fun getCrimeReport(): String {
        var solvedString: String? = null
        solvedString = if (mCrime.mIsSolved) {
            getString(R.string.crime_solved_label)
        } else {
            getString(R.string.crime_unsolved_label)
        }
        val dateFormat = "EEE, MMM dd"
        val dateString: String = android.text.format.DateFormat.format(
            dateFormat,
            mCrime.mDate
        ).toString()
        var suspect = mCrime.suspect
        suspect = if (suspect == null) {
            getString(R.string.crime_no_suspect_text)
        } else {
           getString(R.string.crime_suspect_text, suspect)

        }
        return getString(
            R.string.crime_report,
            mCrime.mTitle, dateString, solvedString, suspect
        )
    }
    private fun updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile!!.exists()) {
            mPhotoView.setImageDrawable(null)
        } else {
            val bitmap: Bitmap = PictureUtils.getScaledBitMap(
                mPhotoFile!!.path, activity!!
            )
            mPhotoView.setImageBitmap(bitmap)
        }
    }
    interface Callbacks{
        fun onCrimeUpdated(crime:Crime)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks=context as Callbacks
    }

    override fun onDetach() {
        super.onDetach()
        callbacks=null
    }
}