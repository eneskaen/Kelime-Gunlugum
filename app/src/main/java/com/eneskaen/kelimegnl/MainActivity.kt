package com.eneskaen.kelimegnl

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.eneskaen.kelimegnl.database.UserDatabase
import com.eneskaen.kelimegnl.database.WordDatabase
import com.eneskaen.kelimegnl.databinding.ActivityMainBinding
import com.eneskaen.kelimegnl.model.User
import com.eneskaen.kelimegnl.model.Word
import com.eneskaen.kelimegnl.repository.UserRepository
import com.eneskaen.kelimegnl.repository.WordRepository
import com.eneskaen.kelimegnl.viewmodel.UserViewModel
import com.eneskaen.kelimegnl.viewmodel.UserViewModelFactory
import com.eneskaen.kelimegnl.viewmodel.WordViewModel
import com.eneskaen.kelimegnl.viewmodel.WordViewModelFactory
import com.eneskaen.kelimegnl.workmanager.WordWorkManager

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var userViewModel : UserViewModel
    lateinit var wordViewModel : WordViewModel
    lateinit var currentUser : User
    lateinit var currentWord : Word
    private val englishLevels = arrayOf("A1", "A2", "B1", "B2", "C1")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        enableEdgeToEdge()

        val wordDao = WordDatabase.getDatabase(this).wordDao()
        val userDao = UserDatabase.getDatabase(this).userDao()

        val userRepository = UserRepository(userDao)
        val wordRepository = WordRepository(wordDao)

        val userFactory = UserViewModelFactory(userRepository)
        val wordFactory = WordViewModelFactory(wordRepository)


        userViewModel = ViewModelProvider(this, userFactory)
            .get(UserViewModel::class.java)

        wordViewModel = ViewModelProvider(this, wordFactory)
            .get(WordViewModel::class.java)

        userViewModel.user.observe(this){
            currentUser = it
            WordWorkManager.getRandomWord(wordViewModel, currentUser.engLevel.toString()){
                it?.let {
                    currentWord = it
                    updateCardViewUI(currentWord)
                }

            }

        }
        binding.mainActivityWordCard.setOnClickListener {
            WordWorkManager.resetWord(wordViewModel, currentUser.engLevel.toString()){
                it?.let {
                    currentWord = it
                    updateCardViewUI(currentWord)
                }
            }
        }



    }

    private fun updateCardViewUI(currentWord: Word?) {
        binding.mainActivityWordText.text = currentWord?.word
        binding.mainActivityUserNameText.text = "Hoşgeldin ${currentUser.name}"
        binding.mainActivityWordEngLevel.text = englishLevels[currentUser.engLevel]
        //DARK MODA ALINCA KELİME DEĞİŞMEMESİ İÇİN RANDOM KELİME ALMA (52.Satır) FONKSİYONUNU MAİNACTİVİTY İÇERİSİNDE DEĞİL DE BİR SİNGLETON İÇİNDE KULLANMAYA ÇALIŞ. SİNGLETON İÇİNDE VERİYİ KONTROL ET.
    }

}
