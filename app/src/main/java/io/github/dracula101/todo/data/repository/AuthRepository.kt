package io.github.dracula101.todo.data.repository

import android.content.Intent
import android.content.IntentSender
import io.github.dracula101.todo.data.models.Resource

interface AuthRepository {
    val currentUser: User?
    suspend fun login(email: String, password: String): Resource<User>
    suspend fun register(name: String, email: String, password: String): Resource<User>
    suspend fun signInWithGoogle(intent: Intent): Resource<User>
    suspend fun getGoogleSignInIntent(): IntentSender
    suspend fun logout()
}