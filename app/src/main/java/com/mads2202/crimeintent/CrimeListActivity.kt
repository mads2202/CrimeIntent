package com.mads2202.crimeintent

import androidx.fragment.app.Fragment

class CrimeListActivity: SingleFragmentActivity() {
    override fun createFragment(): Fragment? {
        return CrimeListFragment()
    }
}