package com.example.lealappsdesafio.exercicio

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
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
import java.lang.reflect.Method

class ExerciseAdapter(val callback : (Int, Char)->Unit) : RecyclerView.Adapter<ExerciseAdapter.RecycleViewHolder>() {
    var exercise = mutableListOf<Exercise>()
    private var firebaseAuth = Firebase.auth
    private var firebaseStorage = Firebase.storage

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.exercise_card, parent, false)
        return RecycleViewHolder(view)
    }

    override fun getItemCount(): Int = exercise.size

    override fun onBindViewHolder(holder: RecycleViewHolder, position: Int) {
        holder.name.text = exercise[position].name.toString()
        holder.observation.text = exercise[position].note
        holder.menu.setOnClickListener {
            showMenu(it, R.menu.menu_training_card,position)
        }
        if (exercise[position].image?.contains("null") == false) {
            holder.image.layoutParams.height = 400
            uploadStorage(exercise[position].image, holder)
        }
        else
            holder.image.layoutParams.height = 0
    }

    private fun showMenu(view: View, @MenuRes menuRes: Int, position: Int) {
        val popup = PopupMenu(view.context!!, view)
        popup.menuInflater.inflate(menuRes, popup.menu)

        popup.setOnMenuItemClickListener {
            when(it.itemId){//delete
                R.id.edit->{
                    callback(position,'e')
                }
                R.id.delete->{
                    callback(position,'d')
                }
            }
            false
        }

        try {
            val fields = popup.javaClass.declaredFields
            for (field in fields) {
                if ("mPopup" == field.name) {
                    field.isAccessible = true
                    val menuPopupHelper = field[popup]
                    val classPopupHelper =
                        Class.forName(menuPopupHelper.javaClass.name)
                    val setForceIcons: Method = classPopupHelper.getMethod(
                        "setForceShowIcon",
                        Boolean::class.javaPrimitiveType
                    )
                    setForceIcons.invoke(menuPopupHelper, true)
                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        popup.show()
    }

    private fun uploadStorage(image: String?, holder: RecycleViewHolder) {
        firebaseAuth.currentUser?.let { user ->
            image?.let {
                firebaseStorage.getReference(user.uid)
                    .child(it)
                    .downloadUrl
                    .addOnSuccessListener { uri ->
                        Picasso.get().load(uri).into(holder.image)
                        Log.d("StorageResultInfoR", "sucessoImagem")
                    }.addOnFailureListener {
                        Log.d("StorageResultInfoR", "fracasso")
                    }
            }
        }
    }

    inner class RecycleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val menu by lazy { view.findViewById<Button>(R.id.menuCardExercise) }
        val name by lazy { view.findViewById<TextView>(R.id.nameExerciseCard) }
        val image by lazy { view.findViewById<ImageView>(R.id.imageCard) }
        val observation by lazy { view.findViewById<TextView>(R.id.observationCard) }
    }

    fun addExercises(exercises: List<Exercise>) {
        exercise.clear()
        exercise.addAll(exercises)
        notifyDataSetChanged()
    }

}