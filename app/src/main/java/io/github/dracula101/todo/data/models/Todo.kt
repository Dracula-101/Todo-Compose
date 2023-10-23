package io.github.dracula101.todo.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(
    val title: String,
    val description: String?,
    val isDone: Boolean,
    val date: String,
    val time: String,
    val priority: Int,
    val categoryId: Int,
    @PrimaryKey val id: Int? = null
)