package com.bangkit.gymguru.data

data class Users(
    var name: String? = null,
    val email: String? = null,
    var uid: String? = null,
    var gender: String? = null,
    var age: Long? = 0,
    var weight : Long? = 0,
    var height : Long? = 0
)
