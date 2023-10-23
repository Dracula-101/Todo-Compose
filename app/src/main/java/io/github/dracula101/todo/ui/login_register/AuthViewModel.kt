package io.github.dracula101.todo.ui.login_register

import android.util.Log
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.dracula101.todo.data.models.Resource
import io.github.dracula101.todo.data.repository.AuthRepository
import io.github.dracula101.todo.data.repository.User
import io.github.dracula101.todo.ui.login_register.AuthEvent.Success
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {
    private val _authEventFlow = MutableStateFlow<AuthEvent>(AuthEvent.StandBy)
    val authEventFlow: StateFlow<AuthEvent> = _authEventFlow

    private val _userAuthFlow = MutableStateFlow<User?>(null)
    val userAuthFlow: StateFlow<User?> = _userAuthFlow

    val currentUser: User?
        get() = repository.currentUser

    init {
        if(currentUser != null){
            _userAuthFlow.value = currentUser
        }
    }

    fun onAuthStateChanged(user: User?){
        if(user == null){
            _userAuthFlow.value = null
        }else{
            _userAuthFlow.value =  user
        }
    }

    fun onEvent(event: AuthEvent){
        viewModelScope.launch {
            when(event){
                is AuthEvent.RegisterClicked ->{
                    _authEventFlow.value = AuthEvent.Loading
                    val result = repository.register(
                        name = event.name,
                        email = event.email,
                        password = event.password
                    )
                    if(result is Resource.Success){
                        _authEventFlow.value = Success(result.result)
                    }else if (result is Resource.Failure){
                        if(result.exception is FirebaseAuthException){
                            _authEventFlow.value = AuthEvent.Error(result.exception.message ?: "Unknown Error")
                        }else {
                            _authEventFlow.value = AuthEvent.Error("Unknown Error")
                        }
                    }
                }
                is AuthEvent.LoginClicked ->{
                    _authEventFlow.value = AuthEvent.Loading
                    val result = repository.login(
                        email = event.email,
                        password = event.password
                    )
                    if(result is Resource.Success){
                        _authEventFlow.value = Success(result.result)
                    }else if (result is Resource.Failure){
                        if(result.exception is FirebaseAuthException){
                            _authEventFlow.value = AuthEvent.Error(result.exception.message ?: "Unknown Error")
                        }else {
                            _authEventFlow.value = AuthEvent.Error("Unknown Error")
                        }
                    }
                }
                is AuthEvent.GoogleSignInClicked->{
                    Log.d("AuthViewModel", "onEvent: ${event.intent}")
                    _authEventFlow.value = AuthEvent.Loading
                    val result = repository.signInWithGoogle(
                        intent = event.intent
                    )
                    if(result is Resource.Success){
                        _authEventFlow.value = Success(result.result)
                    }else if (result is Resource.Failure){
                        if(result.exception is FirebaseAuthException){
                            _authEventFlow.value = AuthEvent.Error(result.exception.message ?: "Unknown Error")
                        }else {
                            _authEventFlow.value = AuthEvent.Error("Unknown Error")
                        }
                    }
                }
                is AuthEvent.LogoutClicked ->{
                    repository.logout().let {
                        _userAuthFlow.value = null
                        _authEventFlow.value = AuthEvent.StandBy
                    }
                }
                is AuthEvent.StandBy -> _authEventFlow.value = AuthEvent.StandBy
                is AuthEvent.Loading -> _authEventFlow.value = AuthEvent.Loading
                else -> Unit
            }
        }
    }

    suspend fun getGoogleSignInIntent() = repository.getGoogleSignInIntent()
}