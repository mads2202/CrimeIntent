package com.mads2202.crimeintent

import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment

class CrimeListActivity: SingleFragmentActivity(),Callbacks,CrimeFragment.Callbacks {
    override fun createFragment(): Fragment? {
        return CrimeListFragment()
    }

    override fun onCrimeSelected(crime: Crime) {
        if(findViewById<View>(R.id.detail_fragment_container)==null){
            val intent=CrimePaperActivity.newIntent(this,crime.mId)
            startActivity(intent)
        }else{
            val newDetail=CrimeFragment.newInstance(crime.mId)
            supportFragmentManager.beginTransaction().replace(R.id.detail_fragment_container,newDetail).commit()
        }
    }

    override fun onCrimeUpdated(crime: Crime) {
        var listFragment=supportFragmentManager.findFragmentById(R.id.fragments_container) as CrimeListFragment
        listFragment.updateUi()
    }


}