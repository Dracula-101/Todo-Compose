package io.github.dracula101.todo.common.ui

import android.icu.text.SimpleDateFormat
import android.icu.text.TimeZoneFormat
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy

@RequiresApi(Build.VERSION_CODES.N)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTimePicker(
    onTimeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {

    val timePickerState = rememberTimePickerState()

    AlertDialog(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.medium
            ),
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            securePolicy = SecureFlagPolicy.SecureOn
        ),
        onDismissRequest = {}
    ) {
        Column(
            modifier = Modifier
                .padding(top = 28.dp, start = 20.dp, end = 20.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "Select Time", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(12.dp))
            TimePicker(
                state = timePickerState,
            )
            Row(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = {
                    onDismiss()
                }) {
                    Text(text = "Dismiss")
                }
                TextButton(
                    onClick = {
//                        Time Format : 12:00 AM
                        val selectedHour = timePickerState.hour % 12
                        val selectedMinute = timePickerState.minute
                        val is24Hour = timePickerState.is24hour
                        val time = "${if(selectedHour.toString().length == 1) "0${selectedHour}" else selectedHour}:${if(selectedMinute.toString().length ==1) "0${selectedMinute}" else  selectedMinute} ${if (timePickerState.hour < 12) "AM" else "PM"}"
                        onTimeSelected(time)
                        onDismiss()
                    }
                ) {
                    Text(text = "Confirm")
                }
            }
        }
    }

}
