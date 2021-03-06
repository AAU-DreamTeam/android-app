package com.example.androidapp.models
//id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, transportEmission REAL NOT NULL, GHPenalty BOOLEAN NOT NULL CHECK(GHPenalty IN (0, 1))
class Country(val id: Int,
              val name: String,
              val transportEmission: Double,
              val ghPenalty: Boolean){

    constructor(name: String, transportEmission: Double, ghPenalty: Boolean): this(0, name, transportEmission, ghPenalty)
    constructor(): this(0, "", 0.0, false)

    fun validate(): Boolean {
        return id > 0
    }
}