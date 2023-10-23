package io.github.dracula101.todo.ui.todo_list

import android.os.Build
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.dracula101.todo.common.ui.utils.UiEvent
import io.github.dracula101.todo.data.models.Todo
import io.github.dracula101.todo.data.repository.TodoRepository
import io.github.dracula101.todo.ui.add_edit_todo.AddTodoEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel
@Inject constructor(
    private val repository: TodoRepository
): ViewModel()
{
    var unCompletedTodos = repository.getUnCompletedTodos()
        private set

    var completedTodos = repository.getCompletedTodos()
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var deletingTodo: Todo? = null
    private var deletedTodo: Todo? = null

    var todo by mutableStateOf<Todo?>(null)
        private set

    var title by mutableStateOf("Title")
        private set

    var description by mutableStateOf("Description")
        private set

    var date by mutableStateOf("")

    var time by mutableStateOf("")

    var priority by mutableStateOf(1)

    var category by mutableStateOf(-1)

    private val _uiAddEditEvent =  Channel<UiEvent>()
    val addEditUiEvent = _uiAddEditEvent.receiveAsFlow()

    init {
        Log.d("TodoListViewModel","Called init from TodoListViewModel")

    }

    fun onEvent(event : TodoCardEvent){
        when (event) {
            is TodoCardEvent.OnTodoClick -> {
//                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO + "?todoId=${event.todo.id}"))
            }
            is TodoCardEvent.OnAddTodoClick -> {
                sendUiEvent(UiEvent.ShowBottomSheet)
            }
            is TodoCardEvent.OnDeleteTodoClick -> {
                viewModelScope.launch {
                    repository.deleteTodo(
                        event.todo
                    )
                    deletingTodo = event.todo
                    sendUiEvent(UiEvent.ShowSnackbar("Todo Deleted", "Undo"))
                }
            }
            is TodoCardEvent.OnDoneChange -> {
                viewModelScope.launch {
                    repository.insertTodo(
                        event.todo.copy(isDone = event.isDone)
                    )
                }
            }
            is TodoCardEvent.OnUndoDeleteClick -> {
                deletedTodo?.let {
                    viewModelScope.launch {
                        repository.insertTodo(
                            it
                        )
                    }
                }
            }
        }
    }

    fun orderTodosByDateAndTime() {
        viewModelScope.launch {
            unCompletedTodos = repository
                .getUnCompletedTodos()
            completedTodos = repository
                .getCompletedTodos()
        }
    }

    fun refreshTodos() {
        viewModelScope.launch {
            unCompletedTodos = repository.getUnCompletedTodos()
            completedTodos = repository.getCompletedTodos()
        }
    }

    fun resetTodo(){
        todo = null
        title = ""
        description = ""
    }

    fun onAddEvent(event: AddTodoEvent) {
        when(event) {
            is AddTodoEvent.OnTitleChange -> {
                title = event.title
            }
            is AddTodoEvent.OnDescriptionChange -> {
                description = event.description
            }
            is AddTodoEvent.OnSaveTodoClick -> {
                viewModelScope.launch {
                    repository.insertTodo(
                        Todo(
                            title = title,
                            description = description,
                            isDone = todo?.isDone ?: false,
                            date = date,
                            time = time,
                            priority = priority,
                            categoryId = category,
                            id = todo?.id
                        )
                    )
                }
            }
        }
    }


    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

}