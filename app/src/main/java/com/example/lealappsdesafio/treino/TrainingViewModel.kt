package com.example.lealappsdesafio.treino

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lealappsdesafio.R
import com.example.lealappsdesafio.model.Bodybuilding
import com.example.lealappsdesafio.model.Training
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class TrainingViewModel:ViewModel() {
    private var fireStore = Firebase.firestore
    private var firebaseAuth = Firebase.auth
    private var firebaseStorage = Firebase.storage

    val trainings by lazy { MutableLiveData<MutableList<Training>>() }

    fun getTrainings() = CoroutineScope(IO).launch{
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
        training[position].exercises.forEach {
            it.image?.let { it1 -> deleteStorage(it1) }
        }
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

    private fun deleteStorage(image: String) = CoroutineScope(IO).launch{
        firebaseAuth.currentUser?.let { user ->
            firebaseStorage.getReference(user.uid)
                .child(image)
                .delete()
                .addOnSuccessListener {
                    Log.d("StorageResultInfoDelete", "sucesso")
                }.addOnFailureListener {
                    Log.d("StorageResultInfoDelete", "fracasso")

                }
        }
    }

    fun logout(googleSignIn:GoogleSignInClient){
        googleSignIn.signOut()
        firebaseAuth.signOut()
    }
}
