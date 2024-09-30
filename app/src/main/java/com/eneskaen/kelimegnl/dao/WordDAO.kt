package com.eneskaen.kelimegnl.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.eneskaen.kelimegnl.model.User
import com.eneskaen.kelimegnl.model.Word

@Dao
interface WordDAO {

    @Insert
    suspend fun insertAll(words: List<Word>)

    @Insert
    suspend fun insertSingleWord(word: Word)

    @Update
    suspend fun update(word: Word)

    @Query("select * from words where level = :level and isLearned = 0 order by random() limit 1")
    suspend fun getRandomWord(level: String): Word?

    @Query("select * from words where level = :level and isLearned = 0 order by random() limit :limit")
    suspend fun getRandomWords(level: String, limit: Int): List<Word>

    @Query("select * from words where word = :wordString")
    suspend fun getWordByString(wordString: String): Word?

    @Query("select * from words where id = :wordId limit 1")
    suspend fun getWordById(wordId: Int?): Word?

    @Query("select * from words where isActivated = 1 order by id desc")
    suspend fun getActivatedWords(): List<Word>



}