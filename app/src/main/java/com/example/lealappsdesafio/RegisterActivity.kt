package com.example.lealappsdesafio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private val email by lazy { findViewById<TextInputEditText>(R.id.tvEmail) }
    private val password by lazy { findViewById<TextInputEditText>(R.id.tvPassword) }
    private val confirmPassword by lazy { findViewById<TextInputEditText>(R.id.tvConfirmPassword) }
    private val tvEmail by lazy { findViewById<TextInputLayout>(R.id.emailField) }
    private val tvPassword by lazy { findViewById<TextInputLayout>(R.id.passwordField) }
    private val tvConfirmPassword by lazy { findViewById<TextInputLayout>(R.id.confirmPasswordField) }

    private val firebaseAuth = Firebase.auth
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        fieldsValidate()
    }

    private fun fieldsValidate() {
        viewModel.emailLiveData.observe(this) {
            if (it)
                tvEmail.error = null
            else {
                tvEmail.error = "Preenchido incorretamente"
                tvEmail.errorIconDrawable = null
            }
            confirmFieldToCreateUser()
        }

        viewModel.passwordValidate.observe(this) {
            if (it)
                tvPassword.error = null
            else {
                tvPassword.error = "Preenchido incorretamente"
                tvPassword.errorIconDrawable = null
            }
            confirmFieldToCreateUser()
        }

        viewModel.confirmPasswordValidate.observe(this) {
            if (it)
                tvConfirmPassword.error = null
            else {
                tvConfirmPassword.error = "Digite a senha igual a anterior"
                tvConfirmPassword.errorIconDrawable = null
            }
            confirmFieldToCreateUser()
        }
    }

    fun verifiedRegister(view:View) {
        val emailString = email.text.toString()
        val passwordString = password.text.toString()
        val confirmPasswordString = confirmPassword.text.toString()

        viewModel.validateFields(emailString, passwordString, confirmPasswordString)
    }

    private fun confirmFieldToCreateUser() {
        if (viewModel.confirmPasswordValidate.value == true && viewModel.emailLiveData.value == true && viewModel.passwordValidate.value == true)
            createUserWithEmailPass(
                email.text.toString(),
                password.text.toString(),
            )
    }

    private fun createUserWithEmailPass(email: String, pass: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this,"Conta criada com sucesso", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this,"Falha ao criar conta, tente novamente depois", Toast.LENGTH_LONG).show()
            }
        }
    }
}