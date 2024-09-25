package com.eneskaen.kelimegnl.workmanager

import android.util.Log
import android.widget.Toast
import com.eneskaen.kelimegnl.model.Word
import com.eneskaen.kelimegnl.viewmodel.WordViewModel

object WordWorkManager {
    private var currentWord: Word? = null
    private var currentEngLevel: String? = null
    fun getRandomWord(wordViewModel: WordViewModel, engLevel: String, callback: (Word?) -> Unit) {
        if (currentEngLevel != engLevel || currentWord == null) {
            currentEngLevel = engLevel
            wordViewModel.getRandomWord(engLevel)
            wordViewModel.randomWord.observeForever { word ->
                currentWord = word
                callback(word)
            }
        } else {
            callback(currentWord)
        }
    }

    fun resetWord(wordViewModel: WordViewModel, engLevel: String, callback: (Word?) -> Unit) {
        currentWord = null
        getRandomWord(wordViewModel, engLevel, callback)
    }
}
