package com.mads2202.crimeintent

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class CrimeActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment? {
        return CrimeFragment()

    }



}
