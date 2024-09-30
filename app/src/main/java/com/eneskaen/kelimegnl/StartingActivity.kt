package com.eneskaen.kelimegnl

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.eneskaen.kelimegnl.database.UserDatabase
import com.eneskaen.kelimegnl.database.WordDatabase
import com.eneskaen.kelimegnl.model.User
import com.eneskaen.kelimegnl.model.Word
import com.eneskaen.kelimegnl.repository.UserRepository
import com.eneskaen.kelimegnl.repository.WordRepository
import com.eneskaen.kelimegnl.viewmodel.UserViewModel
import com.eneskaen.kelimegnl.viewmodel.UserViewModelFactory
import com.eneskaen.kelimegnl.viewmodel.WordViewModel
import com.eneskaen.kelimegnl.viewmodel.WordViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StartingActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var userViewModel : UserViewModel
    private lateinit var wordViewModel : WordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starting)
        enableEdgeToEdge()

        setUpViewModels()
        observeUser()

    }

    private fun Today() : String {
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val todayDate = formatter.format(Date())
        return todayDate
    }


    private fun observeUser() {
        userViewModel.user.observe(this) {
            it?.let { user ->
                userViewModel.user.removeObservers(this)
                when {
                    user.lastWordUpdateDate == "99999" -> { // İlk kez kelime oluşturulacaksa
                        Log.d("StartingActivityLogu", "Kullanıcı ilk kelimesi oluşturuluyor.")
                        fetchFirstRandomWords(user)
                    }
                    user.lastWordUpdateDate == Today() -> { // Kullanıcı BUGÜN kelime oluşturmuşsa
                        startMainActivity(0)
                    }
                    else -> { // Kullanıcı daha önce kelime oluşturmuş
                        Log.d("StartingActivityLogu", "Kullanıcı daha önce kelimeler oluşturmuş ${user.lastWordUpdateDate}")
                        startMainActivity(1)
                    }
                }
            } ?: run {
                // Kullanıcı daha önce oluşturulmamışsa
                findViewById<FragmentContainerView>(R.id.fragmentContainerView).visibility = View.VISIBLE
                val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
                navController = navHostFragment.navController
            }
        }
    }

    private fun fetchFirstRandomWords(user: User) {
        updateUserData(user)

        startMainActivity(1)
//        wordViewModel.getRandomWords(user.engLevel.toString(), user.dailyWordLimit)
//
//        wordViewModel.randomWords.observe(this) {
//            it?.let {
//                wordViewModel.randomWords.removeObservers(this)
//                updateUserData(user)
//
//                startMainActivity(1)
//
//            } ?: run {
//                retryFetchFirstRandomWordsWithDelay(user)
//            }
//        }
    }

    private fun updateUserData(user: User) {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        val updatedUser = user.copy(lastWordUpdateDate = currentDate.toString())
        userViewModel.update(updatedUser)
    }


    private fun retryFetchFirstRandomWordsWithDelay(user: User) {
        lifecycleScope.launch {
            delay(1000)
            fetchFirstRandomWords(user)
        }
    }

    private fun startMainActivity(mode: Int) { //Mode 1 ise kelime oluşturmaya ihtiyaç var. Mode 0 ise sadece kelime okuyacaz dbden. Kelimeler belli zaten.
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.putExtra("MODE_KEY", mode)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    private fun setUpViewModels() {
        val userDao = UserDatabase.getDatabase(this).userDao()
        val wordDao = WordDatabase.getDatabase(this).wordDao()
        val wordRepository = WordRepository(wordDao)
        val repository = UserRepository(userDao)

        // UserViewModelFactory ile ViewModel'i oluşturun
        val factory = UserViewModelFactory(repository)
        val wordFactory = WordViewModelFactory(wordRepository)
        userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)
        wordViewModel = ViewModelProvider(this, wordFactory).get(WordViewModel::class.java)
    }


}