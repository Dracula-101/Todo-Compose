package io.github.dracula101.todo.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.dracula101.todo.data.repository.AuthRepository
import io.github.dracula101.todo.data.repository.TodoRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val todoRepository: TodoRepository,
) : ViewModel() {
    val completedTask = todoRepository.getCompletedTask()
    val unCompletedTask = todoRepository.getUnCompletedTask()

    val currentUser = authRepository.currentUser

    suspend fun logout() {
        authRepository.logout()
    }

}