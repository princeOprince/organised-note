package com.princeoprince.organisednote.controller

import android.content.SharedPreferences
import com.princeoprince.organisednote.utils.AppBackgroundColour
import com.princeoprince.organisednote.utils.DEFAULT_COLOUR
import com.princeoprince.organisednote.utils.KEY_APP_BACKGROUND_COLOUR
import com.princeoprince.organisednote.utils.NoteSortOrder

class NotePrefs(private val sharedPrefs: SharedPreferences) {

    fun saveNoteSortOrder(noteSortOrder: NoteSortOrder) {

    }

    fun getNoteSortOrder()  = NoteSortOrder.FILENAME_ASC

    fun saveNotePriorityFilters(priorities: Set<String>) {

    }

    fun getNotePriorityFilters(): Set<String> = setOf()

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