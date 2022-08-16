package com.princeoprince.organisednote.utils

import com.princeoprince.organisednote.R

enum class AppBackgroundColour(val intColour: Int, val displayString: String) {

    GREEN(R.color.light_green, "Green"),
    ORANGE(R.color.light_orange, "Orange"),
    PURPLE(R.color.light_purple, "Purple");

    companion object {
        fun getColourByName(displayString: String) =
            when (displayString) {
                "Orange" -> ORANGE
                "Purple" -> PURPLE
                else -> GREEN
            }
    }
}