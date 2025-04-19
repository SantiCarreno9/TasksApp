package com.example.santiagocarreno_comp304sec002_lab02.data

import kotlinx.coroutines.flow.StateFlow

interface TasksRepository {
    fun getTasks(): StateFlow<List<Task>>
    fun getTask(index: Int):Task
    fun addTask(task: Task)
    fun modifyTask(index: Int, task: Task)
    fun changeStatus(index: Int, done: Boolean)
}