package com.eneskaen.kelimegnl.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class Word(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var word: String,
    val level: Int,
    var meaning: String = "",
    var meaning2: String = "",
    var meaning3: String = "",
    var exampleSentence: String = "",
    var exampleSentenceTranslation: String = "",
    var isLearned: Boolean = false,
    var isActivated: Boolean = false,
    var learnedDate: String = ""
)

