package com.example.lealappsdesafio.exercicio

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.lealappsdesafio.model.Bodybuilding
import com.example.lealappsdesafio.model.Data
import com.example.lealappsdesafio.model.Exercise
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ExerciseRegisterViewModel: ViewModel() {
    private var fireStore = Firebase.firestore
    private var firebaseAuth = Firebase.auth
    private var firebaseStorage = Firebase.storage

    fun postExercises(){
        firebaseAuth.currentUser?.let { user->
            fireStore.collection("users")
                .document(user.uid)
                .set(Bodybuilding(Data.trainings))
                .addOnSuccessListener {
                }
        }
    }

    fun uploadStorage(image:String,imageUri: Uri?){
        firebaseAuth.currentUser?.let { user->
            imageUri?.let {
                firebaseStorage.getReference(user.uid)
                    .child(image)
                    .putFile(it)
                    .addOnSuccessListener {
                        Log.d("StorageResultInfo","sucesso")
                    }.addOnFailureListener {
                        Log.d("StorageResultInfo","fracasso")
                    }.addOnProgressListener {
                        it
                    }
            }
        }
    }

    fun updateData(exercise: Exercise, imageUri: Uri?){
        val position = Data.position
        Data.trainings[position].exercises.add(exercise)
        if (exercise.image != null){
            uploadStorage(exercise.image!!,imageUri)
        }
        postExercises()
    }
}