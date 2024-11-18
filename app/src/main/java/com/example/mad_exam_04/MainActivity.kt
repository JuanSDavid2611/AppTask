package co.sanchez.proyecto_kotlin


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import co.sanchez.proyecto_kotlin.databinding.ActivityMainBinding
import androidx.recyclerview.widget.LinearLayoutManager


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val getStartedButton = findViewById<Button>(R.id.startButton)
        getStartedButton.setOnClickListener {
            val intent = Intent(this, AllTasksActivity::class.java)
            startActivity(intent)
        }
    }
}


