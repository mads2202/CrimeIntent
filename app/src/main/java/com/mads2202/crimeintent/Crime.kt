package com.mads2202.crimeintent

import java.util.*

class Crime(val mId: UUID) {
    var mTitle: String? = null
    var mDate: Date
    var mIsSolved: Boolean
    var mRequiresPolice: Boolean = false
    var suspect: String? = null

    init {
        mId
        mDate = Date()
        mIsSolved = false
    }
    fun getPhotoName():String{
        return "IMG_${mId.toString()}.jpg"
    }


}