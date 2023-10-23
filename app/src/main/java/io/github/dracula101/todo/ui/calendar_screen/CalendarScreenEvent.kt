package io.github.dracula101.todo.ui.calendar_screen

import java.time.LocalDate

sealed class CalendarScreenEvent {
    object IsLoadingTodo: CalendarScreenEvent()
    object IsLoadingSucess: CalendarScreenEvent()
    data class OnDateChanged(val date: LocalDate): CalendarScreenEvent()
    data class OnTodoClick(val todo: String): CalendarScreenEvent()
}