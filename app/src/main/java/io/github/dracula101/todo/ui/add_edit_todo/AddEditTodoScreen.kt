package io.github.dracula101.todo.ui.add_edit_todo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import io.github.dracula101.todo.R
import io.github.dracula101.todo.ui.todo_list.TodoListViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AddTodoBottomSheet(
    onSaveTask : () -> Unit,
    viewModel: TodoListViewModel,
) {

    val focusRequester = remember { FocusRequester() }
    val todoNameHasError = remember { mutableStateOf(false) }
    val todoDescriptionHasError = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(key1 = Unit){
        focusRequester.requestFocus()
        viewModel.resetTodo()
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
            )
    ) {
        Text(
            text = "Add Task",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = viewModel.title,
            onValueChange = {
                viewModel.onAddEvent(AddTodoEvent.OnTitleChange(it))
            },
            placeholder = {
                Text(text = if(todoNameHasError.value) "Title can't be empty" else "Title")
            },
            isError = todoNameHasError.value,
            label = {
                Text(text = "Title")
            },
            shape = MaterialTheme.shapes.medium,
            maxLines = 1,
            modifier = Modifier
                .focusRequester(focusRequester)
                .fillMaxWidth(),
            prefix = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Title",
                    modifier = Modifier.padding(end = 8.dp)
                )
            },
            keyboardActions = KeyboardActions(
                onNext = {
                    if (viewModel.title.isEmpty()) {
                        todoNameHasError.value = true
                    } else {
                        todoNameHasError.value = false
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                },

            ),
            keyboardOptions = KeyboardOptions(
                imeAction =  ImeAction.Next,
                autoCorrect = true,
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Text,
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = viewModel.description,
            onValueChange = {
                viewModel.onAddEvent( AddTodoEvent.OnDescriptionChange(it))
            },
            label = {
                Text(text = "Description")
            },
            placeholder = {
                Text(text = if(todoDescriptionHasError.value) "Description can't be empty" else "Description")
            },
            isError = todoDescriptionHasError.value,
            shape = MaterialTheme.shapes.medium,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth(),
            prefix = {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Description",
                    modifier = Modifier.padding(end = 8.dp)
                )
            },
            keyboardActions = KeyboardActions(
                onDone = {
                    if (viewModel.description.isEmpty()) {
                        todoDescriptionHasError.value = true
                    } else {
                        todoDescriptionHasError.value = false
                        focusManager.clearFocus()
                    }
                }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction =  ImeAction.Done,
                autoCorrect = true,
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Text,
            )
        )
        Row(
            modifier = Modifier.padding(
                vertical = 16.dp
            ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .weight(0.7f),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                IconButton(
                    modifier = Modifier
                        .size(24.dp),
                    onClick = {
                    }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.timer),
                        contentDescription = "Save"
                    )
                }
                IconButton(
                    modifier = Modifier
                        .size(24.dp),
                    onClick = {
                    }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.tag),
                        contentDescription = "Save"
                    )
                }
                IconButton(
                    modifier = Modifier
                        .size(24.dp),
                    onClick = {}
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.flag),
                        contentDescription = "Save"
                    )
                }
            }
//            icon with text button
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .padding(
                        horizontal =8.dp
                    )
                    .clickable {
                        todoNameHasError.value = viewModel.title.isEmpty()
                        todoDescriptionHasError.value = viewModel.description.isEmpty()
                        if (!todoNameHasError.value && !todoDescriptionHasError.value) {
                            coroutineScope.launch {
                                onSaveTask.invoke()
                                keyboardController?.hide()
                            }

                        }
                    }
            ) {
                Row {
                    Text(text = "Save")
                    Spacer(modifier = Modifier.width(8.dp) )
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.send),
                        contentDescription = "Save"
                    )
                }

            }
        }
    }
}
