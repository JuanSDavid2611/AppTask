package co.sanchez.proyecto_kotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import co.sanchez.proyecto_kotlin.databinding.ActivityAllTasksBinding

class AllTasksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAllTasksBinding
    private lateinit var db: TasksDatabaseHelper
    private lateinit var tasksAdapter: TasksAdapter
    private val tasksList = mutableListOf<Task>() // Lista mutable para gestionar las tareas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = TasksDatabaseHelper(this)

        // Inicializar el adaptador
        tasksAdapter = TasksAdapter(tasksList, this)
        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.tasksRecyclerView.adapter = tasksAdapter

        // Configurar el Spinner con las opciones de prioridad
        val priorityFilters = resources.getStringArray(R.array.priority_filters)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, priorityFilters)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.prioritySpinner.adapter = adapter

        // Configurar el evento de selección del Spinner
        binding.prioritySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedFilter = priorityFilters[position]
                applyFilter(selectedFilter)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No hacer nada si no se selecciona nada
            }
        }

        // Botón para agregar nuevas tareas
        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddNewTaskActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        refreshTasks()
    }

    private fun refreshTasks() {
        tasksList.clear()
        tasksList.addAll(db.getAllTasks()) // Cargar todas las tareas desde la base de datos
        tasksAdapter.notifyDataSetChanged()
    }

    private fun applyFilter(filter: String) {
        tasksList.clear() // Limpiar la lista actual

        when (filter) {
            "Todos" -> tasksList.addAll(db.getAllTasks())
            "Prioridad baja" -> tasksList.addAll(db.getTasksByPriority("Prioridad baja"))
            "Prioridad media" -> tasksList.addAll(db.getTasksByPriority("Prioridad media"))
            "Prioridad alta" -> tasksList.addAll(db.getTasksByPriority("Prioridad alta"))
        }

        tasksAdapter.notifyDataSetChanged() // Actualizar la vista con las tareas filtradas
    }
}
