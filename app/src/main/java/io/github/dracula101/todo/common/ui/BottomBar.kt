package io.github.dracula101.todo.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import io.github.dracula101.todo.R
import kotlinx.coroutines.flow.Flow

val bottomBarItems = listOf(
    "Home",
    "Calendar",
    "Focus",
    "Profile"
)
@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    iconSize: Dp = 24.dp,
    onTabChanged : (Int) -> Unit = {},
    selectedItem : MutableIntState,
) {
    NavigationBar (
        modifier = modifier,
    ){
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = if(selectedItem.value == 0) R.drawable.home_filled else R.drawable.home),
                    contentDescription = "Home",
                    modifier = Modifier.size(iconSize)
                )
            },
            label = {
                Text(text = bottomBarItems[0])
            },
            selected = selectedItem.value == 0,
            onClick = {
                if(selectedItem.value !=0) {
                    onTabChanged(0)
                }
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = if(selectedItem.value == 1) R.drawable.calendar_filled else R.drawable.calendar),
                    contentDescription = "Calendar",
                    modifier = Modifier.size(iconSize)
                )
            },
            label = {
                Text(text = bottomBarItems[1])
            },
            selected = selectedItem.value == 1,
            onClick = {
               if(selectedItem.value!=1){
                   onTabChanged(1)
               }
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = if(selectedItem.value == 2) R.drawable.clock_filled else R.drawable.clock),
                    contentDescription = "Focus",
                            modifier = Modifier.size(iconSize)
                )
            },
            label = {
                Text(text = bottomBarItems[2])
            },
            selected = selectedItem.value == 2,
            onClick = {
                if(selectedItem.value!=2){
                    onTabChanged(2)
                }
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.user),
                    contentDescription = "Profile",
                    modifier = Modifier.size(iconSize)
                )
            },
            label = {
                Text(text = bottomBarItems[3])
            },
            selected = selectedItem.value == 3,
            onClick = {
                if(selectedItem.value!=3){
                    onTabChanged(3)
                }
            }
        )

    }
}