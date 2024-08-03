package com.practice.ideavault.model_class

import java.io.Serializable

data class Note(
    val id: Int?, var notesTitle: String, var notesContent: String, var notesEditTime: String) : Serializable
