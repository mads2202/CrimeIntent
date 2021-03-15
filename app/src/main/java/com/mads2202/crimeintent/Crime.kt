package com.mads2202.crimeintent

import java.util.*

class Crime{
     val mId:UUID
     var mTitle:String?=null
     var mDate:Date
     var mIsSolved:Boolean?=null

    init {
        mId= UUID.randomUUID()
        mDate=Date()
    }




}