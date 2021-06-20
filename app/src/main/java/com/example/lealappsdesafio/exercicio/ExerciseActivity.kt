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
import com.example.lealappsdesafio.treino.TrainingRegisterActivity

class ExerciseActivity : AppCompatActivity() {
    private val recycle by lazy { findViewById<RecyclerView>(R.id.exerciseRecycle) }
    private val toolbar by lazy { findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarExercise) }

    private lateinit var viewModel: ExerciseViewModel
    private val adapter = ExerciseAdapter(){position, type->
    if (type == 'e'){
        val intent = Intent(this, ExerciseRegisterActivity::class.java)
        intent.putExtra("TYPE",'e')
        intent.putExtra("POSITION" , position)
        startActivity(intent)
    }else{
        viewModel.deleteExercise(position)
    }
}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.addExercise-> {
                    click()
                    true
                }
                else -> false
            }
        }

        viewModel = ViewModelProvider(this).get(ExerciseViewModel::class.java)

        recycle.adapter = adapter
        adapter.addExercises(Data.trainings[Data.position].exercises)
        recycle.layoutManager = LinearLayoutManager(this)

        viewModel.updated.observe(this){
            if (it){
                adapter.addExercises(Data.trainings[Data.position].exercises)
                viewModel.updated.postValue(false)
            }
        }
    }

    fun click(){
        val intent = Intent(this,ExerciseRegisterActivity::class.java)
        intent.putExtra("TYPE",'c')
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        adapter.addExercises(Data.trainings[Data.position].exercises)
    }
}