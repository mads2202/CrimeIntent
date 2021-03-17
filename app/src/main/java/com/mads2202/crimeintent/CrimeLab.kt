package com.mads2202.crimeintent

import android.content.Context
import java.util.*

object CrimeLab {
    var mCrimeList= mutableListOf<Crime>()


    fun fillCrimeList(){
        for(x in 0..100){
            var mCrime=Crime()
            mCrime.mTitle="# $x"
            mCrime.mIsSolved=x%2==0
            mCrime.mRequiresPolice=x%2==0
            mCrimeList.add(mCrime)
        }
    }
    fun getCrime(uuid: UUID): Crime? {
        mCrimeList.forEach {
            if( it.mId==uuid)
                return it }
        return null
    }


}