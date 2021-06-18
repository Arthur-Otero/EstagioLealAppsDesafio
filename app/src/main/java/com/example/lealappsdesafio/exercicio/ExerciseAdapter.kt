package com.example.lealappsdesafio.exercicio

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lealappsdesafio.R
import com.example.lealappsdesafio.model.Exercise
import com.example.lealappsdesafio.model.Training
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ExerciseAdapter() : RecyclerView.Adapter<ExerciseAdapter.RecycleViewHolder>() {
    var exercise = mutableListOf<Exercise>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.exercise_card, parent, false)
        return RecycleViewHolder(view)
    }

    override fun getItemCount(): Int = exercise.size

    override fun onBindViewHolder(holder: RecycleViewHolder, position: Int) {
        holder.name.text = exercise[position].name.toString()
        holder.observation.text = exercise[position].note
        Picasso.get().load(exercise[position].image).into(holder.image)
    }

    inner class RecycleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name by lazy { view.findViewById<TextView>(R.id.nameExerciseCard) }
        val image by lazy { view.findViewById<ImageView>(R.id.imageCard) }
        val observation by lazy { view.findViewById<TextView>(R.id.observationCard) }
    }

    fun addExercises(exercises: List<Exercise>){
        exercise.clear()
        exercise.addAll(exercises)
        notifyDataSetChanged()
    }

}