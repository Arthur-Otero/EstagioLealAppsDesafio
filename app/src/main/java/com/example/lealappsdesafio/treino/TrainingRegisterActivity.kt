package com.example.lealappsdesafio.treino

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.lealappsdesafio.R
import com.example.lealappsdesafio.model.Training
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Timestamp
import java.util.*

class TrainingRegisterActivity : AppCompatActivity() {
    private val name by lazy { findViewById<TextInputEditText>(R.id.tvName) }
    private val description by lazy { findViewById<TextInputEditText>(R.id.tvDescription) }
    private val confirmButton by lazy { findViewById<Button>(R.id.button) }
    private val info by lazy { findViewById<TextView>(R.id.trainingInfo) }
    private val backBtn by lazy { findViewById<ImageView>(R.id.backBtnTraining) }

    private lateinit var viewModel: TrainingRegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training_register)
        window.statusBarColor = ContextCompat.getColor(this ,R.color.black)
        viewModel = ViewModelProvider(this).get(TrainingRegisterViewModel::class.java)

        val intent = intent.extras
        if (intent?.getInt("POSITION") != null){
            val position = intent.getInt("POSITION")
            viewModel.getTrainingsView(position)
            viewModel.editTraining.observe(this){
                name.text = SpannableStringBuilder(it.name.toString())
                description.text = SpannableStringBuilder(it.description)
            }
            info.text = "Edite um Treino"

            confirmButton.setOnClickListener {
                edit(position)
            }

        }else{
            info.text = "Crie um Treino"
            confirmButton.setOnClickListener {
                confirm()
            }
        }

        backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    fun confirm(){
        val time = System.currentTimeMillis()
        val ts = Timestamp(Date(time))
        val nameText = name.text.toString().toInt()
        val descriptionText = description.text.toString()

        viewModel.getTrainings(Training(nameText,descriptionText,ts, mutableListOf()),null)
        finish()
    }

    fun edit(position:Int){
        val time = System.currentTimeMillis()
        val ts = Timestamp(Date(time))
        val nameText = name.text.toString().toInt()
        val descriptionText = description.text.toString()

        viewModel.getTrainings(Training(nameText,descriptionText,ts, mutableListOf()),position)
        finish()
    }
}