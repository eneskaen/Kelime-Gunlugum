package com.eneskaen.kelimegnl.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.eneskaen.kelimegnl.anims.anims
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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

        setUpViewModels()
        setUpSeekBar()
        setUpAnims()
        getNameNextButtonOnClickListener()

        return binding.root
    }

    private fun getNameNextButtonOnClickListener() {

        binding.getNameNextButton.setOnClickListener {

            val name = binding.getNameTextInputLayout.editText?.text.toString().trim()
            if (name.isNotEmpty()){

                val newUser = User(
                    0,
                    name,
                    selectedOption,
                    10,
                    "99999" //İlk kez kelime oluştururkenki değer
                )

                userViewModel.insert(newUser)

                readWordsFromTextFile(requireContext())

            }
            else{
                binding.getNameTextInputLayout.error = "Lütfen bir isim giriniz"
            }
        }
    }

    private fun setUpViewModels() {
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

    }

    private fun readWordsFromTextFile(context: Context) {
        val words = mutableListOf<Word>()
        val fileNames = arrayOf("a1_words.json", "a2_words.json", "b1_words.json", "b2_words.json", "c1_words.json")
        fileNames.forEach { fileName ->
            val inputStream = context.assets.open(fileName)
            val jsonString = inputStream.bufferedReader().use { it.readText() }

            val wordListType = object : TypeToken<List<Word>>() {}.type
            val wordList: List<Word> = Gson().fromJson(jsonString, wordListType)
            words.addAll(wordList)
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
