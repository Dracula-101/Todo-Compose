package io.github.dracula101.todo.ui.intro

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : ViewModel(){

    fun checkIfFirstTime(): Boolean {
        return sharedPreferences.getBoolean(IS_FIRST_TIME, true)
    }

    fun setFirstTime() {
        sharedPreferences.edit().putBoolean(IS_FIRST_TIME, false).apply()
    }

    companion object {
        private const val IS_FIRST_TIME = "is_first_time"
    }
}