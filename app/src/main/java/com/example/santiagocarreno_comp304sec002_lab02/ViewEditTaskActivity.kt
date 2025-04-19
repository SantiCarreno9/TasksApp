package com.example.santiagocarreno_comp304sec002_lab02

import androidx.compose.runtime.Composable
import com.example.santiagocarreno_comp304sec002_lab02.viewmodel.TasksViewModel

@Composable
fun ViewEditTaskActivity(
    tasksViewModel: TasksViewModel,
    index: Int,
    onSavedClick: () -> Unit,
    onBackClick: () -> Unit
) {
    TaskEditor(
        tasksViewModel.getTask(index),
        onSaved = { modifiedTask ->
            tasksViewModel.modifyTask(index, modifiedTask);
            onSavedClick()
        },
        onBackClick = onBackClick
    );
}