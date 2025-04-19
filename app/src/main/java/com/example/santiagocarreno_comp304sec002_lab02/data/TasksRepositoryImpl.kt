package com.example.santiagocarreno_comp304sec002_lab02.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalTime

class TasksRepositoryImpl : TasksRepository {
    private val _tasks: MutableStateFlow<List<Task>> = MutableStateFlow(
        listOf(
            Task(
                "Write essay",
                "Write essay about Kotlin",
                false,
                null,
                null
            ),
            Task(
                "Do the chores",
                "Sweep the floor",
                false,
                LocalDate.of(2024, 2, 4),
                LocalTime.of(8, 30)
            ),
            Task(
                "Record demo",
                "Record Game Programming demo assignment",
                true,
                LocalDate.of(2024, 5, 4),
                LocalTime.of(16, 30)
            )
        )
    )

    override fun getTasks(): StateFlow<List<Task>> {
        return _tasks;
    }

    override fun getTask(index: Int): Task {
        return _tasks.value[index];
    }

    override fun addTask(task: Task) {
        _tasks.update { currentTasks ->
            currentTasks + task
        }
    }

    override fun modifyTask(index: Int, task: Task) {
        _tasks.update { currentTasks ->
            currentTasks.mapIndexed { i, task ->
                if (i == index) {
                    task.copy(
                        title = task.title,
                        description = task.description,
                        done = task.done,
                        dueDate = task.dueDate,
                        dueTime = task.dueTime
                    )
                } else task
            }
        }
    }

    override fun changeStatus(index: Int, done: Boolean) {
        _tasks.update { currentTasks ->
            currentTasks.mapIndexed { i, task ->
                if (i == index) task.copy(done = done) else task
            }
        }
    }
}