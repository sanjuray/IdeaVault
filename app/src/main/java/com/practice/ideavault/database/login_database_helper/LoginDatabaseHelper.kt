package com.practice.ideavault.database.login_database_helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class LoginDatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME,null,DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME = "ideaVault.db"
        private const val  DATABASE_VERSION = 1
        private const val TABLE_NAME = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_NAME = "displayName"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_EMAIL TEXT NOT NULL UNIQUE, $COLUMN_NAME TEXT)"
        db!!.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val query = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(query)
        onCreate(db)
    }

    fun insertUser(email: String, displayName: String): Boolean{
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_EMAIL,email)
            put(COLUMN_NAME, displayName)
        }
        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result.toInt() != -1
    }

    fun isLogin(email: String): Boolean{
        try {
            val db = writableDatabase
            val query = "SELECT * FROM $TABLE_NAME where $COLUMN_EMAIL = ?"
            val cursor = db.rawQuery(query, arrayOf(email))
            val ans = cursor.count != 0
            cursor.close()
            return ans
        }catch (e: Exception){
            Log.e("SGERROR",e.toString())
        }
        return true
    }
}