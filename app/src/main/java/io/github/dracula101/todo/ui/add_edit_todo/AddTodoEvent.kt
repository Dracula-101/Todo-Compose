package io.github.dracula101.todo.ui.add_edit_todo

sealed class AddTodoEvent {
    data class OnTitleChange(val title: String): AddTodoEvent()
    data class OnDescriptionChange(val description: String): AddTodoEvent()
    object OnSaveTodoClick: AddTodoEvent()
}