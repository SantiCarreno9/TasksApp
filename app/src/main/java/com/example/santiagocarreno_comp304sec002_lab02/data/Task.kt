package com.example.santiagocarreno_comp304sec002_lab02.data

import java.time.LocalDate
import java.time.LocalTime

data class Task(
    var title: String,
    var description: String,
    var done: Boolean,
    var dueDate: LocalDate? = null,
    var dueTime: LocalTime? = null
)
