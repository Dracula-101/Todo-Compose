package io.github.dracula101.todo.ui.calendar_screen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.dracula101.todo.data.models.Todo
import io.github.dracula101.todo.data.repository.TodoRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {

    var todoList = MutableLiveData<List<Todo>>(listOf())
        private set


    val firstTaskDate = mutableStateOf<LocalDate?>(null)
    val lastTaskDate = mutableStateOf(LocalDate.now())
    val calendarDates = mutableStateOf<List<List<LocalDate>>>(listOf())
    private val firstCalendarDate = mutableStateOf<LocalDate?>(null)
    private val lastCalendarDate = mutableStateOf<LocalDate?>(null)
    val currentSelectedDate = mutableStateOf<LocalDate?>(null)
    val selectedWeekIndex = mutableIntStateOf(0)

    val isTodoTypePending = mutableStateOf(true)
    private val _uiEvent = MutableLiveData<CalendarScreenEvent>()
    val uiEvent = _uiEvent

    init {
        onEvent(CalendarScreenEvent.IsLoadingTodo)
        Log.d("CalendarViewModel", "Called init from CalendarViewModel")
        viewModelScope.launch {

            findLastTodoDate()
        }
    }

    fun onEvent(event: CalendarScreenEvent) {
        Log.d("CalendarViewModel", "Called onEvent from ${event::class.simpleName}")
        when (event) {
            is CalendarScreenEvent.IsLoadingTodo -> {
                _uiEvent.value = CalendarScreenEvent.IsLoadingTodo
            }

            is CalendarScreenEvent.IsLoadingSucess -> {
                _uiEvent.value = CalendarScreenEvent.IsLoadingSucess
            }

            is CalendarScreenEvent.OnDateChanged -> {
                _uiEvent.value = CalendarScreenEvent.OnDateChanged(event.date)
            }

            is CalendarScreenEvent.OnTodoClick -> {
                _uiEvent.value = CalendarScreenEvent.OnTodoClick(event.todo)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun findLastTodoDate() {
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(Locale.US)
        repository.getUnCompletedTasksFlow().collect{
            todoList.value = it
            it.firstOrNull().let { firstTodo ->
                if(firstTodo == null){
                    return@let
                }
                val date = LocalDate.parse(firstTodo.date, dateFormatter)
                firstTaskDate.value = date
            }
            it.lastOrNull().let { lastTodo ->
                if(lastTodo == null){
                    return@let
                }
                val date = LocalDate.parse(lastTodo.date, dateFormatter)
                lastTaskDate.value = date
            }
            getCalendarDates()
            getSelectedDate()
            Log.d("CalendarViewModel", "Calendar Dates: ${calendarDates.value}")
            onEvent(CalendarScreenEvent.IsLoadingSucess)
        }
    }

    private fun getCalendarDates() {
        firstCalendarDate.value = firstTaskDate.value?.minusDays((firstTaskDate.value?.dayOfWeek?.value ?: 0).toLong())
        lastCalendarDate.value = lastTaskDate.value?.plusDays(7 - (lastTaskDate.value?.dayOfWeek?.value ?: 0).toLong())
        val calendarDates = mutableListOf<List<LocalDate>>()
        var date: LocalDate = firstCalendarDate.value ?: LocalDate.now().minusDays(LocalDate.now().dayOfWeek.value.toLong())
        while (date.isBefore(lastCalendarDate.value)) {
            val weekDates = mutableListOf<LocalDate>()
            for (i in 0..6) {
                weekDates.add(date)
                date = date.plusDays(1)
            }
            calendarDates.add(weekDates)
        }
        this.calendarDates.value = calendarDates
        if(firstTaskDate.value == null){
            firstTaskDate.value = LocalDate.now()
        }
    }

    fun getSelectedDate() {
        calendarDates.value.forEachIndexed { index, weekDates ->
            weekDates.forEach { date ->
                if (date == LocalDate.now()) {
                    selectedWeekIndex.value = index
                    currentSelectedDate.value = date
                }
            }
        }
        if(currentSelectedDate.value == null){
            currentSelectedDate.value = calendarDates.value[0][0]
        }
        Log.d("CalendarViewModel", "Selected Date: ${currentSelectedDate.value}")
    }

    fun isWeekEndDate(date: LocalDate): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date.dayOfWeek.value == 6 || date.dayOfWeek.value == 7
        } else {
            false
        }
    }

    fun isTodoPresentInDate(time: LocalDate): Boolean {
        val date: String = time.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        return repository.isTodoPresent(date)
    }

    suspend fun onDateChanged(date:LocalDate){
        currentSelectedDate.value = date
        onEvent(CalendarScreenEvent.IsLoadingTodo)
        repository.getTodosOnDate(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).collect{ uncompletedTodo ->
            todoList.value = uncompletedTodo
            delay(500)
            onEvent(CalendarScreenEvent.IsLoadingSucess)
        }
    }

}