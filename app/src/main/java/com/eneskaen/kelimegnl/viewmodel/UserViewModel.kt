package com.eneskaen.kelimegnl.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eneskaen.kelimegnl.model.User
import com.eneskaen.kelimegnl.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    val user : LiveData<User> = userRepository.getUser()

    fun insert(user: User) = viewModelScope.launch {
        userRepository.insert(user)
    }

    fun update(user: User) = viewModelScope.launch {
        userRepository.update(user)
    }



}