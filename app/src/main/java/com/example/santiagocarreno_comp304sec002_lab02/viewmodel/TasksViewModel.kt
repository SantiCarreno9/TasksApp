package com.example.santiagocarreno_comp304sec002_lab02.viewmodel

import androidx.lifecycle.ViewModel
import com.example.santiagocarreno_comp304sec002_lab02.data.Task
import com.example.santiagocarreno_comp304sec002_lab02.data.TasksRepository
import kotlinx.coroutines.flow.StateFlow

class TasksViewModel(
    private val tasksRepository: TasksRepository
) : ViewModel() {
    fun getTasks(): StateFlow<List<Task>> = tasksRepository.getTasks()
    fun getTask(index: Int) = tasksRepository.getTask(index)
    fun addTask(task: Task) = tasksRepository.addTask(task)
    fun modifyTask(index: Int, task: Task) = tasksRepository.modifyTask(index, task)
    fun changeStatus(index: Int, done: Boolean) = tasksRepository.changeStatus(index, done)
}