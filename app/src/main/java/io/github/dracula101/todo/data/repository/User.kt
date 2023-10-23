package io.github.dracula101.todo.data.repository

import com.google.firebase.auth.FirebaseUser

data class User(
    val name: String,
    val email: String,
    val id: String,
    val profilePic : String? = null
)
fun FirebaseUser.toUser(): User {
    return User(
        name = displayName ?: "",
        email = email ?: "",
        id = uid,
        profilePic = photoUrl.toString()
    )
}