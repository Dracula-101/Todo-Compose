package io.github.dracula101.todo.ui.todo_list

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.dracula101.todo.R
import io.github.dracula101.todo.data.models.Category
import io.github.dracula101.todo.data.models.Todo
import io.github.dracula101.todo.data.models.listOfCategory
import io.github.dracula101.todo.ui.theme.Grey
import io.github.dracula101.todo.ui.theme.LightBlack
import io.github.dracula101.todo.ui.theme.LightBlackAccent
import io.github.dracula101.todo.ui.theme.LightRed
import io.github.dracula101.todo.ui.theme.Red
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodoCard(
    todo: Todo,
    modifier: Modifier = Modifier.padding(16.dp),
    onEvent: (TodoCardEvent) -> Unit = {},
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    onTaskDone:(Boolean) -> Unit = {},
    hasRadioButton : Boolean = true
) {
    val category = listOfCategory.firstOrNull() { it.id == todo.categoryId }
    val todayDate = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(java.time.LocalDate.now())
    val todayTime = DateTimeFormatter.ofPattern("hh:mm a").format(java.time.LocalTime.now())
    val formattedDate = DateTimeFormatter.ofPattern("dd MMM").format(java.time.LocalDate.parse(todo.date, DateTimeFormatter.ofPattern("dd/MM/yyyy")))
    val formattedTime = if (todo.time == "00:00 AM") "Midnight"
                        else if(todo.time == "00:00 PM") "Noon"
                        else todo.time
    val isToday = todo.date == todayDate
    val isYesterday = todo.date == DateTimeFormatter.ofPattern("dd/MM/yyyy").format(java.time.LocalDate.now().minusDays(1))
    val isPast = todo.date < todayDate
    val fraction = if(hasRadioButton) 0.5f else 0.55f
    return Box(
        modifier = Modifier
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
            .clip(MaterialTheme.shapes.small)
            .background(                            MaterialTheme.colorScheme.inverseOnSurface
            )
            .combinedClickable(
                onLongClick = onLongClick,
                onClick = onClick
            )
            .alpha(if (todo.isDone) 0.7f else 1f),
        contentAlignment = Alignment.BottomEnd
    ) {
        Row(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp,
                    vertical = 4.dp
                )
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if(hasRadioButton) {
                RadioButton(
                    modifier = Modifier.size(15.dp),
                    selected = todo.isDone,
                    onClick = {
                        onTaskDone(!todo.isDone)
                    }
                )
            }
            Column(
                modifier = Modifier
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
                    .fillMaxWidth()
            ) {
                Text(
                    text = todo.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if(todo.isDone || !hasRadioButton){
                    todo.description?.let {
                        MaterialTheme.typography.labelSmall?.copy(
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )?.let { it1 ->
                            Text(
                                text = it,
                                style = it1,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .fillMaxWidth(fraction)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                todo.isDone.let {isDone->
                    if (isDone) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(fraction),
                            text = "Completed on $formattedDate",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }else {
                        when {
                            isToday -> {
                                Text(modifier = Modifier
                                    .fillMaxWidth(fraction),
                                    text = "Today at $formattedTime",
                                    style = MaterialTheme.typography.labelSmall,                            color = MaterialTheme.colorScheme.onSecondaryContainer

                                )
                            }
                            isYesterday -> {
                                Text(modifier = Modifier
                                    .fillMaxWidth(fraction),
                                    text = "Missed Yesterday at $formattedTime",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            isPast -> {
                                Text(modifier = Modifier
                                    .fillMaxWidth(fraction),
                                    text = "Missed on $formattedDate",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            else -> {
                                Text(modifier = Modifier
                                    .fillMaxWidth(fraction),
                                    text = "$formattedDate at $formattedTime",
                                    style = MaterialTheme.typography.labelSmall,                            color = MaterialTheme.colorScheme.onSecondaryContainer

                                )
                            }
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .padding(
                    bottom = 4.dp,
                    end = 4.dp
                )
                .matchParentSize(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom,
        ) {
            category?.let {
                CategoryPreview(category = it)
            }
            Spacer(modifier = Modifier.size(4.dp))
            Row{
                todo.priority?.let {priority->
                    PriorityPreview(priority = priority)
                }
            }
        }
    }
}
@Composable
fun CategoryPreview(category: Category){
    Box(
        modifier = Modifier
            .padding(2.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(category.color)
    ) {
        Row(
            Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ){
            Image(
                painter = painterResource(id = category.icon),
                contentDescription = category.name,
                modifier = Modifier
                    .size(20.dp)
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = category.name,
                style = MaterialTheme.typography.labelSmall,
                color = LightBlack,
            )
        }
    }
}

@Composable
fun PriorityPreview(priority: Int){
    Box(
        modifier = Modifier
            .padding(2.dp)
            .clip(RoundedCornerShape(4.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(4.dp)
    ) {
        Row(
            Modifier.padding(
                horizontal = 4.dp,
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = R.drawable.flag),
                contentDescription = "Priority",
                modifier = Modifier
                    .size(20.dp)
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = priority.toString(),
                style = MaterialTheme.typography.labelSmall,
            )
        }
    }
}
//
//@Preview()
//@Composable
//fun TodoCardPreview() {
//    TodoCard(
//        todo = Todo(
//            title = "This is the a very large title",
//            description = "This is the a very very large description",
//            isDone = false,
//            date = "12/12/2021",
//            time = "12:00 AM",
//            priority = 1,
//            categoryId = 1 ,
//        ),
//        onEvent = {},
//        onClick = {},
//        onLongClick = {},
//        onTaskDone = {}
//
//    )
//}

