package io.github.dracula101.todo.ui.intro

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.github.dracula101.todo.R
import io.github.dracula101.todo.common.ui.utils.Routes
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun IntroScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,

        pageCount = { 3 }
    )
    val showExitIntroPagess = remember {
        mutableStateOf(true)
    }
    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        AnimatedContent(
            targetState = showExitIntroPagess.value,
            label = "Exit Intro Pages",
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            }
        ) {
            if(!it){
                Column {
                    TextButton(
                        modifier = modifier
                            .padding(horizontal = 8.dp),
                        onClick = { /*TODO*/ }
                    ) {
                        Text(
                            text = "Skip",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                            )
                        )
                    }
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.8F)
                            .padding(16.dp),
                        userScrollEnabled = false
                    ) { it ->
                        when (it) {
                            0 -> IntroPage(
                                modifier = modifier,
                                resourceId = R.drawable.intro_1,
                                titleString = "Manage your tasks",
                                descriptionString = "You can easily manage all of your daily tasks in UpTodo for free"
                            )

                            1 -> IntroPage(
                                modifier = modifier,
                                resourceId = R.drawable.intro_2,
                                titleString = "Create daily routine",
                                descriptionString = "In Uptodo you can create your personalized routine to stay productive."
                            )

                            2 -> IntroPage(
                                modifier = modifier,
                                resourceId = R.drawable.intro_3,
                                titleString = "Organize your tasks",
                                descriptionString = "You can organize your daily tasks by adding your tasks into separate categories"
                            )

                            else -> Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        ElevatedButton(
                            onClick = {
                                coroutineScope.launch {
                                    if (pagerState.currentPage > 0) {
                                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                    }
                                }
                            }
                        ) {
                            Text(text = "Back")
                        }
                        if(pagerState.currentPage == pagerState.pageCount - 1) {
                            FilledTonalButton(
                                onClick = {
                                    showExitIntroPagess.value = true
                                }
                            ) {
                                Text(text = "Get Started")
                            }
                        } else {
                            ElevatedButton(
                                onClick = {
                                    coroutineScope.launch {
                                        if (pagerState.currentPage < 2) {
                                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                        }
                                    }
                                }
                            ) {
                                Text(text = "Next")
                            }
                        }
                    }
                }
            }
            else Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.TopStart
            ) {
                Column(
                    modifier = modifier
                        .fillMaxHeight()
                        .padding(16.dp),
                    horizontalAlignment =  Alignment.Start,
                    verticalArrangement = Arrangement.SpaceBetween,
                ){
                    Column(

                    ){
                        IconButton(
                            onClick = {
                            },
                        ) {
                            Icon(
                                Icons.Rounded.KeyboardArrowLeft,
                                contentDescription = "Back",
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                        Text(
                            text = "Welcome to UpTodo",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontSize = 32.sp
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Please login to your account or create new account to continue",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 16.sp
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 32.dp
                                )
                        )
                    }
                    Column{
                        Button(
                            modifier = modifier.fillMaxWidth(),
                            onClick = {
                                navController.navigate(Routes.AUTH_SCREEN)
                            }
                        ){
                            Text(text = "Login")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedButton(
                            modifier = modifier.fillMaxWidth(),
                            onClick = {}
                        ){
                            Text(text = "Create New Account")
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                    }

                }

            }
        }
    }
}

@Composable
fun IntroPage(
    resourceId: Int,
    titleString: String,
    descriptionString: String,
    modifier: Modifier
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = "Intro Image",
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                    shape = MaterialTheme.shapes.large
                )
                .padding(16.dp)

        )
        Text(
            text = titleString,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontSize = 32.sp
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(16.dp)
        )
        Text(
            text = descriptionString,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(16.dp)
        )
    }
}
