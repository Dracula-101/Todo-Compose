package io.github.dracula101.todo.data.models

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTodo(todo: Todo)

    @Delete(entity = Todo::class)
    fun deleteTodo(todo: Todo)

    @Query("SELECT * FROM todo WHERE id = :id")
    fun getTodoById(id: Int): Todo?

    @Query("SELECT * FROM todo WHERE isDone = 0 ORDER BY priority ASC")
    fun getUncompletedTodos(): LiveData<List<Todo>>

    @Query("SELECT * FROM todo WHERE isDone = 1 ORDER BY priority ASC")
    fun getCompletedTodos(): LiveData<List<Todo>>

    @Query("SELECT * FROM todo WHERE isDone = 0 ORDER BY priority ASC")
    fun getCompletedTasksFlow(): Flow<List<Todo>>

    @Query("SELECT * FROM todo WHERE isDone = 1 ORDER BY priority ASC")
    fun getUncompletedTasksFlow(): Flow<List<Todo>>

    @Query("SELECT EXISTS(SELECT * FROM todo WHERE date = :date)")
    fun isTodoPresent(date: String): Boolean

    @Query("SELECT * FROM todo WHERE date = :date")
    fun getTodosOnDate(date: String): Flow<List<Todo>>

    @Query("SELECT * FROM todo WHERE date <= :date")
    fun getTodosUptoDate(date: String): Flow<List<Todo>>

    @Query("SELECT COUNT(*) FROM todo WHERE isDone = 1")
    fun getCompletedTask() : Flow<Int>

    @Query("SELECT COUNT(*) FROM todo WHERE isDone = 0")
    fun getUnCompletedTask() : Flow<Int>

}