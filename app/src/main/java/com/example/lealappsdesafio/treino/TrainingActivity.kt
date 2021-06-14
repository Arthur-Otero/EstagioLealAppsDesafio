package com.example.lealappsdesafio.treino

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lealappsdesafio.R
import com.example.lealappsdesafio.model.Training
import java.sql.Timestamp

class TrainingActivity : AppCompatActivity() {
    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.trainingRecycle) }

    lateinit var viewModel: TrainingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)

        viewModel = ViewModelProvider(this).get(TrainingViewModel::class.java)
        viewModel.getTrainings()
        viewModel.trainings.observe(this){
            val adapter = TrainingAdapter(){ position, type->
                if (type == 'e'){
                    val intent = Intent(this,TrainingRegisterActivity::class.java)
                    intent.putExtra("POSITION",position)
                    startActivity(intent)
                }else{
                    viewModel.deleteTraining(it,position)
                }
            }
            recyclerView.adapter = adapter
            adapter.addTraining(it)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        val handle = Handler(Looper.myLooper()!!)
        handle.postDelayed(Runnable { viewModel.getTrainings() }, 500)
        viewModel.getTrainings()
    }

    fun floatButtonClick(view:View){
        val intent = Intent(this,TrainingRegisterActivity::class.java)
        startActivity(intent)
    }
}