package com.eneskaen.kelimegnl

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
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
import com.eneskaen.kelimegnl.workers.WordWorkManager

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    lateinit var userViewModel : UserViewModel
    lateinit var wordViewModel : WordViewModel
    lateinit var currentUser : User
    lateinit var currentWord : Word
    private val englishLevels = arrayOf("A1", "A2", "B1", "B2", "C1")
    lateinit var dialog: Dialog
    lateinit var wordDialogSoundButton : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setUpDialog()
        wordDialogSoundButton = dialog.findViewById<ImageView>(R.id.wordDialogSoundButton)

        setUpViewModels()

        observeUserAndWord()

        mainActivityWordCardClickListener()

        dialog.setOnDismissListener {
            dialog.dismiss()
        }

    }

    private fun mainActivityWordCardClickListener() {
        binding.mainActivityWordCard.setOnClickListener {
            dialog.window?.setDimAmount(0.8f)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
            dialog.show()
            updateDialogUI(currentWord)

        }
    }

    private fun observeUserAndWord() {

        userViewModel.user.observe(this){
            it?.let {
                currentUser = it
                observeWord()

            }


        }
    }

    private fun observeWord() {
        wordViewModel.getWordById(currentUser.lastWordId)
        wordViewModel.searchedWordById.observe(this){
            it?.let {
                currentWord = it
                updateCardViewUI(currentWord)
                updateDialogUI(currentWord)
            }
        }


    }

    private fun setUpViewModels() {
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

    }

    private fun setUpDialog() {
        dialog = Dialog(this)
        dialog.setContentView(R.layout.word_dialog_card)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)

    }

    private fun updateDialogUI(currentWordInFunc: Word) {

        val wordDialogWordText = dialog.findViewById<TextView>(R.id.wordDialogWordText)
        val wordDialogEngLevelText = dialog.findViewById<TextView>(R.id.wordDialogEngLevelText)
        val wordDialogMeaningText = dialog.findViewById<TextView>(R.id.wordDialogMeaningText)
        val wordDialogMeaningType = dialog.findViewById<TextView>(R.id.wordDialogMeaningType)
        val wordDialogLinear2 = dialog.findViewById<LinearLayout>(R.id.wordDialogLinear2)
        val wordDialogMeaning2Text = dialog.findViewById<TextView>(R.id.wordDialogMeaning2Text)
        val wordDialogMeaning2Type = dialog.findViewById<TextView>(R.id.wordDialogMeaning2Type)
        val wordDialogLinear3 = dialog.findViewById<LinearLayout>(R.id.wordDialogLinear3)
        val wordDialogMeaning3Text = dialog.findViewById<TextView>(R.id.wordDialogMeaning3Text)
        val wordDialogMeaning3Type = dialog.findViewById<TextView>(R.id.wordDialogMeaning3Type)
        val wordDialogExampleText = dialog.findViewById<TextView>(R.id.wordDialogExampleText)
        val wordDialogExampleTranslationText = dialog.findViewById<TextView>(R.id.wordDialogExampleTranslationText)
        val wordDialogCountText = dialog.findViewById<TextView>(R.id.wordDialogCountText)

        wordDialogWordText.text = currentWordInFunc.word
        wordDialogEngLevelText.text = englishLevels[currentWordInFunc.level]
        if (currentUser.dailyWordCount == currentUser.dailyWordLimit){
            wordDialogCountText.setTextColor(Color.RED)
            wordDialogCountText.text = "Günlük Kelime Limitiniz Doldu!"
        }
        else{
            wordDialogCountText.setTextAppearance(R.style.Text)
            wordDialogCountText.text = "${currentUser.dailyWordLimit-currentUser.dailyWordCount}/${currentUser.dailyWordLimit}"

        }

        wordDialogSoundButtonClickListener()

        wordDialogExampleText.text = currentWordInFunc.exampleSentence
        wordDialogExampleTranslationText.text = currentWordInFunc.exampleSentenceTranslation

        // Meaning 1
        setMeaningText(1, currentWordInFunc.meaning, wordDialogMeaningText, wordDialogMeaningType, wordDialogLinear2)

        // Meaning 2
        if (currentWordInFunc.meaning2.isNullOrEmpty()) {
            wordDialogLinear2.visibility = View.GONE
        } else {
            wordDialogLinear2.visibility = View.VISIBLE
            setMeaningText(2, currentWordInFunc.meaning2, wordDialogMeaning2Text, wordDialogMeaning2Type, wordDialogLinear2)
        }

        // Meaning 3
        if (currentWordInFunc.meaning3.isNullOrEmpty()) {
            wordDialogLinear3.visibility = View.GONE
        } else {
            wordDialogLinear3.visibility = View.VISIBLE
            setMeaningText(3, currentWordInFunc.meaning3, wordDialogMeaning3Text, wordDialogMeaning3Type, wordDialogLinear3)
        }
    }

    private fun wordDialogSoundButtonClickListener() {

        wordDialogSoundButton.setOnClickListener {
            
            if (currentUser.dailyWordCount < currentUser.dailyWordLimit) {
                changeRandomWord()
            }else{
                Toast.makeText(this, "Limitiniz doldu", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun changeRandomWord() {
        wordViewModel.getRandomWord(currentUser.engLevel.toString())
        wordViewModel.randomWord.observe(this){
            it?.let {
                currentWord = it
                val updatedUser = currentUser.copy(lastWordId = it.id, dailyWordCount = currentUser.dailyWordCount+1)
                userViewModel.update(updatedUser)
                currentUser = updatedUser
                updateCardViewUI(currentWord)
                updateDialogUI(currentWord)
                wordViewModel.randomWord.removeObservers(this)
            }
        }
    }

    private fun setMeaningText(meaningNumber: Int, meaning: String, meaningTextView: TextView, meaningTypeTextView: TextView, linearLayout: LinearLayout) {
        val parts = meaning.split(" (")
        meaningTextView.text = "${meaningNumber.toString()}. ${parts[0]}"

        if (parts.size > 1 && parts[1].endsWith(")")) {
            meaningTypeTextView.text = "(${parts[1].substringBefore(")")})"
        } else {
            meaningTypeTextView.visibility = View.GONE
        }
    }

    private fun updateCardViewUI(currentWord: Word?) {
        binding.mainActivityWordText.text = currentWord?.word
        binding.mainActivityUserNameText.text = "Hoşgeldin ${currentUser.name}"
        binding.mainActivityWordEngLevel.text = englishLevels[currentUser.engLevel]
        binding.mainActivityWordMeaningText.text = currentWord?.meaning
        if (currentUser.dailyWordCount==currentUser.dailyWordLimit){
            binding.mainActivityWordCountText.setTextColor(Color.RED)
            binding.mainActivityWordCountText.text = "${currentUser.dailyWordLimit-currentUser.dailyWordCount}/${currentUser.dailyWordLimit}"
        }
        else{
            binding.mainActivityWordCountText.setTextAppearance(R.style.Text)
            binding.mainActivityWordCountText.text = "${currentUser.dailyWordLimit-currentUser.dailyWordCount}/${currentUser.dailyWordLimit}"
        }

    }

}
