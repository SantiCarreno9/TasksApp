package com.example.santiagocarreno_comp304sec002_lab02.dependencyinjection

import com.example.santiagocarreno_comp304sec002_lab02.data.TasksRepository
import com.example.santiagocarreno_comp304sec002_lab02.data.TasksRepositoryImpl
import com.example.santiagocarreno_comp304sec002_lab02.viewmodel.TasksViewModel
import org.koin.dsl.module

val appModules = module {
    single<TasksRepository> { TasksRepositoryImpl() }
    single { TasksViewModel(get()) }
}