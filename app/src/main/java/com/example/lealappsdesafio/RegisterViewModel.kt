package com.example.lealappsdesafio

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel:ViewModel() {
    val emailLiveData by lazy { MutableLiveData<Boolean>() }
    val passwordValidate by lazy { MutableLiveData<Boolean>() }
    val confirmPasswordValidate by lazy { MutableLiveData<Boolean>() }

    fun validateFields(email:String, password: String,confirmPassword: String){
        when{
            validateEmail(email) && validaPass(password)->{
                emailLiveData.postValue(false)
                passwordValidate.postValue(false)
            }
            validateEmail(email)->{
                emailLiveData.postValue(false)
                passwordValidate.postValue(true)
            }
            validaPass(password)->{
                emailLiveData.postValue(true)
                passwordValidate.postValue(false)
            }
            else->{
                emailLiveData.postValue(true)
                passwordValidate.postValue(true)
            }
        }
        if (password == confirmPassword)
            confirmPasswordValidate.postValue(true)
        else
            confirmPasswordValidate.postValue(false)
    }
}