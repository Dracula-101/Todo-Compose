package io.github.dracula101.todo.ui.calendar_screen

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import io.github.dracula101.todo.R
import io.github.dracula101.todo.common.ui.PlaceholderText
import io.github.dracula101.todo.ui.theme.Red
import io.github.dracula101.todo.ui.todo_list.TodoCard
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedContentLambdaTargetStateParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    calendarViewModel: CalendarViewModel,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    //bind uistate with lifecyleowner
    val uiState = calendarViewModel.uiEvent.observeAsState()
    val todoListTask =
        calendarViewModel.todoList.observeAsState()
    val scrollState = rememberScrollState()
    val pagerState = rememberPagerState(
        initialPage = calendarViewModel.selectedWeekIndex.value,
        initialPageOffsetFraction = 0f,

        pageCount = {
            calendarViewModel.calendarDates.value.size
        }
    )
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.inverseOnSurface
                ),
        ) {
            Row(
                modifier = Modifier
                    .padding(
                        horizontal = 8.dp,
                        vertical = 4.dp
                    )
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowLeft,
                        contentDescription = null
                    )
                }
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = calendarViewModel.firstTaskDate.value?.month?.name.toString(),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = calendarViewModel.firstTaskDate.value?.year.toString()
                            .uppercase(),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowRight,
                        contentDescription = null
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
                beyondBoundsPageCount = 1,
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (calendarViewModel.calendarDates.value.isNotEmpty()) {
                        items(calendarViewModel.calendarDates.value[it].size) { date ->
                            calendarViewModel.currentSelectedDate.value?.let { selectedDate ->
                                CalendarItem(
                                    modifier = Modifier
                                        .shadow(
                                            elevation = 4.dp
                                        ),
                                    date = calendarViewModel.calendarDates.value[it][date].dayOfWeek.name.lowercase()
                                        .substring(0, 2)
                                        .replaceFirstChar {
                                            if (it.isLowerCase()) it.titlecase(
                                                Locale.ROOT
                                            ) else it.toString()
                                        },
                                    weekIndex = calendarViewModel.calendarDates.value[it][date].dayOfMonth,
                                    hasTodo = calendarViewModel.isTodoPresentInDate(
                                        calendarViewModel.calendarDates.value[it][date]
                                    ),
                                    isLastTime = calendarViewModel.isWeekEndDate(
                                        calendarViewModel.calendarDates.value[it][date]
                                    ),
                                    isChoosen = selectedDate.isEqual(
                                        calendarViewModel.calendarDates.value[it][date]
                                    ),
                                    onClick = {
                                        coroutineScope.launch {
                                            calendarViewModel.onDateChanged(
                                                calendarViewModel.calendarDates.value[it][date]
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        AnimatedVisibility(
            uiState.value is CalendarScreenEvent.IsLoadingSucess,
            label = "Task in Calendar",
        ) {
            when (uiState.value) {
                is CalendarScreenEvent.IsLoadingTodo -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            strokeWidth = 1.5.dp,
                        )
                    }
                }

                is CalendarScreenEvent.IsLoadingSucess -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .scrollable(scrollState, orientation = Orientation.Vertical),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start
                    ) {
                        if(todoListTask.value?.isNotEmpty() ==true) {
                            item {
                                Text(
                                    text = "Tasks on ${getSelectedDate( calendarViewModel.currentSelectedDate.value ?: LocalDate.now())}",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(
                                        start = 16.dp,
                                        top = 16.dp,
                                        end = 8.dp,
                                    )
                                )
                            }
                        }
                        items(todoListTask.value?.size ?:0 ) { todo ->
                            TodoCard(
                                modifier = Modifier
                                    .padding(
                                        horizontal = 8.dp,
                                        vertical = 4.dp
                                    )
                                    .fillMaxWidth(),
                                todo = todoListTask.value?.get(todo) ?: return@items,
                                onClick = {
                                },
                                hasRadioButton = false
                            )
                        }
                        if(todoListTask.value?.isEmpty()==true)
                            item{
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Spacer(modifier = Modifier.height(25.dp))
                                    Image(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.checklist),
                                        modifier = Modifier.size(250.dp),
                                        contentDescription = "Empty Todo",
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "No Tasks for selected date",
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Add a task for it to show up here",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                    }
                }

                else -> {
                    PlaceholderText(text = "${uiState.value}")
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getSelectedDate(currentSelectedDate: LocalDate): String {
    return "${currentSelectedDate.dayOfWeek?.name?.lowercase()?.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.ROOT
        ) else it.toString()
    }} ${currentSelectedDate.dayOfMonth} ${
        currentSelectedDate.month?.name?.lowercase()?.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.ROOT
            ) else it.toString()
        }?.substring(0,3)
    } ${currentSelectedDate.year}"

}

@Composable
fun CalendarItem(
    modifier: Modifier = Modifier,
    isLastTime: Boolean = false,
    date: String,
    weekIndex: Int,
    hasTodo: Boolean = false,
    isChoosen: Boolean = false,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .height(55.dp)
            .width(40.dp)
            .clip(MaterialTheme.shapes.small)
            .background(
                if (isChoosen) MaterialTheme.colorScheme.primary.copy(alpha = 0.9F) else MaterialTheme.colorScheme.surface,
            )
            .clickable {
                onClick()
            },
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f)
                .padding(top = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            MaterialTheme.typography.labelSmall?.copy(
                color = if (isLastTime)
                    Red
                else if (isChoosen)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSurface
            )?.let {
                Text(
                    text = date,
                    style = it
                )
            }
            MaterialTheme.typography.labelSmall?.copy(
                color = if (isLastTime)
                    Red
                else if (isChoosen)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSurface
            )?.let {
                Text(
                    text = weekIndex.toString(),
                    style = it
                )
            }
        }
        if (hasTodo) {
            Box(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .size(4.dp)
                    .clip(RoundedCornerShape(100))
                    .background(
                        if (isChoosen)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurface
                    )
            )

        }
    }
}
