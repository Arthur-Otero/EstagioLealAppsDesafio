package com.example.lealappsdesafio.treino

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lealappsdesafio.model.Bodybuilding
import com.example.lealappsdesafio.model.Training
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TrainingRegisterViewModel: ViewModel() {
    val editTraining by lazy { MutableLiveData<Training>() }

    private var fireStore = Firebase.firestore
    private var firebaseAuth = Firebase.auth

    private fun createTrainings(trainings:MutableList<Training>){
        firebaseAuth.currentUser?.let { user->
            fireStore.collection("users")
                .document(user.uid)
                .set(Bodybuilding(trainings))
                .addOnSuccessListener {
                    Log.d("teste","gravour")
                }.addOnFailureListener {
                    it.message?.let { it1 -> Log.d("teste", it1) }
                }
        }
    }

    fun getTrainingsView(position: Int){
        firebaseAuth.currentUser?.let { user->
            fireStore.collection("users")
                .document(user.uid)
                .get()
                .addOnSuccessListener {
                    val bodybuilding = it.toObject(Bodybuilding::class.java)
                    editTraining.postValue(bodybuilding?.training?.get(position))
                }
        }
    }

    fun getTrainings(training: Training, position: Int?){
        firebaseAuth.currentUser?.let { user->
            fireStore.collection("users")
                .document(user.uid)
                .get()
                .addOnSuccessListener {
                    val bodybuilding = it.toObject(Bodybuilding::class.java)
                    if (position==null)
                        addTrainings(bodybuilding,training)
                    else{
                        editTrainings(bodybuilding,training,position)
                    }
                }
        }
    }

    private fun addTrainings(bodybuilding: Bodybuilding?, training: Training){
        val lastTraining = bodybuilding?.training ?: mutableListOf()
        lastTraining.add(training)
        createTrainings(lastTraining)
    }

    private fun editTrainings(bodybuilding: Bodybuilding?, training: Training,position: Int){
        val lastTraining = bodybuilding?.training
        lastTraining?.get(position)?.description = training.description
        lastTraining?.get(position)?.date = training.date
        lastTraining?.get(position)?.name = training.name
        lastTraining?.let { createTrainings(it) }
    }
}