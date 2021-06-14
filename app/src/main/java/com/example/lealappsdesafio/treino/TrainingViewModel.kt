package com.example.lealappsdesafio.treino

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lealappsdesafio.model.Bodybuilding
import com.example.lealappsdesafio.model.Training
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TrainingViewModel:ViewModel() {
    private var fireStore = Firebase.firestore
    private var firebaseAuth = Firebase.auth

    val trainings by lazy { MutableLiveData<MutableList<Training>>() }

    fun getTrainings(){
        firebaseAuth.currentUser?.let { user->
            fireStore.collection("users")
                .document(user.uid)
                .get()
                .addOnSuccessListener {
                    val bodybuilding = it.toObject(Bodybuilding::class.java)
                    trainings.postValue(bodybuilding?.training)
                }
        }
    }

    fun deleteTraining(training: MutableList<Training>,position:Int){
        training.removeAt(position)
        firebaseAuth.currentUser?.let { user->
            fireStore.collection("users")
                .document(user.uid)
                .set(Bodybuilding(training))
                .addOnCompleteListener {
                    trainings.postValue(training)
                }
        }
    }
}