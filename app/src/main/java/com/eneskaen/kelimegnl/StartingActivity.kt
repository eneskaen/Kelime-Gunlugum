package com.eneskaen.kelimegnl

import android.content.Intent
import android.os.Bundle
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
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.eneskaen.kelimegnl.database.UserDatabase
import com.eneskaen.kelimegnl.database.WordDatabase
import com.eneskaen.kelimegnl.model.User
import com.eneskaen.kelimegnl.repository.UserRepository
import com.eneskaen.kelimegnl.repository.WordRepository
import com.eneskaen.kelimegnl.viewmodel.UserViewModel
import com.eneskaen.kelimegnl.viewmodel.UserViewModelFactory
import com.eneskaen.kelimegnl.viewmodel.WordViewModel
import com.eneskaen.kelimegnl.viewmodel.WordViewModelFactory
import kotlinx.coroutines.delay

class StartingActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var userViewModel : UserViewModel
    private lateinit var wordViewModel : WordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starting)
        enableEdgeToEdge()

        val userDao = UserDatabase.getDatabase(this).userDao()
        val wordDao = WordDatabase.getDatabase(this).wordDao()
        val wordRepository = WordRepository(wordDao)
        val repository = UserRepository(userDao)

        // UserViewModelFactory ile ViewModel'i oluşturun
        val factory = UserViewModelFactory(repository)
        val wordFactory = WordViewModelFactory(wordRepository)
        userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)
        wordViewModel = ViewModelProvider(this, wordFactory).get(WordViewModel::class.java)

        userViewModel.user.observe(this){
            if (it != null){
                wordViewModel.getRandomWord(it.engLevel.toString())
                wordViewModel.randomWord.observe(this){word ->
                    if (word == null){
                        val intent = Intent(this, StartingActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                        finish()
                    }
                }
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
                }
            else{
                findViewById<FragmentContainerView>(R.id.fragmentContainerView).visibility = View.VISIBLE
                val navHostFragment = supportFragmentManager
                    .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
                navController = navHostFragment.navController
            }

        }

    }


}