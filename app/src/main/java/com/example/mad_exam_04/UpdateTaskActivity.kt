package co.sanchez.proyecto_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.ArrayAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import co.sanchez.proyecto_kotlin.databinding.ActivityUpdateTaskBinding

class UpdateTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateTaskBinding
    private lateinit var db: TasksDatabaseHelper
    private var taskId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = TasksDatabaseHelper(this)

        // Obtener el ID de la tarea desde el intent
        taskId = intent.getIntExtra("task_id", -1)
        if (taskId == -1) {
            finish()
            return
        }

        // Obtener la tarea desde la base de datos y llenar los campos del formulario
        val task = db.getTaskByID(taskId)
        binding.updateTitleEditText.setText(task.title)
        binding.updateTaskContentEditText.setText(task.content)
        binding.updateCompletionDateEditText.setText(task.completionDate)

        // Configurar el Spinner de prioridad
        val priorityOptions = resources.getStringArray(R.array.priority_options)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, priorityOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.uptadePrioritySpinner.adapter = adapter

        // Establecer la prioridad seleccionada en el Spinner
        val taskPriority = task.priority  // Obtén la prioridad de la tarea
        val position = priorityOptions.indexOf(taskPriority)
        if (position >= 0) {
            binding.uptadePrioritySpinner.setSelection(position)
        }

        // Guardar los cambios al presionar el botón
        binding.updateSaveButton.setOnClickListener {
            val newTitle = binding.updateTitleEditText.text.toString()
            val newContent = binding.updateTaskContentEditText.text.toString()
            val newCompletionDate = binding.updateCompletionDateEditText.text.toString()

            // Obtener la prioridad seleccionada
            val selectedPriority = binding.uptadePrioritySpinner.selectedItem.toString()

            // Obtener la fecha actual en formato de texto
            val currentDate = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
            val formattedDate = dateFormat.format(currentDate)

            // Crear un objeto Task actualizado
            val updatedTask = Task(taskId, newTitle, newContent, newCompletionDate, formattedDate, selectedPriority)

            // Actualizar la tarea en la base de datos
            db.updateTask(updatedTask)
            finish()
            Toast.makeText(this, "Task Updated", Toast.LENGTH_SHORT).show()
        }
    }
}
