package com.example.lealappsdesafio.exercicio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lealappsdesafio.R
import com.example.lealappsdesafio.model.Data

class ExerciseActivity : AppCompatActivity() {
    private val recycle by lazy { findViewById<RecyclerView>(R.id.exerciseRecycle) }

    private lateinit var viewModel: ExerciseViewModel
    private val adapter = ExerciseAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        viewModel = ViewModelProvider(this).get(ExerciseViewModel::class.java)

        recycle.adapter = adapter
        adapter.addExercises(Data.trainings[Data.position].exercises)
        recycle.layoutManager = LinearLayoutManager(this)
    }

    fun click(view:View){
        startActivity(Intent(this,ExerciseRegisterActivity::class.java))
    }

    override fun onResume() {
        super.onResume()
        adapter.addExercises(Data.trainings[Data.position].exercises)
    }

}