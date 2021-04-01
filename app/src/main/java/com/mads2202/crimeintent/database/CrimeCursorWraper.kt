package com.mads2202.crimeintent.database

import android.database.Cursor
import android.database.CursorWrapper
import com.mads2202.crimeintent.Crime
import java.util.*

class CrimeCursorWraper(cursor: Cursor):CursorWrapper(cursor) {
    fun getCrime():Crime{
        var crime=Crime()
        crime.mId=UUID.fromString(getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.UUID)))
        crime.mTitle=getString(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.TITLE))
        crime.mDate=Date(getLong(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.DATE)))
        crime.mIsSolved= getInt(getColumnIndex(CrimeDbSchema.CrimeTable.Cols.SOLVED))==1
        return crime
    }
}