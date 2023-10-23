package io.github.dracula101.todo.ui.focus_screen

import android.app.Activity
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.Intent.CATEGORY_DEFAULT
import android.content.Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
import android.content.IntentSender
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat.startActivity
import io.github.dracula101.todo.MainActivity
import io.github.dracula101.todo.R
import io.github.dracula101.todo.common.ui.CircularProgressBar
import io.github.dracula101.todo.common.ui.DropDownButton
import io.github.dracula101.todo.ui.focus_screen.AppUsageInterval.Today
import io.github.dracula101.todo.ui.theme.LightBlackAccent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import java.util.SortedMap

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun FocusScreen(
    modifier: Modifier = Modifier,
    viewModel: FocusViewModel,
    context: Context
) {
    val socialAppUsageStats by viewModel.socialAppUsageStats.observeAsState()
    val uiEvent by viewModel.uiEvent.observeAsState()
    val coroutineScope = rememberCoroutineScope()
    val usageContract = remember { UsageAccessContract() }
    val activityLauncher :ActivityResultLauncher<Unit> = rememberLauncherForActivityResult(
        contract = usageContract
    ) { _ ->
        coroutineScope.launch {
            viewModel.updateUsageStats()
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiEvent) {
            is FocusScreenEvent.IsLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        strokeWidth = 1.5.dp
                    )
                }
            }

            is FocusScreenEvent.PermissionNotGranted -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Enable Usage Access",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(0.95f),
                        text = "Find the option in settings and give access",
                        textAlign = TextAlign.Center
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clip(shape = MaterialTheme.shapes.medium)
                            .background(
                                MaterialTheme.colorScheme.inverseOnSurface
                            )
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
//                        app icon
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .padding(4.dp)
                                .clip(shape = MaterialTheme.shapes.extraSmall)
                                .background(MaterialTheme.colorScheme.background)
                        )
                        Column(
                            modifier = Modifier
                                .padding(
                                    start = 8.dp,
                                    top = 4.dp,
                                ),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "UpTodo",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Disabled",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )

                        }
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    ElevatedButton(
                        onClick = {
                            activityLauncher.launch()
                        }
                    ) {
                        Text(text = "Open Settings")
                    }
                }
            }

            is FocusScreenEvent.IsLoaded -> {
                socialAppUsageStats?.toSortedMap(
                    compareByDescending<String> { socialAppUsageStats!![it]?.timeSpent }
                        .thenBy { socialAppUsageStats!![it]?.appName }
                ).let { apps: SortedMap<String, AppInfo>? ->
                    LazyColumn {
                        item {
                            DropDownButton(
                                dropdownItems = viewModel.timeList.map {
                                    "$it mins"
                                },
                                onItemClick = {
                                    viewModel.selectedTime.value = it.split(" ").first().toInt()
                                },
                                content = {
                                    Box{
                                        Row {
                                            Icon(
                                                imageVector = Icons.Outlined.KeyboardArrowDown,
                                                contentDescription = null,
                                            )
                                            Text(
                                                text = "${viewModel.selectedTime.value} min",
                                            )
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                        shape = MaterialTheme.shapes.small
                                    )
                                    .padding(8.dp)
                            )
                            CircularProgressBar(
                                value = viewModel.stopWatch.value,
                                maxValue = viewModel.selectedTime.value * 60,
                                middleText = viewModel.readableTime(viewModel.stopWatch.value),
                                fontSize = if(viewModel.stopWatch.value > 3600) 30F else 40F ,
                                modifier = Modifier.height(200.dp)
                            )
                            Spacer(modifier = Modifier.padding(8.dp))
                        }
                        item{
                            Text(
                                text = "While your focus mode is on, all of your notifications will be off",
                                textAlign = TextAlign.Center,
                            )
                            Spacer(modifier = Modifier.padding(8.dp))
                            ElevatedButton(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                onClick = {
                                    if(!viewModel.isFocusModeEnabled.value) {
                                        viewModel.isFocusModeEnabled.value = true
                                        viewModel.startFocusMode()
                                    }
                                    else {
                                        viewModel.isFocusModeEnabled.value = false
                                        viewModel.stopFocusMode()
                                    }
                                }
                            ) {
                                Text(
                                    "${if (!viewModel.isFocusModeEnabled.value) "Start" else "Stop"} Focus Mode"
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.BottomEnd
                            ){
                                DropDownButton(
                                    dropdownItems = viewModel.listOfInterval.map {
                                        it.displayName
                                    },
                                    onItemClick = {
                                        val intervalInfo = viewModel.listOfInterval.first { interval ->
                                            interval.displayName == it
                                        }
                                        coroutineScope.launch {
                                            viewModel.updateUsageStats(intervalInfo.interval)
                                        }
                                        viewModel.selectedUsageInterval.value = intervalInfo
                                    },
                                    content = {
                                        Box{
                                            Row {
                                                Icon(
                                                    imageVector = Icons.Outlined.KeyboardArrowDown,
                                                    contentDescription = null,
                                                )
                                                Text(
                                                    text = viewModel.selectedUsageInterval.value.displayName,
                                                )
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .border(
                                            width = 1.dp,
                                            color = MaterialTheme.colorScheme.onBackground.copy(
                                                alpha = 0.6f
                                            ),
                                            shape = MaterialTheme.shapes.small
                                        )
                                        .padding(8.dp)
                                )
                            }
                        }

                        if (apps != null) {
                            items(apps.size) { appInfo ->
                                AppCard(
                                    appInfo = apps.values.elementAt(appInfo),
                                    appUsageIntervalInfo = viewModel.selectedUsageInterval.value,
                                    onClick = {}
                                )
                            }
                        }
                    }
                }
            }

            else -> {}
        }
    }
}

@Composable
fun AppCard(appInfo: AppInfo, onClick: (packageName: String) -> Unit, iconSize: Int = 50, appUsageIntervalInfo: AppUsageIntervalInfo) {
    Box(
        modifier = Modifier
            .padding(
                horizontal = 4.dp,
                vertical = 8.dp
            )
            .fillMaxSize()
            .clip(shape = MaterialTheme.shapes.small)
            .background(
                MaterialTheme.colorScheme.inverseOnSurface
            )
            .clickable {
                onClick(appInfo.packageName)
            }
            .padding(
                horizontal = 8.dp,
                vertical = 4.dp
            ),
    ) {
        Row {
            appInfo.icon.let {
                if (it != null) {
                    AndroidView(
                        factory = { context ->
                            ImageView(context).apply {
                                setImageDrawable(appInfo.icon)
                            }
                        },
                        modifier = Modifier
                            .size(iconSize.dp)
                            .padding(4.dp)
                            .clip(shape = MaterialTheme.shapes.extraSmall)
                    )
                } else
                    Box(
                        modifier = Modifier
                            .size(iconSize.dp)
                            .padding(4.dp)
                            .clip(shape = MaterialTheme.shapes.extraSmall)
                            .background(
                                MaterialTheme.colorScheme.inverseOnSurface
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = null
                        )
                    }

            }
            Column(
                modifier = Modifier
                    .padding(
                        start = 8.dp,
                        top = 4.dp,
                    )
                    .fillMaxSize(0.75f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = appInfo.appName)
                Text(
                    text = "You have spent ${appInfo.readableTimeSpent} since ${appUsageIntervalInfo.displayName}",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Row(
                modifier = Modifier
                    .height(
                        iconSize.dp
                    )
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(2.dp)
                        .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                )
                Image(
                    painter = painterResource(id = R.drawable.info_circle),
                    contentDescription = null,
                )
            }
        }
    }


}


class UsageAccessContract : ActivityResultContract<Unit, Boolean>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            .setData(Uri.fromParts("package", context.packageName, null))
            .setFlags(FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_NO_HISTORY or FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        Log.d("UsageAccessContract", "parseResult: $resultCode, $intent")
        return resultCode == RESULT_OK
    }

}

//comparator for sorting apps based on time spent
val appInfoComparator = Comparator<AppInfo> { app1, app2 ->
    when {
        app1.timeSpent > app2.timeSpent -> -1
        app1.timeSpent < app2.timeSpent -> 1
        else -> 0
    }
}