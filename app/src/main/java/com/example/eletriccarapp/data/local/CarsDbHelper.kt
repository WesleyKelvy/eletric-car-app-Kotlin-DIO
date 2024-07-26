package com.example.eletriccarapp.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.eletriccarapp.data.local.CarrosContract.SQL_DELETE_ENTRYS
import com.example.eletriccarapp.data.local.CarrosContract.TABLE_CAR

class CarsDbHelper(context: Context) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        null,
        DATABASE_VERSION
    ) {

    companion object {
        const val DATABASE_NAME = "dbCar.db"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(TABLE_CAR)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        db?.execSQL(SQL_DELETE_ENTRYS)
        onCreate(db)
    }

    override fun onDowngrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        onUpgrade(db, oldVersion, newVersion)
    }
}