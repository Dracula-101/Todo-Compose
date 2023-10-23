package io.github.dracula101.todo.ui.todo_list

import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import io.github.dracula101.todo.common.ui.EmptyTodoComponent
import io.github.dracula101.todo.common.ui.Searchbar
import io.github.dracula101.todo.data.models.Todo
import io.github.dracula101.todo.ui.theme.LightBlackAccent
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TodoListScreen(
    navController: NavController,
    viewModel: TodoListViewModel,
    snackbarHostState: SnackbarHostState,
    showAddTaskDialog: MutableState<Boolean>,
) {
    val todoFlow = viewModel.unCompletedTodos.observeAsState()
    val completedTodoFlow = viewModel.completedTodos.observeAsState()
    val scrollState = rememberScrollState()

    Surface {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (todoFlow.value?.isEmpty() == true && completedTodoFlow.value?.isEmpty() == true) {
                EmptyTodoComponent()
            }else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .scrollable(scrollState, orientation = Orientation.Vertical),
                ) {
                    item{
                        //Search Bar
                        if ((todoFlow.value?.isNotEmpty() == true) || (completedTodoFlow.value?.isNotEmpty() == true)) {
                            Searchbar(onSearchClick = {})
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                    }
                    // UnCompleted Todos
                    // Today Drop Down
                    if (todoFlow.value?.isNotEmpty() == true) {
                        item {
                            Box(
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.inverseOnSurface,
                                        shape = MaterialTheme.shapes.small
                                    )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = "Today",
                                        style = MaterialTheme.typography.titleMedium,
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = null,
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                    items(
                        items = todoFlow?.value?.sortedWith(TodoComparator) ?: emptyList() ,
                        key = {todo ->todo.hashCode()}
                    ){
                            todo ->
                        TodoCard(
                            todo = todo,
                            onEvent = viewModel::onEvent,
                            onClick = {
                                viewModel.onEvent(TodoCardEvent.OnTodoClick(todo))
                            },
                            onLongClick = {
                                viewModel.deletingTodo = todo
                                showAddTaskDialog.value = true
                            },
                            onTaskDone = {
                                viewModel.onEvent(TodoCardEvent.OnDoneChange(todo, it))
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .animateItemPlacement(
                                    animationSpec = tween(
                                        durationMillis = 500,
                                        easing = LinearOutSlowInEasing,
                                    )
                                )
                        )
                    }
                    // Spacer if no completed todos
                    if (completedTodoFlow.value?.isEmpty() == true) {
                        item(

                        ) {
                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    }
                    if ((todoFlow.value?.isNotEmpty() == true) && (completedTodoFlow.value?.isNotEmpty() == true)) {
                        item(

                        ) {
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp)
                            )
                        }
                    }
                    // Completed Drop Down
                    if (completedTodoFlow.value?.isNotEmpty() == true) {
                        item(
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.inverseOnSurface,
                                        shape = MaterialTheme.shapes.small
                                    )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = "Completed",
                                        style = MaterialTheme.typography.titleMedium,
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = null,
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        // Completed Todos
                        items(
                            items = completedTodoFlow?.value?.sortedWith(TodoComparator) ?: emptyList(),
                            key = { todo -> todo.id.toString() }
                        ) { todo ->
                            TodoCard(
                                todo = todo,
                                onEvent = viewModel::onEvent,
                                onClick = {
                                    viewModel.onEvent(TodoCardEvent.OnTodoClick(todo))
                                },
                                onLongClick = {
                                    viewModel.deletingTodo = todo
                                    showAddTaskDialog.value = true
                                },
                                onTaskDone = {
                                    viewModel.onEvent(TodoCardEvent.OnDoneChange(todo, it))
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .animateItemPlacement(
                                        animationSpec = tween(
                                            durationMillis = 500,
                                            easing = LinearOutSlowInEasing,
                                        )
                                    )
                            )
                        }
                        // Spacer if
                        item(
                        ) {
                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    }

                }
            }


        }


    }
}

@Composable
fun ShowDeleteTodoDialog(
    openDialog: MutableState<Boolean>,
    onEvent: (TodoCardEvent) -> Unit,
    todo: Todo
) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        onEvent(TodoCardEvent.OnDeleteTodoClick(todo))
                    }
                ) {
                    Text(text = "Yes")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text(text = "No")
                }
            },
            title = { Text(text = "Delete Task?") },
            text = { Text(text = "Are you sure you want to delete this todo\n${todo.title}?") }
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
private val TodoComparator = Comparator<Todo> { todo1, todo2 ->
    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.US)

    val dateTime1 = Calendar.getInstance()
    dateTime1.time = dateFormat.parse(todo1.date)!!
    dateTime1.set(Calendar.HOUR_OF_DAY, timeFormat.parse(todo1.time)!!.hours)
    dateTime1.set(Calendar.MINUTE, timeFormat.parse(todo1.time)!!.minutes)

    val dateTime2 = Calendar.getInstance()
    dateTime2.time = dateFormat.parse(todo2.date)!!
    dateTime2.set(Calendar.HOUR_OF_DAY, timeFormat.parse(todo2.time)!!.hours)
    dateTime2.set(Calendar.MINUTE, timeFormat.parse(todo2.time)!!.minutes)

    dateTime1.compareTo(dateTime2)
}