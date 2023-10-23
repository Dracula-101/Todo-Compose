package io.github.dracula101.todo.di

import android.app.Application
import android.content.ContentProviderClient
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.room.Room
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.dracula101.todo.TodoApp
import io.github.dracula101.todo.data.database.TodoDatabase
import io.github.dracula101.todo.data.repository.AuthRepository
import io.github.dracula101.todo.data.repository.AuthRepositoryImpl
import io.github.dracula101.todo.data.repository.TodoRepository
import io.github.dracula101.todo.data.repository.TodoRepositoryImpl
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTodoDatabase(app: Application): TodoDatabase {
        return Room
            .databaseBuilder(
                app,
                TodoDatabase::class.java,
            "todo_db")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun provideTodoRepository(db: TodoDatabase): TodoRepository {
        return TodoRepositoryImpl(db.dao)
    }


    @Provides
    @Singleton
    fun providesAppContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun providesPackageManager(@ApplicationContext context: Context): PackageManager {
        return context.packageManager
    }

    @Provides
    @Singleton
    fun providesSharedPrefs(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("todo_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providesFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun providesOneTapClient(@ApplicationContext context: Context) : SignInClient {
        return Identity.getSignInClient(context)
    }

    @Provides
    @Singleton
    fun providesAuthRepository(@ApplicationContext context: Context): AuthRepository {
        return AuthRepositoryImpl(providesFirebaseAuth(), providesOneTapClient(context), context)
    }

}