package com.example.lealappsdesafio.model

import java.sql.Timestamp

class Training() {
    var name : Number = 0
    var description : String = ""
    var date : Timestamp? = null

    constructor(name:Number,description : String,date : Timestamp?):this(){
        this.name = name
        this.description = description
        this.date = date
    }
}