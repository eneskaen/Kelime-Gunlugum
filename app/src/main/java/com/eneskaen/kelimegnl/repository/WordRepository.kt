package com.eneskaen.kelimegnl.repository

import androidx.lifecycle.LiveData
import com.eneskaen.kelimegnl.dao.WordDAO
import com.eneskaen.kelimegnl.model.Word

class WordRepository(private val wordDao: WordDAO) {

    suspend fun insertWords(words: List<Word>) {
        wordDao.insertAll(words)
    }

    suspend fun insertSingleWord(word: Word){
        wordDao.insertSingleWord(word)
    }

    suspend fun getRandomWord(level: String): Word? {
        return wordDao.getRandomWord(level)
    }

    suspend fun getRandomWords(level: String, limit: Int): List<Word> {
        return wordDao.getRandomWords(level, limit)
    }

    suspend fun getActivatedWords(): List<Word> {
        return wordDao.getActivatedWords()
    }

    suspend fun getWordByString(wordString: String): Word? {
        return wordDao.getWordByString(wordString)
    }

    suspend fun getWordById(wordId: Int?): Word? {
        return wordDao.getWordById(wordId)
    }

    suspend fun update(word: Word) {
        wordDao.update(word)
    }


}