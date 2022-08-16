package com.princeoprince.organisednote.model

import java.io.Serializable
import java.util.*

data class Note(
    var fileName: String = "",
    var noteText: String = "",
    var priority: Int = 0,
    var dateModified: Date = Date()
) : Serializable {

    override fun toString(): String {
        return "$noteText$priority"
    }
}