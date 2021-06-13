package com.example.lealappsdesafio.treino

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lealappsdesafio.R
import com.example.lealappsdesafio.model.Training
import java.sql.Timestamp

class TrainingActivity : AppCompatActivity() {
    val recyclerView by lazy { findViewById<RecyclerView>(R.id.trainingRecycle) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)

        recyclerView.adapter = TrainingAdapter(mutableListOf(Training(1,"macacos me mordam",
            Timestamp(1,1,1,1,1,1,1)
        )))
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}