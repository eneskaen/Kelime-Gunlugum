package com.eneskaen.kelimegnl

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.eneskaen.kelimegnl.database.UserDatabase
import com.eneskaen.kelimegnl.database.WordDatabase
import com.eneskaen.kelimegnl.databinding.ActivityMainBinding
import com.eneskaen.kelimegnl.fragments.ExercisesFragment
import com.eneskaen.kelimegnl.fragments.HomeFragment
import com.eneskaen.kelimegnl.fragments.ProfileFragment
import com.eneskaen.kelimegnl.model.User
import com.eneskaen.kelimegnl.model.Word
import com.eneskaen.kelimegnl.repository.UserRepository
import com.eneskaen.kelimegnl.repository.WordRepository
import com.eneskaen.kelimegnl.viewmodel.UserViewModel
import com.eneskaen.kelimegnl.viewmodel.UserViewModelFactory
import com.eneskaen.kelimegnl.viewmodel.WordViewModel
import com.eneskaen.kelimegnl.viewmodel.WordViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var userViewModel: UserViewModel
    lateinit var wordViewModel: WordViewModel
    lateinit var currentUser: User
    lateinit var currentWord: Word
    private val englishLevels = arrayOf("A1", "A2", "B1", "B2", "C1")
    lateinit var dialog: Dialog
    lateinit var wordDialogSoundButton: ImageView
    private var downX = 0f
    private var upX = 0f
    private val SWIPE_THRESHOLD = 150 // Kaydırma hareketi için minimum mesafe

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        replaceFragment(HomeFragment())
        setUpBottomNavbar()
    }

    private fun setUpBottomNavbar() {
        binding.mainActivityBottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navbarHome -> replaceFragment(HomeFragment())
                R.id.navbarProfile -> replaceFragment(ProfileFragment())
                R.id.navbarExercises -> replaceFragment(ExercisesFragment())

            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainActivityFrameLayout, fragment)
        fragmentTransaction.commit()
    }
}