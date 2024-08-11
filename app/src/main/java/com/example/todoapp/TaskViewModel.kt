package com.example.todoapp

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.time.LocalDate

class TaskViewModel(private val repository: TaskItemRepository) : ViewModel() {

    val taskItems: LiveData<List<TaskItem>> = repository.allTaskItems.asLiveData()

    fun addTaskItem(taskItem: TaskItem) = viewModelScope.launch {
        repository.insertTaskItem(taskItem)
    }

    fun updateTaskItem(taskItem: TaskItem) = viewModelScope.launch {
        repository.updateTaskItem(taskItem)
    }
    fun deleteTaskItem(taskItem: TaskItem) = viewModelScope.launch {
        repository.deleteTaskItem(taskItem)
    }

    fun setCompleted(taskItem: TaskItem) = viewModelScope.launch {
        if (!taskItem.isCompleted()) {
            taskItem.completedDateString = TaskItem.dateFormatter.format(LocalDate.now())
        }
        repository.updateTaskItem(taskItem)
    }
}

class TaskItemModelFactory(private val repository: TaskItemRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            return TaskViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
