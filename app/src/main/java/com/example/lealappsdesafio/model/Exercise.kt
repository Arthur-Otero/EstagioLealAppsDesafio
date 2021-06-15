package com.example.lealappsdesafio.model

import android.net.Uri

class Exercise() {
    var name : Int = 0
    var image : Uri? = null
    var note : String = ""

    constructor(name : Int,image : Uri?,note : String):this(){
        this.name = name
        this.image = image
        this.note = note
    }
}