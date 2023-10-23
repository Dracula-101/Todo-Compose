package io.github.dracula101.todo.ui.todo_list

import io.github.dracula101.todo.data.models.Todo

sealed class TodoCardEvent {
    data class OnDeleteTodoClick(val todo: Todo): TodoCardEvent()
    data class OnDoneChange(val todo: Todo, val isDone: Boolean): TodoCardEvent()
    object OnUndoDeleteClick: TodoCardEvent()
    data class OnTodoClick(val todo: Todo): TodoCardEvent()
    object OnAddTodoClick: TodoCardEvent()
}