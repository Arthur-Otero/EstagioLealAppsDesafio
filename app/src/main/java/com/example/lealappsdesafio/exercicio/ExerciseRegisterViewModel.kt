package com.example.lealappsdesafio.exercicio

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lealappsdesafio.model.Bodybuilding
import com.example.lealappsdesafio.model.Data
import com.example.lealappsdesafio.model.Exercise
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.*

class ExerciseRegisterViewModel : ViewModel() {
    private var fireStore = Firebase.firestore
    private var firebaseAuth = Firebase.auth
    private var firebaseStorage = Firebase.storage

    val name by lazy { MutableLiveData<String>() }
    val note by lazy { MutableLiveData<String>() }
    val imageObserve by lazy { MutableLiveData<Uri>() }
    val loadingTotal by lazy { MutableLiveData<Long>() }
    val loadingNow by lazy { MutableLiveData<Long>() }

    private var uri:Uri? = null

    private fun postExercises() {
        firebaseAuth.currentUser?.let { user ->
            fireStore.collection("users")
                .document(user.uid)
                .set(Bodybuilding(Data.trainings))
                .addOnSuccessListener {
                }
        }
    }

    private fun uploadStorage(image: String, imageUri: Uri?) {
        firebaseAuth.currentUser?.let { user ->
            imageUri?.let {
                firebaseStorage.getReference(user.uid)
                    .child(image)
                    .putFile(it)
                    .addOnSuccessListener {
                        Log.d("StorageResultInfoUpload", "sucesso")
                    }.addOnFailureListener {
                        Log.d("StorageResultInfoUpload", "fracasso")
                    }.addOnProgressListener { bytes ->
                        loadingTotal.postValue(bytes.totalByteCount)
                        loadingNow.postValue(bytes.bytesTransferred)
                    }
            }
        }
    }

    fun updateData(exercise: Exercise, imageUri: Uri?) {
        val position = Data.position
        Data.trainings[position].exercises.add(exercise)
        if (exercise.image != null) {
            uploadStorage(exercise.image!!, imageUri)
        }
        postExercises()
    }

    private fun deleteStorage(image: String) {
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

    fun updateExercise(name: String, note: String, imageUri: Uri?, exercisePosition: Int) {
        val position = Data.position
        val imagePath = Data.trainings[position].exercises[exercisePosition].image
        val currentTime: Date = Calendar.getInstance().time

        if (imageUri != null) {
            imagePath?.let { deleteStorage(it) }
            Data.trainings[position].exercises[exercisePosition].image = "${imageUri}_${currentTime}"
            uploadStorage("${imageUri}_${currentTime}", imageUri)
        }

        Data.trainings[position].exercises[exercisePosition].name = name.toInt()
        Data.trainings[position].exercises[exercisePosition].note = note
        postExercises()
    }

    fun getData(exercisePosition: Int) {
        val position = Data.position
        val getName = Data.trainings[position].exercises[exercisePosition].name.toString()
        val getNote = Data.trainings[position].exercises[exercisePosition].note
        uploadStorage(Data.trainings[position].exercises[exercisePosition].image)

        name.postValue(getName)
        note.postValue(getNote)
    }

    private fun uploadStorage(image: String?){
        firebaseAuth.currentUser?.let { user ->
            image?.let {
                firebaseStorage.getReference(user.uid)
                    .child(it)
                    .downloadUrl
                    .addOnSuccessListener { uri ->
                        imageObserve.postValue(uri)
                    }.addOnFailureListener {
                        Log.d("StorageResultInfoBaixar", "fracasso")
                    }
            }
        }
    }
}