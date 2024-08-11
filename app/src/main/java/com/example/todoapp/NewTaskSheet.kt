package com.example.todoapp

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.todoapp.databinding.FragmentNewTaskSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalTime

class NewTaskSheet(private var taskItem: TaskItem?) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNewTaskSheetBinding
    private val taskViewModel: TaskViewModel by activityViewModels {
        TaskItemModelFactory((requireActivity().application as TodoApplication).repository)
    }
    private var dueTime: LocalTime? = null
    private var isNameError = false
    private var isDescError = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewTaskSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (taskItem != null) {
            binding.tasktitle.text = "Edit Task"
            binding.name.text = Editable.Factory.getInstance().newEditable(taskItem!!.name)
            binding.desc.text = Editable.Factory.getInstance().newEditable(taskItem!!.desc)
            if (taskItem!!.dueTime() != null) {
                dueTime = taskItem!!.dueTime()!!
                updateTimeButtonText()
            }
        } else {
            binding.tasktitle.text = "New Task"
        }

        binding.savebutton.setOnClickListener {
            saveAction()
        }
        binding.timePickerButton.setOnClickListener {
            openTimePicker()
        }
    }

    private fun openTimePicker() {
        if (dueTime == null) dueTime = LocalTime.now()
        val listener = TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
            dueTime = LocalTime.of(selectedHour, selectedMinute)
            updateTimeButtonText()
        }
        val dialog = TimePickerDialog(activity, listener, dueTime!!.hour, dueTime!!.minute, true)
        dialog.setTitle("Task Due")
        dialog.show()
    }

    private fun updateTimeButtonText() {
        binding.timePickerButton.text = String.format("%02d:%02d", dueTime!!.hour, dueTime!!.minute)
    }

    private fun checkErrors() {
        if (binding.name.text.isNullOrBlank()) {
            isNameError =true
            binding.name.error = "Please add the name!"
        }else
            isNameError = false
        if (binding.desc.text.isNullOrBlank()) {
            isDescError=true
            binding.desc.error = "Please add the desc"
        }
        else
            isDescError = false

    }

    private fun saveAction() {
        val name = binding.name.text.toString()
        val desc = binding.desc.text.toString()

        checkErrors()
        if (!isNameError && !isDescError) {
            val dueTimeString = dueTime?.let { TaskItem.timeFormatter.format(it) }
            if (taskItem == null) {
                val newTask = TaskItem(name, desc, dueTimeString, null)
                taskViewModel.addTaskItem(newTask)
            } else {
                taskItem!!.name = name
                taskItem!!.desc = desc
                taskItem!!.dueTimeString = dueTimeString
                taskViewModel.updateTaskItem(taskItem!!)
            }
            binding.name.setText("")
            binding.desc.setText("")
            dismiss()
        }
        else{
            Toast.makeText(context,"Please Fill the details to Continue!",Toast.LENGTH_LONG).show()
        }


    }

}

