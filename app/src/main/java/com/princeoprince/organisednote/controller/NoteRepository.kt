package com.princeoprince.organisednote.controller

import com.princeoprince.organisednote.model.Note
import com.princeoprince.organisednote.utils.NoteSortOrder

interface NoteRepository {
    fun addNote(note: Note): Boolean
    fun getNote(fileName: String): Note
    fun deleteNote(fileName: String): Boolean
    fun editNote(note: Note)
    fun getNotes(): List<Note>
    fun getNotesWithPrioritySortedBy(priorities: Set<String>, order: NoteSortOrder): List<Note>
}
