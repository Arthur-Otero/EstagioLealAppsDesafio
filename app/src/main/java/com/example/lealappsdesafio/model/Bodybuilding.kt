package com.example.lealappsdesafio.model

class Bodybuilding() {
    var training = mutableListOf<Training>()

    constructor(training: MutableList<Training>):this(){
        this.training = training
    }
}