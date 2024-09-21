package com.eneskaen.kelimegnl.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.eneskaen.kelimegnl.R
import com.eneskaen.kelimegnl.StartingActivity
import com.eneskaen.kelimegnl.anims.anims
import com.eneskaen.kelimegnl.dao.WordDAO
import com.eneskaen.kelimegnl.database.UserDatabase
import com.eneskaen.kelimegnl.database.WordDatabase
import com.eneskaen.kelimegnl.databinding.FragmentGetNameBinding
import com.eneskaen.kelimegnl.model.User
import com.eneskaen.kelimegnl.model.Word
import com.eneskaen.kelimegnl.repository.UserRepository
import com.eneskaen.kelimegnl.repository.WordRepository
import com.eneskaen.kelimegnl.viewmodel.UserViewModel
import com.eneskaen.kelimegnl.viewmodel.UserViewModelFactory
import com.eneskaen.kelimegnl.viewmodel.WordViewModel
import com.eneskaen.kelimegnl.viewmodel.WordViewModelFactory
import java.io.BufferedReader
import java.io.InputStreamReader

class GetNameFragment : Fragment() {

    lateinit var binding : FragmentGetNameBinding
    private lateinit var userViewModel : UserViewModel
    private lateinit var wordViewModel : WordViewModel
    private var selectedOption: Int = 0
    private val englishLevels = arrayOf("A1", "A2", "B1", "B2", "C1")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGetNameBinding.inflate(inflater, container, false)

        //DAO
        val userDao = UserDatabase.getDatabase(requireContext()).userDao()
        val wordDao = WordDatabase.getDatabase(requireContext()).wordDao()

        //Repository
        val wordRepository = WordRepository(wordDao)
        val repository = UserRepository(userDao)

        //ViewModelFactory
        val wordFactory = WordViewModelFactory(wordRepository)
        wordViewModel = ViewModelProvider(this, wordFactory).get(WordViewModel::class.java)

        val userFactory = UserViewModelFactory(repository)
        userViewModel = ViewModelProvider(this, userFactory).get(UserViewModel::class.java)


        setUpSeekBar()
        setUpAnims()

        binding.getNameNextButton.setOnClickListener {

            val name = binding.getNameTextInputLayout.editText?.text.toString().trim()
            if (name.isNotEmpty()){

                userViewModel.insert(User(0, name, selectedOption))
                readWordsFromTextFile()
                wordViewModel.getRandomWord(userViewModel.user.value?.engLevel.toString())
                wordViewModel.randomWord.observe(viewLifecycleOwner){
                    if (it == null){
                        val intent = Intent(requireContext(), StartingActivity::class.java)
                        startActivity(intent)
                    }
                }

            }
            else{
                binding.getNameTextInputLayout.error = "Lütfen bir isim giriniz"
            }
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun readWordsFromTextFile() {
        val words = mutableListOf<Word>()
        val fileNames = arrayOf("a1_words.txt", "a2_words.txt", "b1_words.txt", "b2_words.txt", "c1_words.txt")
        fileNames.forEachIndexed {index , fileName ->
            try {
                val inputStream = requireContext().assets.open(fileName)
                val reader = BufferedReader(InputStreamReader(inputStream))
                reader.useLines { 
                    it.forEach {word ->
                       if (word.isNotEmpty()){
                           words.add(Word(id = 0, word = word.trim(), level = index, meaning = "", definition = ""))
                       }
                    }
                }
            } catch (e : Exception){
                e.printStackTrace()
            }
        }
        insertWordsToRoomDB(words)
    }

    private fun insertWordsToRoomDB(words: List<Word>) {
        wordViewModel.insertWords(words)

    }

    private fun setUpAnims() {
        binding.getNameTextview.animation = anims.getAnimText(requireContext())
        binding.getNameButtonCardView.animation = anims.getAnimButton(requireContext())
        binding.getNameTextInputLayout.animation = anims.getAnimButton(requireContext())
        binding.getNameSeekBar.animation = anims.getAnimText(requireContext())
        binding.getNameSelectedOptionTextView.animation = anims.getAnimText(requireContext())
    }

    private fun setUpSeekBar() {

        binding.getNameSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (englishLevels[progress].equals("A1")) {
                    binding.getNameSelectedOptionTextView.text = "Başlangıç seviyesi veya A1"
                }
                else if (englishLevels[progress].equals("C1")) {
                    binding.getNameSelectedOptionTextView.text = "C1 veya üstü"
                }
                else{
                    binding.getNameSelectedOptionTextView.text = englishLevels[progress]
                }


            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                selectedOption = binding.getNameSeekBar.progress
            }

        })
    }
}
