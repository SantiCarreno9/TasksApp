package com.example.santiagocarreno_comp304sec002_lab02

import android.app.Application
import com.example.santiagocarreno_comp304sec002_lab02.dependencyinjection.appModules
import org.koin.core.context.startKoin

class TasksApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(appModules)
        }
    }
}