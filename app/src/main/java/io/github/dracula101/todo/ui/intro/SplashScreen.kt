package io.github.dracula101.todo.ui.intro

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.github.dracula101.todo.R
import io.github.dracula101.todo.common.ui.utils.Routes
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(context: Context, navController : NavController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val isSplashScreenVisible = remember {
            mutableStateOf(false)
        }
        val SPLASH_SCREEN_START_DURATION = 200L
        val SPLASH_SCREEN_ANIM_DURATION = 500L
        val NAVIGATE_DURATION = 300L
        LaunchedEffect(
            key1 = Unit,
            block = {
                delay(SPLASH_SCREEN_START_DURATION)
                isSplashScreenVisible.value = true
                delay(SPLASH_SCREEN_ANIM_DURATION + NAVIGATE_DURATION)
                navController.navigate(Routes.INTRO_SCREEN)
            }
        )
        AnimatedContent(
            targetState = isSplashScreenVisible.value,
            label = "Splash Screen",
            contentAlignment = Alignment.Center,
            transitionSpec = {
                scaleIn(
                    animationSpec = tween(durationMillis =SPLASH_SCREEN_ANIM_DURATION.toInt() ),
                    initialScale = 0.3F
                ) + fadeIn(
                    animationSpec = tween(durationMillis = SPLASH_SCREEN_ANIM_DURATION.toInt())
                ) togetherWith ExitTransition.None
            }
        ) {
            if(it) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.app_icon),
                        contentDescription = "App Icon",
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.inverseOnSurface,
                                shape = MaterialTheme.shapes.extraLarge
                            )
                            .padding(start = 16.dp, end = 4.dp, top = 8.dp, bottom = 8.dp)

                    )
                    Spacer(modifier = Modifier.fillMaxHeight(0.05F))
                    MaterialTheme.typography.headlineLarge.copy(fontSize = 30.sp)?.let {
                        Text(
                            text = context.getString(R.string.app_name),
                            style = it,
                        )
                    }
                }
            }
        }
    }
}