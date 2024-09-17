package com.eneskaen.kelimegnl.repository

import androidx.lifecycle.LiveData
import com.eneskaen.kelimegnl.dao.UserDAO
import com.eneskaen.kelimegnl.model.User

class UserRepository(private val userDao: UserDAO) {

    suspend fun insert(user: User) {
        return userDao.insert(user)
    }

    suspend fun delete(user: User) {
        return userDao.delete(user)
    }

    fun getUser(): LiveData<User>{
        return userDao.getUser()
    }

}