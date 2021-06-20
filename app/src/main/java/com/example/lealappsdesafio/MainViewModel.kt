package com.example.lealappsdesafio

import android.content.Intent
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainViewModel : ViewModel() {
    val emailValidate: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val passwordValidate: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    fun validateFields(email: String, password: String){
        when{
            validateEmail(email) && validaPass(password)->{
                emailValidate.postValue(false)
                passwordValidate.postValue(false)
            }
            validateEmail(email)->{
                emailValidate.postValue(false)
                passwordValidate.postValue(true)
            }
            validaPass(password)->{
                emailValidate.postValue(true)
                passwordValidate.postValue(false)
            }
            else->{
                emailValidate.postValue(true)
                passwordValidate.postValue(true)
            }
        }
    }
}

fun validateEmail(email: String):Boolean{
    return email.isEmpty()
}

fun validaPass(pass:String):Boolean{
    return pass.length <= 6
}