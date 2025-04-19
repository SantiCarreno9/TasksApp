package com.example.santiagocarreno_comp304sec002_lab02

import androidx.compose.runtime.Composable
import com.example.santiagocarreno_comp304sec002_lab02.data.Task
import com.example.santiagocarreno_comp304sec002_lab02.viewmodel.TasksViewModel

@Composable
fun CreateTaskActivity(
    tasksViewModel: TasksViewModel,
    onSavedClick: () -> Unit,
    onBackClick: () -> Unit
) {
    TaskEditor(
        task = Task("", "", false),
        onSaved = { task ->
            tasksViewModel.addTask(task);
            onSavedClick()
        },
        onBackClick = onBackClick
    );
}