package io.github.dracula101.todo

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import io.github.dracula101.todo.common.ui.utils.Routes
import io.github.dracula101.todo.ui.home_screen.HomeScreen
import io.github.dracula101.todo.ui.intro.IntroScreen
import io.github.dracula101.todo.ui.intro.SplashScreen
import io.github.dracula101.todo.ui.login_register.AuthEvent
import io.github.dracula101.todo.ui.login_register.AuthScreen
import io.github.dracula101.todo.ui.login_register.AuthViewModel
import io.github.dracula101.todo.ui.theme.TodoTheme
import kotlinx.coroutines.launch


@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val baseNavController = rememberNavController()
            val authViewModel by viewModels<AuthViewModel>()
            authViewModel.onAuthStateChanged(authViewModel.currentUser)
            val userAuthFlow = authViewModel.userAuthFlow.collectAsState()
            LaunchedEffect(userAuthFlow.value){
                if(userAuthFlow.value == null){
                    baseNavController.navigate(Routes.AUTH_SCREEN){
                        popUpTo(Routes.SPLASH_SCREEN){
                            inclusive = true
                        }
                    }
                }else{
                    baseNavController.navigate(Routes.HOME_SCREEN){
                        popUpTo(Routes.SPLASH_SCREEN){
                            inclusive = true
                        }
                    }
                }
            }
            TodoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    NavHost(
                        navController = baseNavController,
                        startDestination = Routes.SPLASH_SCREEN,
                    ) {

                        composable(Routes.SPLASH_SCREEN) {
                            SplashScreen(
                                context = this@MainActivity,
                                navController = baseNavController
                            )
                        }
                        composable(Routes.INTRO_SCREEN){
                            IntroScreen(
                                navController = baseNavController
                            )
                        }
                        composable(Routes.AUTH_SCREEN){
                             AuthScreen(
                                    viewModel = authViewModel,
                                    context = this@MainActivity
                             )
                        }
                        composable(Routes.HOME_SCREEN) {
                            HomeScreen(
                                navController = baseNavController,
                                authViewModel = authViewModel,
                                onLogout = {
                                    authViewModel.viewModelScope.launch {
                                        authViewModel.onEvent(AuthEvent.LogoutClicked)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

}
