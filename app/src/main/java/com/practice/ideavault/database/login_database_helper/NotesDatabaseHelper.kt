package com.practice.ideavault.database.login_database_helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.practice.ideavault.model_class.Note

class NotesDatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION){

    companion object{
        private const val DATABASE_NAME = "ideaVault.db"
        private const val  DATABASE_VERSION = 2
        private const val TABLE_NAME = "notes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_TIME = "time"
        private const val COLUMN_USER_EMAIL = "userEmail"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT, $COLUMN_TIME TEXT, $COLUMN_USER_EMAIL TEXT)"
        db!!.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val query = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(query)
        onCreate(db)
    }

    fun insertNote(note: Note, mail: String){
        try {
            val db = writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_TITLE, note.notesTitle)
                put(COLUMN_CONTENT, note.notesContent)
                put(COLUMN_TIME, note.notesEditTime)
                put(COLUMN_USER_EMAIL, mail)
            }
            if(note.notesTitle == "" && note.notesContent == ""){
                /**
                 *
                 * find better implementation
                 *
                 */
            }else {
                try {
                    db.insert(TABLE_NAME, null, values)
                } catch (e: Exception) {
                    onCreate(db)
                    db.insert(TABLE_NAME, null, values)
                }
            }
            db.close()
        }catch(e: Exception){
            Log.e("SGERROR",e.toString())
        }
    }

    fun updateNote(note: Note,mail: String){
        try {
            val db = writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_TITLE, note.notesTitle)
                put(COLUMN_CONTENT, note.notesContent)
                put(COLUMN_TIME, note.notesEditTime)
                put(COLUMN_USER_EMAIL, mail)
            }
            val whereClause = "$COLUMN_ID = ?"
            val whereArgs = arrayOf(note.id.toString())
            db.update(TABLE_NAME, values, whereClause, whereArgs)
            db.close()
        }catch(e: Exception){
            Log.e("SGERROR",e.toString())
        }
    }

    fun deleteNote(noteId: Int?){
        try {
            if(noteId != null) {
                val db = writableDatabase
                val whereClause = "$COLUMN_ID = ?"
                val whereArgs = arrayOf(noteId.toString())
                db.delete(TABLE_NAME, whereClause, whereArgs)
            }
        }catch (e: Exception){
            Log.e("SGERROR",e.toString())
        }
    }

    fun getAllNotes(email: String): List<Note>{
        val noteList = mutableListOf<Note>()
        try {
            val db = readableDatabase
            val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_USER_EMAIL = ?"
            val cursor = db.rawQuery(query, arrayOf(email))
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
                val time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME))
                noteList.add(Note(id, title, content, time))
            }
            cursor.close()
        }catch (e: Exception){
            Log.e("SGERROR",e.toString())
        }
        return noteList
    }

}