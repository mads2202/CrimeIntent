package com.mads2202.crimeintent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var mFragmentManager: FragmentManager = supportFragmentManager
        var fragment: Fragment? = mFragmentManager.findFragmentById(R.id.fragments_container);
        if (fragment == null) {
            fragment = CrimeFragment();
            mFragmentManager.beginTransaction()
                .add(R.id.fragments_container, fragment)
                .commit();
        }
    }

}
