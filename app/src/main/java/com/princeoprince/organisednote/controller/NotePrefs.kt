package com.princeoprince.organisednote.controller

import android.content.SharedPreferences
import com.princeoprince.organisednote.utils.*

class NotePrefs(private val sharedPrefs: SharedPreferences) {

    fun saveNoteSortOrder(noteSortOrder: NoteSortOrder) {
        sharedPrefs.edit()
            .putString(KEY_NOTE_SORT_PREFERENCE, noteSortOrder.name)
            .apply()
    }

    fun getNoteSortOrder()  =
        NoteSortOrder.valueOf(
            sharedPrefs.getString(KEY_NOTE_SORT_PREFERENCE, DEFAULT_SORT_ORDER)
                ?: DEFAULT_SORT_ORDER
        )

    fun saveNotePriorityFilters(priorities: Set<String>) {
        sharedPrefs.edit()
            .putStringSet(KEY_NOTE_PRIORITY_SET, priorities)
            .apply()
    }

    fun getNotePriorityFilters(): Set<String> =
        sharedPrefs.getStringSet(KEY_NOTE_PRIORITY_SET,
            setOf(DEFAULT_PRIORITY_FILTER))
            ?: setOf(DEFAULT_PRIORITY_FILTER)

    fun saveNoteBackgroundColour(noteBackgroundColour: String) {
       sharedPrefs.edit()
            .putString(KEY_APP_BACKGROUND_COLOUR, noteBackgroundColour)
            .apply()
    }

    fun getAppBackgroundColour(): AppBackgroundColour =
        AppBackgroundColour.getColourByName(
            sharedPrefs.getString(KEY_APP_BACKGROUND_COLOUR, DEFAULT_COLOUR)
                ?: DEFAULT_COLOUR
        )
}