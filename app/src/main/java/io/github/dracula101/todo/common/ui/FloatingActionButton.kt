package io.github.dracula101.todo.common.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun FAB(
    icon: ImageVector = Icons.Filled.Add,
    onClick: () -> Unit = {},
    shape: Shape = RoundedCornerShape(percent = 25)
){
    return FloatingActionButton(
        onClick = onClick,
        shape = shape,
        containerColor = MaterialTheme.colorScheme.primary,
    ){
        Icon(
            imageVector = icon,
            contentDescription = null
        )
    }
}