package io.github.dracula101.todo.data.repository

import androidx.lifecycle.LiveData
import io.github.dracula101.todo.data.models.Todo
import io.github.dracula101.todo.data.models.TodoDao
import kotlinx.coroutines.flow.Flow


class TodoRepositoryImpl(
    private val dao: TodoDao,
): TodoRepository {

    override suspend fun insertTodo(todo: Todo) {
        dao.insertTodo(todo)
    }

    override suspend fun deleteTodo(todo: Todo) {
        dao.deleteTodo(todo)
    }

    override suspend fun getTodoById(id: Int): Todo? {
        return dao.getTodoById(id)
    }

    override fun getUnCompletedTodos(): LiveData<List<Todo>> {
        return dao.getUncompletedTodos()
    }

    override fun getCompletedTodos() : LiveData<List<Todo>> {
        return dao.getCompletedTodos()
    }

    override fun getUnCompletedTasksFlow(): Flow<List<Todo>> {
        return dao.getUncompletedTasksFlow()
    }

    override fun getCompletedTasksFlow(): Flow<List<Todo>> {
        return dao.getCompletedTasksFlow()
    }

    override fun isTodoPresent(date: String) : Boolean {
        return dao.isTodoPresent(date)
    }

    override fun getTodosOnDate(date: String) : Flow<List<Todo>> {
        return dao.getTodosOnDate(date)
    }

    override fun getTodosUptoDate(date: String) : Flow<List<Todo>> {
        return dao.getTodosUptoDate(date)
    }

    override fun getCompletedTask(): Flow<Int> {
        return dao.getCompletedTask()
    }

    override fun getUnCompletedTask(): Flow<Int> {
        return dao.getUnCompletedTask()
    }
}