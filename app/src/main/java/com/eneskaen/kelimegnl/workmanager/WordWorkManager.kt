package com.eneskaen.kelimegnl.workmanager

import com.eneskaen.kelimegnl.model.Word
import com.eneskaen.kelimegnl.viewmodel.WordViewModel

object WordWorkManager {
    private var currentWord: Word? = null
    private var currentEngLevel: String? = null

    fun getRandomWord(wordViewModel: WordViewModel, engLevel: String, callback: (Word?) -> Unit) {
        // Eğer engLevel değiştiyse veya kelime daha önce alınmamışsa
        if (currentEngLevel != engLevel || currentWord == null) {
            currentEngLevel = engLevel
            wordViewModel.getRandomWord(engLevel)
            wordViewModel.randomWord.observeForever { word ->
                currentWord = word
                callback(word)
            }
        } else {
            // Eğer kelime daha önce alındıysa, onu geri döndür
            callback(currentWord)
        }
    }

    fun resetWord(wordViewModel: WordViewModel, engLevel: String, callback: (Word?) -> Unit) {
        currentWord = null
        getRandomWord(wordViewModel, engLevel, callback)
    }
}
