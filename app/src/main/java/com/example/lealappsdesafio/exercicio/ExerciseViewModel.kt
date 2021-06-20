package com.example.lealappsdesafio.exercicio

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lealappsdesafio.model.Bodybuilding
import com.example.lealappsdesafio.model.Data
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExerciseViewModel():ViewModel() {
    private var fireStore = Firebase.firestore
    private var firebaseAuth = Firebase.auth
    private var firebaseStorage = Firebase.storage
    val updated by lazy { MutableLiveData<Boolean>() }

    fun deleteExercise(positionExercise:Int){
        val position = Data.position
        val exercises = Data.trainings[position].exercises
        if (exercises[positionExercise].image?.contains("null") == false){
            exercises[positionExercise].image?.let { deleteStorage(it,positionExercise) }
        }else{
            Data.trainings[position].exercises.removeAt(positionExercise)
            postExercises()
        }
    }

    private fun deleteStorage(image: String,positionExercise:Int) = CoroutineScope(Dispatchers.IO).launch{
        val position = Data.position
        firebaseAuth.currentUser?.let { user ->
            firebaseStorage.getReference(user.uid)
                .child(image)
                .delete()
                .addOnSuccessListener {
                    Data.trainings[position].exercises.removeAt(positionExercise)
                    postExercises()
                }.addOnFailureListener {
                    Log.d("StorageResultInfoDelete", "fracasso")
                }
        }
    }

    private fun postExercises() {
        firebaseAuth.currentUser?.let { user ->
            fireStore.collection("users")
                .document(user.uid)
                .set(Bodybuilding(Data.trainings))
                .addOnSuccessListener {
                    updated.postValue(true)
                }
        }
    }
}