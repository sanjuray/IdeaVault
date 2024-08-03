package com.practice.ideavault.fragment.notes_home

import android.content.Context
import androidx.lifecycle.ViewModel
import com.practice.ideavault.database.login_database_helper.NotesDatabaseHelper
import com.practice.ideavault.model_class.Note

class NotesHomeViewModel: ViewModel() {

    private var loggedInUserEmail: String = ""
    private var loggedInUserName: String = ""
    private lateinit var notesDatabaseHelper: NotesDatabaseHelper

    fun init(context: Context, email: String, name: String){
        loggedInUserEmail = email
        loggedInUserName = name
        notesDatabaseHelper = NotesDatabaseHelper(context)
    }

    fun getLoggedInUserName() = loggedInUserName

    fun getListOfNotes(): List<Note> = notesDatabaseHelper.getAllNotes(loggedInUserEmail)


}