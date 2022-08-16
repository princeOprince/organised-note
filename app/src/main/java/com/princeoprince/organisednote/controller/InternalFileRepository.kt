package com.princeoprince.organisednote.controller

import android.content.Context
import com.princeoprince.organisednote.model.Note
import com.princeoprince.organisednote.utils.*
import java.util.*

class InternalFileRepository(private var context: Context) : NoteRepository {

    private val allNotes by lazy { getNotes() }

    override fun addNote(note: Note): Boolean {
        val text = note.toString()

        if (!noteFile(note.fileName, noteDirectory(context)).exists()) {
            context.openFileOutput(note.fileName, Context.MODE_PRIVATE).use {
                it.write(text.toByteArray())
            }
            allNotes.add(note)
            return true
        }

        return false
    }

    override fun getNote(fileName: String): Note {
        val note = Note(fileName, "")
        context.openFileInput(fileName).use {
            val text = it.bufferedReader().use {
                it.readText()
            }
            note.dateModified = Date(noteFile(fileName, noteDirectory(context)).lastModified())
            note.priority = text.takeLast(PRIORITY).toInt()
            note.noteText = text.dropLast(PRIORITY)
        }
        return note
    }

    override fun deleteNote(fileName: String): Boolean {
        allNotes.removeIf { it.fileName == fileName }
        return noteFile(fileName, noteDirectory(context)).delete()
    }

    override fun editNote(note: Note) {
        note.noteText = note.toString()
        context.openFileOutput(note.fileName, Context.MODE_PRIVATE).use {
            it.write(note.noteText.toByteArray())
        }
    }

    override fun getNotes(): MutableList<Note> {
        return context.filesDir.listFiles()?.map {
            getNote(it.name)
        }?.toMutableList() ?: mutableListOf()
    }

    override fun getNotesWithPrioritySortedBy(
        priorities: Set<String>,
        order: NoteSortOrder
    ): List<Note> {

        val filteredNotes: List<Note> =
            if (priorities.isEmpty()) allNotes
            else allNotes.filter { priorities.contains(it.priority.toString()) }

        return when (order) {
            NoteSortOrder.FILENAME_ASC -> filteredNotes.sortedBy { it.fileName }
            NoteSortOrder.FILENAME_DESC -> filteredNotes.sortedByDescending { it.fileName }
            NoteSortOrder.DATE_LAST_MOD_ASC -> filteredNotes.sortedBy { it.dateModified }
            NoteSortOrder.DATE_LAST_MOD_DESC -> filteredNotes.sortedByDescending { it.dateModified }
            NoteSortOrder.PRIORITY_ASC -> filteredNotes.sortedBy { it.priority }
            else -> filteredNotes.sortedByDescending { it.priority }
        }
    }
}
