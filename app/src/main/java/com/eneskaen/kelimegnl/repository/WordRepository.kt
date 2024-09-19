package com.eneskaen.kelimegnl.repository

import com.eneskaen.kelimegnl.dao.WordDAO
import com.eneskaen.kelimegnl.model.Word

class WordRepository(private val wordDao: WordDAO) {

    suspend fun insertWords(words: List<Word>) {
        wordDao.insertAll(words)
    }

    suspend fun getRandomWord(level: String): Word? {
        return wordDao.getRandomWord(level)
    }

    suspend fun getWordByString(wordString: String): Word? {
        return wordDao.getWordByString(wordString)
    }

}