package com.example.lealappsdesafio.exercicio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lealappsdesafio.R

class ExerciseActivity : AppCompatActivity() {
    private val recycle by lazy { findViewById<RecyclerView>(R.id.exerciseRecycle) }

    private lateinit var viewModel: ExerciseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        viewModel = ViewModelProvider(this).get(ExerciseViewModel::class.java)

        val adapter = ExerciseAdapter()
        recycle.adapter = adapter
        recycle.layoutManager = LinearLayoutManager(this)
    }
}