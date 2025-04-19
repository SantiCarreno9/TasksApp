package com.example.santiagocarreno_comp304sec002_lab02

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.santiagocarreno_comp304sec002_lab02.data.Task
import com.example.santiagocarreno_comp304sec002_lab02.ui.theme.AppButtonColors
import com.example.santiagocarreno_comp304sec002_lab02.ui.theme.AppTextButtonColors
import com.example.santiagocarreno_comp304sec002_lab02.ui.theme.Santiagocarreno_COMP304Sec002_Lab02Theme
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditor(
    task: Task,
    onSaved: (Task) -> Unit,
    onBackClick: () -> Unit
) {
    var title by remember { mutableStateOf(TextFieldValue(task.title)) };
    var description by remember { mutableStateOf(TextFieldValue(task.description)) };
    var dueDate: LocalDate? by remember { mutableStateOf(task.dueDate) }
    var dueTime: LocalTime? by remember { mutableStateOf(task.dueTime) }
    val doneButtonText = if (!task.done) "Mark completed"
    else "Mark uncompleted"
    val canBeSaved = title.text.isNotEmpty() || description.text.isNotEmpty()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Button(
                        enabled = canBeSaved,
                        colors = AppButtonColors,
                        onClick = {
                            task.title = title.text;
                            task.description = description.text;
                            task.dueDate = dueDate;
                            task.dueTime = dueTime;
                            onSaved(task)
                        },
                        modifier = Modifier
                            .wrapContentSize()
                    ) {
                        Text(
                            "Save", fontSize = 19.sp
                        )
                    }
                }
            })
        },
        bottomBar = {
            Box(
                contentAlignment = Alignment.BottomEnd,
                content = {
                    TextButton(
                        enabled = canBeSaved,
                        colors = AppTextButtonColors,
                        onClick = {
                            task.done = !task.done
                            task.title = title.text;
                            task.description = description.text;
                            task.dueDate = dueDate;
                            task.dueTime = dueTime;
                            onSaved(task)
                        },
                        modifier = Modifier
                            .wrapContentSize()
                    ) {
                        Text(
                            text = doneButtonText,
                            fontSize = 19.sp
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.97f)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 15.dp)
        ) {
            TextField(
                value = title,
                onValueChange = {
                    title = it
                },
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 24.sp
                ),
                placeholder = { Text(text = "Title", fontSize = 24.sp) },
                colors = OutlinedTextFieldDefaults.colors(),
                modifier = Modifier
                    .fillMaxWidth()
            )
            TextField(
                value = description,
                onValueChange = {
                    description = it
                },
                textStyle = TextStyle(
                    fontSize = 20.sp
                ),
                singleLine = false,
                placeholder = { Text(text = "Description", fontSize = 20.sp) },
                colors = OutlinedTextFieldDefaults.colors(),
                modifier = Modifier
                    .fillMaxWidth()
            )
            DateTimePickerButton(
                currentDate = dueDate,
                currentTime = dueTime,
                onDateTimeSelected = { newDate, newTime ->
                    dueDate = newDate
                    dueTime = newTime
                })
        }
    }
}

@Composable
fun DateTimePickerButton(
    currentDate: LocalDate?,
    currentTime: LocalTime?,
    onDateTimeSelected: (LocalDate?, LocalTime?) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDateTime by remember {
        mutableStateOf(getDateTimeText(currentDate, currentTime) ?: "Select date/time")
    }

    TextWithIcon(
        value = selectedDateTime,
        icon = Icons.Default.DateRange,
        description = "Select date",
        onClick = {
            showDatePicker = !showDatePicker
        }
    )
    if (showDatePicker) {
        DateTimePickerModal(
            currentDate = currentDate,
            currentTime = currentTime,
            onConfirm = { date, time ->
                selectedDateTime = getDateTimeText(date, time ?: currentTime) ?: "Select date"
                onDateTimeSelected(date, time ?: currentTime)
                showDatePicker = !showDatePicker
            },
            onDismiss = {
                showDatePicker = !showDatePicker
            }
        )
    }
}

