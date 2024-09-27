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

    // LiveData nesnelerini ViewModel'e ekliyoruz
    private val _randomWord = MutableLiveData<Word?>()
    val randomWord: LiveData<Word?> = _randomWord

    private val _searchedWord = MutableLiveData<Word?>()
    val searchedWord: LiveData<Word?> = _searchedWord

    private val _searchedWordById = MutableLiveData<Word?>()
    val searchedWordById: LiveData<Word?> = _searchedWordById


    // Kelimeleri veritabanına eklemek
    fun insertWords(words: List<Word>) {
        viewModelScope.launch {
            wordRepository.insertWords(words)
        }
    }

    fun insertSingleWord(word: Word) {
        viewModelScope.launch {
            wordRepository.insertSingleWord(word)
        }
    }

    // Random kelimeyi alıp LiveData'ya set ediyoruz
    fun getRandomWord(level: String) {
        viewModelScope.launch {
            _randomWord.value = wordRepository.getRandomWord(level)
        }
    }

    // Kelimeyi string ile arıyoruz
    fun searchWordByString(wordString: String) {
        viewModelScope.launch {
            _searchedWord.value = wordRepository.getWordByString(wordString)
        }
    }

    // ID ile kelimeyi alıyoruz
    fun getWordById(wordId: Int?) {
        viewModelScope.launch {
            _searchedWordById.value = wordRepository.getWordById(wordId)
        }
    }

}
