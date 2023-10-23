package io.github.dracula101.todo.ui.login_register

import android.app.Activity.RESULT_OK
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    context: Context
){
    val uiEvents = viewModel.authEventFlow.collectAsState(initial = AuthEvent.StandBy)
    val coroutineScope = rememberCoroutineScope()
    val showLogin = rememberSaveable {
        mutableStateOf(true)
    }
    val scrollState = rememberScrollState()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult ={
            if(it.resultCode == RESULT_OK){
                if(it.data != null){
                    viewModel.onEvent(AuthEvent.GoogleSignInClicked(it.data!!))
                }
            }
        }
    )
    LaunchedEffect( uiEvents.value){
        when(val event = uiEvents.value){
            is AuthEvent.Success -> {
                coroutineScope.launch {
                    viewModel.onAuthStateChanged(event.user)
                }
            }
            is AuthEvent.Error -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }
    AnimatedContent(
        targetState = showLogin.value,
        transitionSpec = {
            if (targetState) {
                (slideInHorizontally(tween(500)) + fadeIn(tween(500))).togetherWith(
                    slideOutHorizontally(tween(500)) + fadeOut(tween(500))
                )
            } else {
                (slideInHorizontally(tween(500)) + fadeIn(tween(500))).togetherWith(
                    slideOutHorizontally(tween(500)) + fadeOut(tween(500))
                )
            }
        },
        label = "Login/Register Screen",
        modifier = Modifier.fillMaxSize().scrollable(
            state = scrollState,
            orientation = Orientation.Vertical,
            enabled = false
        )
    ){ targetState ->
        if(targetState){
            LoginScreen(
                loginUiEvent = uiEvents.value,
                onSubmitClicked = { email, password ->
                    viewModel.onEvent(AuthEvent.LoginClicked(email, password))
                },
                onRegisterClicked = {
                    showLogin.value = false
                },
                onGoogleSignInClicked = {
                    coroutineScope.launch {
                        val intentSender = viewModel.getGoogleSignInIntent()
                        launcher.launch(
                            IntentSenderRequest.Builder(intentSender).build()
                        )
                    }
                }
            )
        }else{
            RegisterScreen(
                onLoginClicked = {
                    showLogin.value = true
                },
                onSubmitClicked = { name, email, password ->
                    viewModel.onEvent(AuthEvent.RegisterClicked(name, email, password))
                },
                registerUiEvent = uiEvents.value,

            )
        }
    }
}