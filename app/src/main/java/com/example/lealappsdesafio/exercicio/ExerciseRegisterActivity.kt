package com.example.lealappsdesafio.exercicio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.lealappsdesafio.R

class ExerciseRegisterActivity : AppCompatActivity() {
    private val name by lazy { findViewById<TextView>(R.id.NameExerciseField) }//imageExercise/tvNote
    private val image by lazy { findViewById<ImageView>(R.id.imageExercise) }
    private val note by lazy { findViewById<TextView>(R.id.tvNote) }

    private lateinit var viewModel: ExerciseRegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_register)

        viewModel = ViewModelProvider(this).get(ExerciseRegisterViewModel::class.java)
    }
}