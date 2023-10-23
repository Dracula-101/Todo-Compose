package io.github.dracula101.todo.ui.focus_screen

sealed class FocusScreenEvent {
    object IsLoaded: FocusScreenEvent()
    object PermissionNotGranted: FocusScreenEvent()
    object IsLoading : FocusScreenEvent()
}