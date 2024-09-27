package com.eneskaen.kelimegnl.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.eneskaen.kelimegnl.model.User

@Dao
abstract interface UserDAO {

    @Insert
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("select * from users limit 1")
    fun getUser(): LiveData<User>

    @Update
    suspend fun update(user: User)

}