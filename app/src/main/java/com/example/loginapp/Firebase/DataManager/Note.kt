package com.example.loginapp.Firebase.DataManager

data class Note(var id:String? , var title: String? , var notes: String?, var userUid: String?= "", var creationTime: Long = 0) {
    constructor() : this(null, null, null)
}
