package io.github.dracula101.todo.ui.login_register

import android.content.Intent
import io.github.dracula101.todo.data.repository.User

sealed interface  AuthEvent {
    object StandBy: AuthEvent
    object Loading: AuthEvent
    data class Success(val user: User): AuthEvent
    data class Error(val message: String): AuthEvent
    data class LoginClicked(val email: String, val password: String): AuthEvent
    data class RegisterClicked(val name:String,val email: String, val password: String): AuthEvent
    data class GoogleSignInClicked(val intent: Intent) : AuthEvent
    object LogoutClicked: AuthEvent
}