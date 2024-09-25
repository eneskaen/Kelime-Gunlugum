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
    var learnedDate: String = ""
) {
    // Constructor
    init {
        word = word.lowercase().replaceFirstChar { it.uppercase() } // 'word' alanını küçük yapıp baş harfini büyük yap
        meaning = meaning.lowercase().replaceFirstChar { it.uppercase() } // 'meaning' alanını küçük yapıp baş harfini büyük yap
        meaning2 = meaning2.lowercase().replaceFirstChar { it.uppercase() } // 'meaning2' alanını küçük yapıp baş harfini büyük yap
        meaning3 = meaning3.lowercase().replaceFirstChar { it.uppercase() } // 'meaning3' alanını küçük yapıp baş harfini büyük yap
        exampleSentence = exampleSentence.lowercase().replaceFirstChar { it.uppercase() } // 'exampleSentence' alanını küçük yapıp baş harfini büyük yap
        exampleSentenceTranslation = exampleSentenceTranslation.lowercase().replaceFirstChar { it.uppercase() } // 'exampleSentenceTranslation' alanını küçük yapıp baş harfini büyük yap
    }
}

