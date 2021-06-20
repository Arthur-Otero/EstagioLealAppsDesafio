package com.example.lealappsdesafio.treino

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lealappsdesafio.R
import com.example.lealappsdesafio.model.Training
import java.lang.NullPointerException
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
            try {
                val adapter = TrainingAdapter{ position, type->
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
            }catch (e:NullPointerException){
                Toast.makeText(this,"Nada encontrado",Toast.LENGTH_LONG).show()
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        val handle = Handler(Looper.myLooper()!!)
        handle.postDelayed({ viewModel.getTrainings() }, 500)
    }

    fun floatButtonClick(view:View){
        val intent = Intent(this,TrainingRegisterActivity::class.java)
        startActivity(intent)
    }
}