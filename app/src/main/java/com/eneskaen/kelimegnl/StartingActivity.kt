package com.eneskaen.kelimegnl

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.eneskaen.kelimegnl.database.UserDatabase
import com.eneskaen.kelimegnl.model.User
import com.eneskaen.kelimegnl.repository.UserRepository
import com.eneskaen.kelimegnl.viewmodel.UserViewModel
import com.eneskaen.kelimegnl.viewmodel.UserViewModelFactory
import kotlinx.coroutines.delay

class StartingActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var userViewModel : UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starting)
        enableEdgeToEdge()

        val userDao = UserDatabase.getDatabase(this).userDao()
        val repository = UserRepository(userDao)

        // UserViewModelFactory ile ViewModel'i oluşturun
        val factory = UserViewModelFactory(repository)
        userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        userViewModel.user.observe(this){
            if (it != null){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
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