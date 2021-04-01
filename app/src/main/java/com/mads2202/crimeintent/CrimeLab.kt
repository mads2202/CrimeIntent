package com.mads2202.crimeintent

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.mads2202.crimeintent.database.CrimeBaseHelper
import com.mads2202.crimeintent.database.CrimeCursorWraper
import com.mads2202.crimeintent.database.CrimeDbSchema
import com.mads2202.crimeintent.database.CrimeDbSchema.CrimeTable
import java.util.*


object CrimeLab {

    lateinit var mDataBase: SQLiteDatabase
    fun initDB(context: Context) {
        mDataBase = CrimeBaseHelper(context).writableDatabase
    }

    fun addCrime(crime: Crime){
        mDataBase.insert(CrimeDbSchema.CrimeTable.NAME, null, getContentValues(crime))
    }

    fun getCrime(id: UUID): Crime? {
        return null
        val cursor: CrimeCursorWraper = queryCrimes(
            CrimeTable.Cols.UUID + " = ?", arrayOf(id.toString())
        )
        return try {
            if (cursor.count == 0) {
                return null
            }
            cursor.moveToFirst()
            cursor.getCrime()
        } finally {
            cursor.close()
        }
    }
    fun updateCrime(crime: Crime){
        mDataBase.update(
            CrimeDbSchema.CrimeTable.NAME,
            getContentValues(crime),
            CrimeDbSchema.CrimeTable.Cols.UUID + " = ?",
            arrayOf(crime.mId.toString())
        )

    }

    fun getContentValues(crime: Crime):ContentValues{
        val values=ContentValues()
        values.put(CrimeDbSchema.CrimeTable.Cols.UUID, crime.mId.toString())
        values.put(CrimeDbSchema.CrimeTable.Cols.TITLE, crime.mTitle)
        values.put(CrimeDbSchema.CrimeTable.Cols.DATE, crime.mDate.time)
        if(crime.mIsSolved)
        values.put(CrimeDbSchema.CrimeTable.Cols.SOLVED, 1)
        else
            values.put(CrimeDbSchema.CrimeTable.Cols.SOLVED, 0)
        return values
    }
    private fun queryCrimes(whereClause: String?, whereArgs: Array<String>?):CrimeCursorWraper{
        val cursor=mDataBase.query(
            CrimeDbSchema.CrimeTable.NAME,
            null,
            whereClause,
            whereArgs,
            null,
            null,
            null
        )
        return CrimeCursorWraper(cursor)
    }
    fun getCrimes(): List<Crime>? {
        val crimes= mutableListOf<Crime>()
        val cursor: CrimeCursorWraper = queryCrimes(null, null)
        try {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                crimes.add(cursor.getCrime())
                cursor.moveToNext()
            }
        } finally {
            cursor.close()
        }
        return crimes
    }
    fun deleteCrime(crime:Crime){
        mDataBase.delete(CrimeDbSchema.CrimeTable.NAME,CrimeTable.Cols.UUID + " = ?", arrayOf(crime.mId.toString()))
    }

}