package io.github.dracula101.todo.common.ui.utils

sealed class UiEvent {
    data class ShowSnackbar(
        val message: String,
        val action: String? = null
    ): UiEvent()
    object ShowBottomSheet: UiEvent()
}