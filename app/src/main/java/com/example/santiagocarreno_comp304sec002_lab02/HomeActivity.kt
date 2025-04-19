package com.example.santiagocarreno_comp304sec002_lab02

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.santiagocarreno_comp304sec002_lab02.data.Task
import com.example.santiagocarreno_comp304sec002_lab02.data.TasksRepositoryImpl
import com.example.santiagocarreno_comp304sec002_lab02.ui.theme.AppButtonColors
import com.example.santiagocarreno_comp304sec002_lab02.ui.theme.Santiagocarreno_COMP304Sec002_Lab02Theme
import com.example.santiagocarreno_comp304sec002_lab02.viewmodel.TasksViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.LocalDateTime

enum class TaskScreen() {
    HomeActivity,
    CreateTaskActivity,
    ViewEditTaskActivity
}

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Santiagocarreno_COMP304Sec002_Lab02Theme {
                TasksApp()
            }
        }
    }
}

@Composable
fun TasksApp() {
    val navController = rememberNavController()
    val tasksViewModel: TasksViewModel = koinViewModel();
    NavHost(
        navController = navController,
        startDestination = TaskScreen.HomeActivity.name
    ) {
        composable(route = TaskScreen.HomeActivity.name) {
            HomeActivity(
                tasksViewModel = tasksViewModel,
                onNewTaskClick = {
                    navController.navigate(TaskScreen.CreateTaskActivity.name)
                },
                onTaskClick = {
                    navController.navigate(TaskScreen.ViewEditTaskActivity.name + "/${it}")
                }
            )
        }
        composable(route = TaskScreen.CreateTaskActivity.name) {
            CreateTaskActivity(
                tasksViewModel = tasksViewModel,
                onSavedClick = {
                    navController.navigate(TaskScreen.HomeActivity.name)
                },
                onBackClick = {
                    navController.navigate(TaskScreen.HomeActivity.name)
                }
            )
        }
        composable(
            route = TaskScreen.ViewEditTaskActivity.name + "/{index}",
            arguments = listOf(
                navArgument("index") {
                    type = NavType.IntType
                    nullable = false
                    defaultValue = 1
                })
        ) {
            val index = it.arguments?.getInt("index")
            ViewEditTaskActivity(
                tasksViewModel = tasksViewModel,
                index = index!!,
                onSavedClick = {
                    navController.navigate(TaskScreen.HomeActivity.name)
                },
                onBackClick = {
                    navController.navigate(TaskScreen.HomeActivity.name)
                })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeActivity(
    tasksViewModel: TasksViewModel,
    onNewTaskClick: () -> Unit,
    onTaskClick: (Int) -> Unit
) {
    val tasks by tasksViewModel.getTasks().collectAsState()
    //Represents the amount of tasks that are done in order to show/hide the "Completed" section
    var doneTasksCount by remember {
        mutableIntStateOf(tasks.count { it.done })
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Tasks App") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = @androidx.compose.runtime.Composable {
            FloatingActionButton(
                containerColor = AppButtonColors.containerColor,
                contentColor = AppButtonColors.contentColor,
                onClick = onNewTaskClick,
                modifier = Modifier
                    .size(70.dp)
            ) {
                Icon(
                    Icons.Filled.Add, "Create Note button.",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            item {
                Section(title = "ToDo") {
                    tasks.forEachIndexed { index, task ->
                        if (!task.done) {
                            TaskPreview(task,
                                onClick = {
                                    onTaskClick(index)
                                },
                                onStatusChanged = { status ->
                                    tasksViewModel.changeStatus(
                                        index,
                                        status
                                    )
                                    doneTasksCount++
                                })
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                if (doneTasksCount > 0) {
                    Section(title = "Completed") {
                        tasks.forEachIndexed { index, task ->
                            if (task.done) {
                                TaskPreview(task,
                                    onClick = {
                                        onTaskClick(index)
                                    },
                                    onStatusChanged = { status ->
                                        tasksViewModel.changeStatus(
                                            index,
                                            status
                                        )
                                        doneTasksCount--
                                    })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Section(
    title: String,
    content: @Composable() () -> Unit
) {
    Box() {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold
            )
            content()
        }
    }
}

@Composable
fun TaskPreview(
    task: Task,
    onClick: (Task) -> Unit,
    onStatusChanged: (Boolean) -> Unit
) {
    var date = getDateTimeText(task.dueDate, task.dueTime)
    var done by remember {
        mutableStateOf(task.done)
    }
    var isOverdue = false;
    if (!done) {
        //Compares the dueDate with the current local time
        if (task.dueDate != null) {
            if (task.dueTime != null) {
                isOverdue = LocalDateTime.now().isAfter(
                    LocalDateTime.of(task.dueDate, task.dueTime)
                )
            } else isOverdue = LocalDate.now().isAfter(task.dueDate);
        }
        if (isOverdue)
            date += " - Overdue"
    }

    Card(
        onClick = { onClick(task) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .alpha(
                if (done) 0.5f
                else 1f
            )
    ) {
        Row {
            RadioButton(
                selected = done,
                onClick = {
                    onStatusChanged(!done)
                })
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                if (task.title.isNotEmpty()) {
                    Text(
                        text = task.title,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(5.dp)
                    )
                }
                if (task.description.isNotEmpty()) {
                    Text(
                        text = task.description,
                        fontSize = 15.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(5.dp)
                    )
                }
                if (date != null) {
                    Text(
                        text = date,
                        fontSize = 15.sp,
                        color = if (isOverdue && !task.done) Color.Red
                        else Color.Black,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(5.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val tasksViewModel = TasksViewModel(TasksRepositoryImpl())
    Santiagocarreno_COMP304Sec002_Lab02Theme {
        HomeActivity(
            tasksViewModel = tasksViewModel,
            onNewTaskClick = {},
            onTaskClick = {}
        )
    }
}