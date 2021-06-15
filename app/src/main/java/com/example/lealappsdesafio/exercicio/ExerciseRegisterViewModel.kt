package com.example.lealappsdesafio.exercicio

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lealappsdesafio.model.Bodybuilding
import com.example.lealappsdesafio.model.Exercise
import com.example.lealappsdesafio.model.Training
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ExerciseRegisterViewModel: ViewModel() {
    val editTraining by lazy { MutableLiveData<MutableList<Exercise>>() }

    private var fireStore = Firebase.firestore
    private var firebaseAuth = Firebase.auth
    private var firebaseStorage = Firebase.storage

    var position = 0
    var positionExercise = 0

    fun getExercises(position: Int){
        firebaseAuth.currentUser?.let { user->
            fireStore.collection("users")
                .document(user.uid)
                .get()
                .addOnSuccessListener {
                    val bodybuilding = it.toObject(Bodybuilding::class.java)
                    editTraining.postValue(bodybuilding?.training?.get(position)?.exercises)
                }
        }
    }

}