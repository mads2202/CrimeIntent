package com.mads2202.crimeintent

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import java.util.*

class CrimePaperActivity : AppCompatActivity() {

    var mCrimes = mutableListOf<Crime>()
    companion object{
        val EXTRA_CRIME_ID="com.mads2202.crimeintent.crime_id"
        fun newIntent(context: Context, crimeId: UUID): Intent {
            var intent: Intent = Intent(context,CrimePaperActivity::class.java)
            intent.putExtra(EXTRA_CRIME_ID,crimeId)
            return intent
        }
        lateinit var mViewPager: ViewPager
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crime_paper)
        mViewPager = findViewById(R.id.crime_view_paper)
        var crimeId=intent.getSerializableExtra(EXTRA_CRIME_ID) as UUID
        var fragmentManager = supportFragmentManager
        mCrimes = CrimeLab.mCrimeList
        mViewPager.adapter = object : FragmentStatePagerAdapter(fragmentManager) {
            override fun getCount(): Int {
                return mCrimes.size
            }
            override fun getItem(position: Int): Fragment {
                var crime = mCrimes[position]
                return CrimeFragment.newInstance(crime.mId)
            }
        }
        for ((index,item) in mCrimes.withIndex()){
            if(item.mId==crimeId){
                mViewPager.currentItem=index
                break
            }
        }



    }

}