package io.github.dracula101.todo.data.repository

import androidx.lifecycle.LiveData
import io.github.dracula101.todo.data.models.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {

    suspend fun insertTodo(todo: Todo)

    suspend fun deleteTodo(todo: Todo)

    suspend fun getTodoById(id: Int): Todo?

    fun getUnCompletedTodos(): LiveData<List<Todo>>

    fun getCompletedTodos() : LiveData<List<Todo>>

    fun getUnCompletedTasksFlow(): Flow<List<Todo>>

    fun getCompletedTasksFlow(): Flow<List<Todo>>

    fun isTodoPresent(date: String) : Boolean

    fun getTodosOnDate(date: String) : Flow<List<Todo>>

    fun getTodosUptoDate(date: String) : Flow<List<Todo>>

    fun getCompletedTask() : Flow<Int>

    fun getUnCompletedTask() : Flow<Int>
}