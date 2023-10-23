package io.github.dracula101.todo.ui.focus_screen

import android.R
import android.app.AppOpsManager
import android.app.AppOpsManager.OPSTR_GET_USAGE_STATS
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Process.myUid
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.AppOpsManagerCompat
import androidx.core.app.AppOpsManagerCompat.MODE_ALLOWED
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject


@RequiresApi(Build.VERSION_CODES.N)
@HiltViewModel
class FocusViewModel @Inject constructor(
    private val appContext : Context,
    private val packageManager: PackageManager
): ViewModel() {

    private val flags = PackageManager.GET_META_DATA or
            PackageManager.GET_SHARED_LIBRARY_FILES

    private val usageStatsManager = appContext.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    private val _socialAppUsageStats = MutableLiveData<Map<String, AppInfo>>()
    val socialAppUsageStats: MutableLiveData<Map<String, AppInfo>>
        get() = _socialAppUsageStats

    val timeList = listOf<Int>(5, 10, 20, 30, 60)
    var selectedTime = mutableIntStateOf(timeList.first())

    val listOfInterval = listOf(
        AppUsageIntervalInfo(AppUsageInterval.Today, "Today", getTimeInterval(AppUsageInterval.Today)),
        AppUsageIntervalInfo(AppUsageInterval.Yesterday, "Yesterday", getTimeInterval(AppUsageInterval.Yesterday)),
        AppUsageIntervalInfo(AppUsageInterval.LastWeek, "Last Week", getTimeInterval(AppUsageInterval.LastWeek)),
        AppUsageIntervalInfo(AppUsageInterval.LastMonth, "Last Month", getTimeInterval(AppUsageInterval.LastMonth))
    )
    val selectedUsageInterval = mutableStateOf(listOfInterval.first())

    val stopWatch = mutableIntStateOf(0)
    var _uiEvent = MutableLiveData<FocusScreenEvent>()
        private set
    val uiEvent = _uiEvent

    private val socialApps = listOf(
        "facebook",
        "instagram",
        "twitter",
        "snapchat",
        "whatsapp",
        "messenger",
        "tiktok",
        "youtube",
        "reddit",
        "netflix",
        "spotify",
        "amazon",
    )
    private val workApps = listOf(
        "gm",
        "calendar",
    )

    val isFocusModeEnabled = mutableStateOf(false)
    var timer = Timer()
    init {
        viewModelScope.launch {
            updateUsageStats()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun onEvent(event: FocusScreenEvent) {
        when(event) {
            is FocusScreenEvent.IsLoaded -> {
                _uiEvent.value = FocusScreenEvent.IsLoaded
            }
            is FocusScreenEvent.PermissionNotGranted -> {
                _uiEvent.value = FocusScreenEvent.PermissionNotGranted
            }
            is FocusScreenEvent.IsLoading -> {
                _uiEvent.value = FocusScreenEvent.IsLoading
            }
        }
    }

    fun startFocusMode(){
        isFocusModeEnabled.value = true
        timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                stopWatch.value += 1
                if(stopWatch.value == selectedTime.value*60){
                    stopFocusMode()
                }
            }
        }, 0, 1000)
    }

    fun stopFocusMode(){
        isFocusModeEnabled.value = false
        timer.cancel()
        timer.purge()
        stopWatch.value = 0
    }

    private fun checkForPermission(): Boolean {
        val appOpsManager = appContext.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOpsManager.checkOpNoThrow(OPSTR_GET_USAGE_STATS, myUid(), appContext.packageName)
        if(mode == MODE_ALLOWED ) {
            return true
        }
        _uiEvent.value = FocusScreenEvent.PermissionNotGranted
        return false
    }

    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun updateUsageStats(appUsageInterval: AppUsageInterval = AppUsageInterval.Today) {
        _uiEvent.value = FocusScreenEvent.IsLoading
        if(!checkForPermission()) {
            return
        }
        val getStartTime = getTimeInterval(appUsageInterval)
        val usageStats = usageStatsManager.queryAndAggregateUsageStats(
            getStartTime,
            System.currentTimeMillis()
        )
        val stats: MutableList<UsageStats> = ArrayList()
        stats.addAll(usageStats.values)

        val finalList = mutableMapOf<String, AppInfo>()
        for (stat in stats) {
            if ((isSocialApp(stat.packageName) || isWorkApp(stat.packageName)) && stat.totalTimeInForeground>0) {
                val applicationInfo = try {
                    packageManager.getApplicationInfo(stat.packageName, 0)
                } catch (e: PackageManager.NameNotFoundException){
                    null
                }
                applicationInfo.let {
                    if(applicationInfo!=null){
                        val drawable: Drawable? = try {
                            packageManager.getApplicationIcon(stat.packageName)
                        } catch (e: PackageManager.NameNotFoundException){
                            getDrawable(appContext, R.drawable.sym_def_app_icon)
                        }
                        val appInfo = AppInfo(
                            appName = applicationInfo.loadLabel(packageManager).toString(),
                            packageName = stat.packageName,
                            icon = drawable,
                            timeSpent =  stat.totalTimeInForeground,
                            isSystemApp = (applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0,
                            readableTimeSpent = getReadableTime(stat.totalTimeInForeground)
                        )
                        finalList[stat.packageName] = appInfo
                    }
                }
            }
        }
        _socialAppUsageStats.value = finalList
        delay(500)
        _uiEvent.value = FocusScreenEvent.IsLoaded
    }

    private fun getInstalledAppList(): List<String> {
        val infos: List<ApplicationInfo> = packageManager.getInstalledApplications(flags)
        val installedApps: MutableList<String> = ArrayList()
        for (info in infos) {
            installedApps.add(info.packageName)
        }
        return installedApps
    }

    private fun getReadableTime(time: Long): String {
        val hours = time / 3600000
        val remainingMinutes = if (hours > 0) {
            (time - hours * 3600000) / 60000
        } else {
            time / 60000
        }
        val minutes = remainingMinutes % 60
        val remainingSeconds = if (remainingMinutes > 0) {
            (remainingMinutes - minutes) * 60
        } else {
            time / 1000
        }
        return if (hours > 0) {
            "$hours hr${if(hours>1) "s" else ""} $minutes min${if(minutes>1) "s" else ""}"
        } else if(minutes > 0){
            "$minutes min${if(minutes>1) "s" else ""}"
        }else {
            "$remainingSeconds sec"
        }
    }

    private fun isSocialApp(packageName: String): Boolean {
        return socialApps.any { packageName.contains(it, ignoreCase = true) }
    }

    private fun isWorkApp(packageName: String): Boolean {
        return workApps.any { packageName.contains(it, ignoreCase = true) }
    }

    fun readableTime(time: Int): String {
        val hours = time / 3600
        val minutes = (time % 3600) / 60
        val seconds = time % 60
        return if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }

    fun getTimeInterval(appUsageInterval: AppUsageInterval): Long {
        return when (appUsageInterval){
            is AppUsageInterval.Today -> {
                Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                }.timeInMillis
            }
            is AppUsageInterval.Yesterday -> {
                Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_MONTH, -1)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                }.timeInMillis
            }
            is AppUsageInterval.LastWeek -> {
                Calendar.getInstance().apply {
                    add(Calendar.WEEK_OF_MONTH, -1)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                }.timeInMillis
            }
            is AppUsageInterval.LastMonth -> {
                Calendar.getInstance().apply {
                    add(Calendar.MONTH, -1)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                }.timeInMillis
            }

            else -> {
                Calendar.getInstance().apply {
                    add(Calendar.MONTH, -1)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                }.timeInMillis
            }
        }
    }
}

data class AppUsageIntervalInfo(
    val interval: AppUsageInterval,
    val displayName:String,
    val startTime: Long,
)

sealed class AppUsageInterval {
    object Today: AppUsageInterval()
    object Yesterday: AppUsageInterval()
    object LastWeek: AppUsageInterval()
    object LastMonth: AppUsageInterval()
}
