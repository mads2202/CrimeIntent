package com.mads2202.crimeintent

import android.content.Context
import java.util.*

object CrimeLab {
    var mCrimeList= mutableListOf<Crime>()



    fun getCrime(uuid: UUID): Crime? {
        mCrimeList.forEach {
            if( it.mId==uuid)
                return it }
        return null
    }


}