fun getDateTimeText(date: LocalDate?, time: LocalTime?): String? {
    var text: String? = null
    if (date != null) {
        val pattern = if (date.year != LocalDate.now().year) "E, MMM dd, YYYY"
        else "E, MMM dd"
        text = date.format(DateTimeFormatter.ofPattern(pattern))
        val timeText = getTimeText(time)
        if (timeText != null)
            text += ", $timeText"
    }
    return text;
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickerModal(
    currentDate: LocalDate?,
    currentTime: LocalTime?,
    onConfirm: (LocalDate?, LocalTime?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = convertLocalDateToMillis(currentDate ?: LocalDate.now()),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val dateSelected = Instant.ofEpochMilli(utcTimeMillis).atZone(ZoneId.of("UTC"))
                    .toLocalDate()
                return !dateSelected.isBefore(LocalDate.now())
            }

            override fun isSelectableYear(year: Int): Boolean {
                return year>=LocalDate.now().year
            }
        }
    )
    var time: LocalTime? by remember {
        mutableStateOf(null)
    }
    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        title = {
            Text("Select date")
        },
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val milliseconds = datePickerState.selectedDateMillis
                val date = convertMillisToLocalDateTime(milliseconds ?: 0).toLocalDate()
                onConfirm(date, time)
            }) {
                Text("OK")
            }
        },
        text = {
            Column(
            ) {
                DatePicker(
                    state = datePickerState,
                    title = null
                )
                HorizontalDivider(thickness = 2.dp)
                TimePickerButton(
                    currentTime = currentTime,
                    onTimeSelected = {
                        time = it
                    },
                )
                HorizontalDivider(thickness = 2.dp)
            }
        },
        modifier = Modifier
            .fillMaxWidth(0.9f)
    )
}

@Composable
fun TextWithIcon(
    value: String,
    icon: ImageVector,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        colors = AppTextButtonColors,
        modifier = modifier
            .padding(0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = description
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

fun convertMillisToLocalDateTime(milliseconds: Long): LocalDateTime {
    val instant = Instant.ofEpochMilli(milliseconds)
    return LocalDateTime.ofInstant(instant, ZoneId.of("America/Toronto")).plusDays(1)
}

fun convertLocalDateToMillis(localDate: LocalDate): Long {
    return localDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
}

@Composable
fun TimePickerButton(
    currentTime: LocalTime?,
    onTimeSelected: (LocalTime?) -> Unit
) {
    var showTimePicker by remember { mutableStateOf(false) }
    var selectedTime by remember {
        mutableStateOf(getTimeText(currentTime) ?: "Select time")
    }
    TextWithIcon(
        value = selectedTime,
        icon = Icons.Filled.AccessTime,
        description = "Select date",
        onClick = {
            showTimePicker = !showTimePicker
        }
    )
    if (showTimePicker) {
        TimePickerModal(
            currentTime = currentTime,
            onConfirm = {
                selectedTime = getTimeText(it)!!
                onTimeSelected(it)
                showTimePicker = !showTimePicker
            },
            onDismiss = {
                showTimePicker = !showTimePicker
            }
        )
    }
}

fun getTimeText(time: LocalTime?): String? {
    var text: String? = null
    if (time != null) {
        text = time.format(DateTimeFormatter.ofPattern("hh:mm a"))
    }
    return text
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerModal(
    currentTime: LocalTime?,
    onConfirm: (LocalTime?) -> Unit,
    onDismiss: () -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime?.hour ?: LocalTime.now().hour,
        initialMinute = currentTime?.minute ?: LocalTime.now().minute,
        is24Hour = true
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Dismiss")
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val newTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                onConfirm(newTime)
            }) {
                Text("OK")
            }
        },
        text = {
            TimePicker(
                state = timePickerState
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun TaskEditorPreview() {
    Santiagocarreno_COMP304Sec002_Lab02Theme {
        TaskEditor(
            task = Task(
                "",
                "",
                false,
                LocalDate.now(),
                LocalTime.now()
            ),
            onSaved = {},
            onBackClick = { }
        );
    }
}

@Preview(showBackground = true)
@Composable
fun DateTimePickerPreview() {
    Santiagocarreno_COMP304Sec002_Lab02Theme {
        DateTimePickerModal(
            currentDate = LocalDate.now(),
            currentTime = LocalTime.now(),
            onConfirm = { date, time -> },
            onDismiss = {}
        );
    }
}

@Preview(showBackground = true)
@Composable
fun TimePickerPreview() {
    Santiagocarreno_COMP304Sec002_Lab02Theme {
        TimePickerModal(
            currentTime = LocalTime.now(),
            onConfirm = {},
            onDismiss = {}
        )
    }
}