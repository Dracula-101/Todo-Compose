package io.github.dracula101.todo.data.database
import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.dracula101.todo.data.models.Todo
import io.github.dracula101.todo.data.models.TodoDao

@Database(
    entities = [Todo::class],
    version = 1
)
abstract class TodoDatabase: RoomDatabase() {

    abstract val dao: TodoDao
}