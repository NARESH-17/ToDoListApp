package com.example.todoapp

import android.content.Context
import android.graphics.Paint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.TaskItemCellBinding
import java.time.format.DateTimeFormatter

class TaskItemViewHolder(
    private val context: Context,
    private val binding: TaskItemCellBinding,
    private val clickListener: TaskItemClickListener
) : RecyclerView.ViewHolder(binding.root) {

    private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")

    fun bindTaskItem(taskItem: TaskItem) {
        binding.name.text = taskItem.name

        if (taskItem.isCompleted()) {
            binding.name.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.duetime.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.editButton.visibility= View.GONE
        }else
        {
            binding.editButton.visibility= View.VISIBLE
        }

        binding.completebutton.setImageResource(taskItem.imageResource())
        binding.completebutton.setColorFilter(taskItem.imageColor(context))

        binding.completebutton.setOnClickListener {
            clickListener.completeTaskItem(taskItem)
        }
        binding.editButton.setOnClickListener {
            clickListener.editTaskItem(taskItem)
        }
        binding.deleteButton.setOnClickListener {
            clickListener.deleteTaskItem(taskItem)
        }

        binding.duetime.text = taskItem.dueTime()?.let { timeFormat.format(it) } ?: ""
        binding.descText.text =taskItem.desc.toString()
    }
}
