package io.github.dracula101.todo.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun CircleAvatar(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
){
    return Box(
        modifier = Modifier
            .padding(
                vertical = 8.dp,
            )
            .size(40.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onPrimary)
            .clickable { onClick()  },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Person,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}