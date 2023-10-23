package io.github.dracula101.todo.common.ui

import android.inputmethodservice.Keyboard
import android.widget.GridView
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import io.github.dracula101.todo.R
import io.github.dracula101.todo.ui.theme.Grey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriorityDialog(
    onPrioritySelected: (Int) -> Unit,
    onDismiss: () -> Unit
){
    var selectedPriority by rememberSaveable {
        mutableStateOf(1)
    }
    AlertDialog(
        modifier = Modifier
            .padding(16.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.medium
            ),
        properties = DialogProperties(
            securePolicy = SecureFlagPolicy.SecureOn,
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        ),
        onDismissRequest = {}

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ){
            Text(
                text = "Select Priority",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
            Divider()
            LazyVerticalGrid(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                columns = GridCells.Fixed(3)
            ) {
//                1 to 10
                itemsIndexed(listOf(1,2,3,4,5,6,7,8,9,10)) { index, item ->
                    PriorityItem(
                        priority = item,
                        onPrioritySelected = {
                            selectedPriority = it
                        },
                        selected = selectedPriority == item
                    )
                }
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ){
                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text(text = "Cancel")
                }
                TextButton(
                    onClick = {
                        onPrioritySelected(selectedPriority)
                        onDismiss()
                    }
                ) {
                    Text(text = "Save")
                }
            }
        }
    }
}

@Composable
fun PriorityItem(
    priority: Int,
    onPrioritySelected: (Int) -> Unit,
    selected : Boolean = false
){
    Box(
        modifier = Modifier
            .padding(
                horizontal = 4.dp,
                vertical = 8.dp
            )
            .background(
                color = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.7F) else MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.small
            )
            .border(
                width = 1.dp,
                color = Grey,
                shape = MaterialTheme.shapes.small
            )
            .padding(
                vertical = 8.dp
            )
            .clickable {
                onPrioritySelected(priority)
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(35.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.flag),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = priority.toString(),
            )
        }
    }
}
