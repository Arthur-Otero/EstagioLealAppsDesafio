package com.example.lealappsdesafio.exercicio

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.SpannableStringBuilder
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.lealappsdesafio.R
import com.example.lealappsdesafio.exercicio.permissionsHelper.FileHelper
import com.example.lealappsdesafio.exercicio.permissionsHelper.PermissionsHelper
import com.example.lealappsdesafio.model.Exercise
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import java.io.File
import java.util.*


class ExerciseRegisterActivity : AppCompatActivity() {
    private val info by lazy { findViewById<TextView>(R.id.exerciseInfo) }
    private val name by lazy { findViewById<TextInputEditText>(R.id.tvExerciseName) }
    private val picture by lazy { findViewById<ImageView>(R.id.imageExercise) }
    private val note by lazy { findViewById<TextInputEditText>(R.id.tvNote) }
    private val button by lazy { findViewById<Button>(R.id.button) }
    private val backBtn by lazy { findViewById<ImageView>(R.id.backBtnExercise) }
    private val progress by lazy { findViewById<com.google.android.material.progressindicator.LinearProgressIndicator>(R.id.progressIndicator) }

    companion object {
        const val FILE_AUTHORITY = "com.example.lealappsdesafio"
    }

    private var imageUri: Uri? = null
    var fileShare: File? = null
    private lateinit var permissionsHelper: PermissionsHelper

    private lateinit var viewModel: ExerciseRegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_register)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        permissionsHelper = PermissionsHelper(this)
        viewModel = ViewModelProvider(this).get(ExerciseRegisterViewModel::class.java)

        picture.setOnClickListener {
            takePhoto()
        }

        val intent = intent.extras
        val extra = intent?.getChar("TYPE")
        val position = intent?.getInt("POSITION")
        if (extra == 'c') {
            info.text = "Crie um exercício"
        } else if (extra == 'e') {
            position?.let { viewModel.getData(it) }
            info.text = "Edite o exercício"
            viewModel.name.observe(this) {
                name.text = SpannableStringBuilder(it)
            }
            viewModel.note.observe(this) {
                note.text = SpannableStringBuilder(it)
            }
            viewModel.imageObserve.observe(this) {
                Picasso.get().load(it).into(picture)
            }
        }
        button.setOnClickListener {
            when (extra) {
                'c' -> postData()
                'e' -> position?.let { it1 -> updateData(it1) }
                else -> Toast.makeText(this, "Ocorreu algum erro", Toast.LENGTH_LONG).show()
            }

            viewModel.loadingTotal.observe(this){ total ->
                progress.visibility = VISIBLE
                viewModel.loadingNow.observe(this){ now ->
                    if (total == now)
                        finish()
                }
            }
        }

        backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    fun postData() {
        val nameData = name.text.toString().toInt()
        val noteData = note.text.toString()
        val currentTime: Date = Calendar.getInstance().time

        viewModel.updateData(Exercise(nameData, "${imageUri}_${currentTime}", noteData), imageUri)
    }

    private fun updateData(exercisePosition: Int) {
        val nameData = name.text.toString()
        val noteData = note.text.toString()

        viewModel.updateExercise(nameData, noteData, imageUri, exercisePosition)
    }

    private fun takePhoto() {
        val permissions =
            listOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissionsHelper.requestAllPermission(permissions)) {
            openChooser()
        }
    }

    private fun openChooser() {
        val intentList = mutableListOf<Intent>()

        //takePhotoIntent
        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file = FileHelper.createFileInStorage(this)
        val uri = FileProvider.getUriForFile(
            this,
            FILE_AUTHORITY,
            file!!
        )

        fileShare = file

        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)


        //pickImageIntent
        val pickIntent = Intent()
        pickIntent.type = "image/*"
        pickIntent.action = Intent.ACTION_GET_CONTENT

        //Adiciona na lista de intents
        intentList.add(pickIntent)
        intentList.add(takePhotoIntent)

        val chooserIntent = Intent.createChooser(intentList[0], "Escolha como tirar a fotografia:")
        chooserIntent.putExtra(
            Intent.EXTRA_INITIAL_INTENTS,
            intentList.toTypedArray()
        )

        startActivityForResult(chooserIntent, 200)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (isIntentFromCamera(requestCode, resultCode, intent)) {
            val image = BitmapFactory.decodeFile(fileShare?.path)
            picture.background = null
            picture.setImageBitmap(image)

            imageUri = FileProvider.getUriForFile(
                applicationContext,
                FILE_AUTHORITY,
                fileShare!!
            )
        } else if (isIntentFromFiles(requestCode, resultCode, intent)) {
            val pic = intent?.data as Uri
            imageUri = pic
            picture.background = null
            picture.setImageURI(pic)
        }
    }

    private fun isIntentFromFiles(
        requestCode: Int,
        resultCode: Int,
        intent: Intent?
    ) = requestCode == 200 && resultCode == Activity.RESULT_OK && intent?.data != null

    private fun isIntentFromCamera(
        requestCode: Int,
        resultCode: Int,
        intent: Intent?
    ) =
        requestCode == 200 && resultCode == Activity.RESULT_OK && (intent == null || intent.extras == null) && intent?.data == null
}