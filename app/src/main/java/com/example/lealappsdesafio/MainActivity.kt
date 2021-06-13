package com.example.lealappsdesafio

import android.app.Activity
import android.content.ContentProviderClient
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.lealappsdesafio.treino.TrainingActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {
    private val tvEmail by lazy { findViewById<TextInputLayout>(R.id.tvEmail) }
    private val tvPassword by lazy { findViewById<TextInputLayout>(R.id.tvPass) }
    private val fieldEmail by lazy { findViewById<TextInputEditText>(R.id.fieldEmail) }
    private val fieldPassword by lazy { findViewById<TextInputEditText>(R.id.fieldPassword) }

    private lateinit var viewModel:MainViewModel
    lateinit var firebaseAuth: FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        fieldsValidate()

    }

    fun signInGoogle(view: View) {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 200)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 200) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, TrainingActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.w("GoogleSign", "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun fieldsValidate() {
        viewModel.emailValidate.observe(this) {
            if (it)
                tvEmail.error = null
            else {
                tvEmail.error = "Preenchido incorretamente"
                tvEmail.errorIconDrawable = null
            }
            navigation()
        }

        viewModel.passwordValidate.observe(this) {
            if (it)
                tvPassword.error = null
            else {
                tvPassword.error = "Preenchido incorretamente"
                tvPassword.errorIconDrawable = null
            }
            navigation()
        }
    }

    private fun navigation() {
        if (viewModel.passwordValidate.value == true && viewModel.emailValidate.value == true) {
            sigInFirebase(fieldEmail.text.toString(), fieldPassword.text.toString())
        }
    }

    private fun sigInFirebase(email: String, pass: String) {
        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this,task.exception?.message!!,Toast.LENGTH_LONG).show()
            }
        }
    }

    fun login(view: View) {
        val email = fieldEmail.text.toString()
        val password = fieldPassword.text.toString()

        viewModel.validateFields(email, password)
    }

    fun register(view: View){
        val intent = Intent(this,RegisterActivity::class.java)
        startActivity(intent)
    }
}