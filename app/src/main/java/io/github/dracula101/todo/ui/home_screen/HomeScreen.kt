package io.github.dracula101.todo.ui.home_screen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import io.github.dracula101.todo.common.ui.AppBar
import io.github.dracula101.todo.common.ui.BottomNavBar
import io.github.dracula101.todo.common.ui.CategoryDialog
import io.github.dracula101.todo.common.ui.CircleAvatar
import io.github.dracula101.todo.common.ui.CustomDatePicker
import io.github.dracula101.todo.common.ui.CustomTimePicker
import io.github.dracula101.todo.common.ui.FAB
import io.github.dracula101.todo.common.ui.PriorityDialog
import io.github.dracula101.todo.common.ui.utils.UiEvent
import io.github.dracula101.todo.ui.add_edit_todo.AddTodoBottomSheet
import io.github.dracula101.todo.ui.add_edit_todo.AddTodoEvent
import io.github.dracula101.todo.ui.calendar_screen.CalendarScreen
import io.github.dracula101.todo.ui.calendar_screen.CalendarViewModel
import io.github.dracula101.todo.ui.focus_screen.FocusScreen
import io.github.dracula101.todo.ui.focus_screen.FocusViewModel
import io.github.dracula101.todo.ui.login_register.AuthViewModel
import io.github.dracula101.todo.ui.profile.ProfileScreen
import io.github.dracula101.todo.ui.profile.ProfileViewModel
import io.github.dracula101.todo.ui.todo_list.ShowDeleteTodoDialog
import io.github.dracula101.todo.ui.todo_list.TodoCardEvent
import io.github.dracula101.todo.ui.todo_list.TodoListScreen
import io.github.dracula101.todo.ui.todo_list.TodoListViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel = hiltViewModel(),
    todoListViewModel: TodoListViewModel = hiltViewModel(),
    calendarViewModel: CalendarViewModel = hiltViewModel(),
    focusViewModel: FocusViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
    onLogout: () -> Unit = {}
) {
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val openDialog = remember {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState(true)
    val isSheetOpen = remember {
        mutableStateOf(false)
    }
    var showDatePicker by remember {
        mutableStateOf(false)
    }
    var showTimePicker by remember {
        mutableStateOf(false)
    }
    var showPriorityDialog by remember {
        mutableStateOf(false)
    }
    var showCategoryDialog by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(todoListViewModel.uiEvent) {
        todoListViewModel.uiEvent.collect { event ->
            Log.d("TodoListScreen", "TodoListScreen: $event")
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    val result = snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action ?: "",
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        todoListViewModel.onEvent(TodoCardEvent.OnUndoDeleteClick)
                    }

                }

                is UiEvent.ShowBottomSheet -> {
                    sheetState.show()
                    isSheetOpen.value = true
                }
                else -> Unit
            }
        }
    }
    Scaffold(
        topBar = {
            AppBar(
                title = "Todo List",
                actions = {
                    CircleAvatar(
                        onClick = {
                            homeViewModel.onChangeTab(3)
                        }
                    )
                },
                titleChangeFlow = homeViewModel.selectedTab,
            )
        },
        floatingActionButton = {
            FAB(
                onClick = {
                    todoListViewModel.onEvent(TodoCardEvent.OnAddTodoClick)
                }
            )
        },
        snackbarHost = {
            snackbarHostState
        },
        bottomBar = {
            BottomNavBar(
                selectedItem = homeViewModel.selectedTab,
                onTabChanged = {
                    homeViewModel.onChangeTab(it)
                },
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ){
            homeViewModel.selectedTab.value.let {
                when(it){
                    0 -> TodoListScreen(
                            viewModel =todoListViewModel ,
                            snackbarHostState = snackbarHostState,
                            navController = navController,
                            showAddTaskDialog = openDialog,
                        )
                    1 -> CalendarScreen(
                            calendarViewModel =calendarViewModel
                        )
                    2 -> FocusScreen(
                        viewModel =focusViewModel,
                        context = navController.context
                    )
                    3 -> ProfileScreen(
                        profileViewModel = profileViewModel,
                        onLogout = onLogout
                    )
                }
            }
        }

        SnackbarHost(
            modifier = Modifier.padding(
                bottom = FloatingActionButtonDefaults.LargeIconSize * 2

            ),
            hostState = snackbarHostState,
            snackbar = { snackbarData: SnackbarData ->
                Snackbar(
                    snackbarData = snackbarData,
                )
            }
        )
        if (isSheetOpen.value) {
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = {
                    isSheetOpen.value = false
                }
            ) {
                AddTodoBottomSheet(
                    viewModel = todoListViewModel,
                    onSaveTask = {
//                        show calendar here
                        showDatePicker = true
                        isSheetOpen.value = false
                    }
                )
            }
        }
        if (openDialog.value) {
            ShowDeleteTodoDialog(
                openDialog = openDialog,
                onEvent = todoListViewModel::onEvent,
                todo = todoListViewModel.deletingTodo!!
            )
        }
        if (showDatePicker) {
            CustomDatePicker(
                onDateSelected = {
                    todoListViewModel.date = it
                    showTimePicker = true
                },
                onDismiss = {
                    showDatePicker = false
                }
            )
        }
        if (showTimePicker) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                CustomTimePicker(
                    onTimeSelected = {
                        todoListViewModel.time = it
                        showPriorityDialog = true
                    },
                    onDismiss = {
                        showTimePicker = false
                    }
                )
            }
        }
        if (showPriorityDialog) {
            PriorityDialog(
                onPrioritySelected = {
                    todoListViewModel.priority = it
                    showCategoryDialog = true
                },
                onDismiss = {
                    showPriorityDialog = false
                }
            )
        }
        if (showCategoryDialog) {
            CategoryDialog(
                onCategorySaved = {
                    todoListViewModel.category = it
                    todoListViewModel.onAddEvent(AddTodoEvent.OnSaveTodoClick)
                },
                onDismiss = {
                    showCategoryDialog = false
                },
            )
        }
    }
}