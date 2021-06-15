package com.example.lealappsdesafio.model

import com.google.firebase.Timestamp

class Training() {
    var name : Int = 0
    var description : String = ""
    var date : Timestamp? = null
    var exercises : MutableList<Exercise> = mutableListOf()

    constructor(name:Int,description : String,date : Timestamp?,exercises : MutableList<Exercise>):this(){
        this.name = name
        this.description = description
        this.date = date
        this.exercises = exercises
    }
}