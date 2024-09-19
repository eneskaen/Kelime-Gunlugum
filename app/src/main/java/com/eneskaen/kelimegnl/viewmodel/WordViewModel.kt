package com.eneskaen.kelimegnl.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eneskaen.kelimegnl.model.User
import com.eneskaen.kelimegnl.model.Word
import com.eneskaen.kelimegnl.repository.WordRepository
import kotlinx.coroutines.launch

class WordViewModel(private val wordRepository: WordRepository) : ViewModel() {

    private val _randomWord = MutableLiveData<Word?>()
    val randomWord: LiveData<Word?> = _randomWord

    private val _searchedWord = MutableLiveData<Word?>()
    val searchedWord: LiveData<Word?> = _searchedWord

    fun getRandomWord(level: String) {
        viewModelScope.launch {
            _randomWord.value = wordRepository.getRandomWord(level)
        }
    }

    fun searchWordByString(wordString: String) {
        viewModelScope.launch {
            _searchedWord.value = wordRepository.getWordByString(wordString)
        }
    }

    fun insertWords(words: List<Word>) {
        viewModelScope.launch {
            wordRepository.insertWords(words)
        }
    }


}