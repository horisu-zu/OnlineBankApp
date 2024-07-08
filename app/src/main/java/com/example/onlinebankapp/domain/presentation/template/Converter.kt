package com.example.onlinebankapp.domain.presentation.template

import android.graphics.Color.parseColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.example.onlinebankapp.R

fun String.toDrawableRes(): Int {
    return try {
        val field = R.drawable::class.java.getField(this)
        field.getInt(null)
    } catch (e: Exception) {
        R.drawable.ic_info
    }
}

fun Color.toHexString(): String {
    return String.format("#%06X", 0xFFFFFF and this.toArgb())
}

fun String.toColor(): Color {
    return try {
        when {
            startsWith("0x", ignoreCase = true) -> {
                val colorLong = this.substring(2).toLong(16)
                Color(colorLong.toInt())
            }
            else -> {
                Color(parseColor(this))
            }
        }
    } catch (e: IllegalArgumentException) {
        Color.Black
    }
}