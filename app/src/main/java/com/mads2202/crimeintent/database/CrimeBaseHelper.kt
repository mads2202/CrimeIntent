package com.mads2202.crimeintent.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.mads2202.crimeintent.Crime

class CrimeBaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "create table " + CrimeDbSchema.CrimeTable.NAME +
                    "("
                    + CrimeDbSchema.CrimeTable.Cols.UUID +  ", "
                    + CrimeDbSchema.CrimeTable.Cols.TITLE + ", "
                    + CrimeDbSchema.CrimeTable.Cols.DATE +  ", "
                    + CrimeDbSchema.CrimeTable.Cols.SOLVED + ", "
                    + CrimeDbSchema.CrimeTable.Cols.SUSPECT+")"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    companion object {
        private const val VERSION = 1
        private const val DATABASE_NAME = "crimeBase.db"
    }
}
