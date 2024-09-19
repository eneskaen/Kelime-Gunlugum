package com.eneskaen.kelimegnl.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class Word(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val word : String,
    val level : Int,
    var meaning : String = "",
    var definition : String = "",
    var isLearned : Boolean = false

)
