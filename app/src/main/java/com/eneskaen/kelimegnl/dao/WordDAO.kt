package com.eneskaen.kelimegnl.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.eneskaen.kelimegnl.model.User
import com.eneskaen.kelimegnl.model.Word

@Dao
interface WordDAO {

    @Insert
    suspend fun insertAll(words: List<Word>)

    @Insert
    suspend fun insertSingleWord(word: Word)

    @Query("select * from words where level = :level order by random() limit 1")
    suspend fun getRandomWord(level: String): Word?

    @Query("select * from words where word = :wordString")
    suspend fun getWordByString(wordString: String): Word?

    @Query("select * from words where id = :wordId limit 1")
    suspend fun getWordById(wordId: Int?): Word?



}