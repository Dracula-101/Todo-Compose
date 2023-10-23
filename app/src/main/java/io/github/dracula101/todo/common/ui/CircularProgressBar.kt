package io.github.dracula101.todo.common.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData

@Composable
fun CircularProgressBar(
    value: Int,
    maxValue: Int,
    middleText: String,
    fontSize: Float = MaterialTheme.typography.titleLarge.fontSize.value,
    color: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Float = 10f,
    modifier: Modifier = Modifier,

) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = modifier
                .fillMaxSize(0.6f),
            progress =1F,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
            strokeCap = StrokeCap.Round,
            strokeWidth = strokeWidth.dp,
        )
        CircularProgressIndicator(
            modifier = modifier
                .fillMaxSize(0.6f),
            progress = value.toFloat() / maxValue,
            color = color,
            strokeCap = StrokeCap.Round,
            strokeWidth = strokeWidth.dp,
        )
        Box(modifier =modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
            Text(
                text = middleText,
                fontSize = fontSize.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}