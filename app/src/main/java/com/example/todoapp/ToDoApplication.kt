package com.example.todoapp

import android.app.Application
import com.example.todoapp.TaskItemDatabase
import com.example.todoapp.TaskItemRepository

class TodoApplication : Application() {
    val database by lazy { TaskItemDatabase.getDatabase(this) }
    val repository by lazy { TaskItemRepository(database.taskItemDao()) }
}
