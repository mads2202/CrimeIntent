package com.mads2202.crimeintent

import android.R
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


abstract class SingleFragmentActivity:AppCompatActivity() {
    protected abstract fun createFragment(): Fragment?
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        var mFragmentManager: FragmentManager = supportFragmentManager
        var fragment: Fragment? = mFragmentManager.findFragmentById(com.mads2202.crimeintent.R.id.fragments_container);
        if (fragment == null) {
            fragment = createFragment();
            mFragmentManager.beginTransaction()
                .add(com.mads2202.crimeintent.R.id.fragments_container, fragment!!)
                .commit();
        }
    }
    @LayoutRes
    fun getLayoutResId():Int{
        return com.mads2202.crimeintent.R.layout.activity_masterdetail
    }
}