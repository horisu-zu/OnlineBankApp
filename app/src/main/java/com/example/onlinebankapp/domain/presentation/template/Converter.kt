package com.example.onlinebankapp.domain.presentation.template

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
        Color(android.graphics.Color.parseColor(this))
    } catch (e: IllegalArgumentException) {
        Color.Black
    }
}