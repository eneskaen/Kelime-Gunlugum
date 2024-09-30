package com.eneskaen.kelimegnl.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val name: String,
    val engLevel: Int = 0,
    val dailyWordLimit : Int = 10,
    val lastWordUpdateDate : String = ""
)
