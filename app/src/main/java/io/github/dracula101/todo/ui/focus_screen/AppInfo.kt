package io.github.dracula101.todo.ui.focus_screen

import android.graphics.drawable.Drawable

data class AppInfo(
    val appName: String,
    val packageName: String,
    val icon: Drawable?,
    val timeSpent: Long,
    val isSystemApp: Boolean,
    val readableTimeSpent: String,
)
