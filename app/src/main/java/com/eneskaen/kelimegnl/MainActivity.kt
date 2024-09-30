package com.eneskaen.kelimegnl

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
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
import androidx.fragment.app.replace
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
    private var mode: Int = -1
    private var isHomeFragmentFirstLoad = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mode = intent.getIntExtra("MODE_KEY", 0)
        Log.d("MainActivityModeKontrol", "Received mode: $mode") // Log ekleyin
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

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val bundle = Bundle().apply {
            if (fragment is HomeFragment) {
                if (mode == 1 && isHomeFragmentFirstLoad){ //
                    putInt("MODE_KEY", 1) // İlk kez HomeFragment'e 1 gönder
                    isHomeFragmentFirstLoad = false // Sonraki yüklemelerde artık 0 göndereceğiz
                }else{
                    putInt("MODE_KEY", 0) // Diğer fragment'lar için her zaman 0 gönder
                    isHomeFragmentFirstLoad = false // Sonraki yüklemelerde artık 0 göndereceğiz
                }
            } else {
                putInt("MODE_KEY", 0) // Diğer fragment'lar için her zaman 0 gönder
            }
        }
        fragment.arguments = bundle

        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainActivityFrameLayout, fragment)
        fragmentTransaction.commit()
    }
}