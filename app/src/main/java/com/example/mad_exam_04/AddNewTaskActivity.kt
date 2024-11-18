package co.sanchez.proyecto_kotlin

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import co.sanchez.proyecto_kotlin.databinding.ActivityAddNewTaskBinding
import java.text.SimpleDateFormat
import java.util.*

class AddNewTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNewTaskBinding
    private lateinit var db: TasksDatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = TasksDatabaseHelper(this)

        // Abrir DatePickerDialog para seleccionar la fecha
        binding.completionDateTextView.setOnClickListener {
            openDatePickerDialog()
        }

        // Guardar la tarea
        binding.saveButton.setOnClickListener {
            val title = binding.taskTitleEditText.text.toString().trim()
            val content = binding.taskContentEditText.text.toString().trim()
            val completionDate = binding.completionDateTextView.text.toString().trim()
            val priority = binding.prioritySpinner.selectedItem.toString().trim()

            if (title.isEmpty() || content.isEmpty() || completionDate.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Obtener la fecha de creación automática del dispositivo
            val currentDate = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            val creationDate = dateFormat.format(currentDate) // La fecha actual formateada

            val task = Task(0, title, content, completionDate, creationDate, priority)
            db.insertTask(task)
            finish()
            Toast.makeText(this, "Nueva Tarea Guardada", Toast.LENGTH_SHORT).show()
        }
    }

    // Método para abrir el DatePickerDialog
    private fun openDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            binding.completionDateTextView.text = formattedDate
        }, year, month, day)

        datePickerDialog.show()
    }

    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 1001
    }
}
