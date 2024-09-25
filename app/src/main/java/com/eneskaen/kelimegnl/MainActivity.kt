package com.eneskaen.kelimegnl

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
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
    lateinit var dialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        dialog = Dialog(this)
        dialog.setContentView(R.layout.word_dialog_card)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)

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
            dialog.show()
            updateDialogUI(currentWord)

        }

        dialog.setOnDismissListener {
            dialog.dismiss()
        }







    }

    private fun updateDialogUI(currentWordInFunc: Word) {


        val wordDialogWordText = dialog.findViewById<TextView>(R.id.wordDialogWordText)
        val wordDialogEngLevelText = dialog.findViewById<TextView>(R.id.wordDialogEngLevelText)
        val wordDialogSoundButton = dialog.findViewById<ImageView>(R.id.wordDialogSoundButton)
        val wordDialogMeaningText = dialog.findViewById<TextView>(R.id.wordDialogMeaningText)
        val wordDialogMeaningType = dialog.findViewById<TextView>(R.id.wordDialogMeaningType)
        val wordDialogLinear2 = dialog.findViewById<LinearLayout>(R.id.wordDialogLinear2)
        val wordDialogMeaning2Text = dialog.findViewById<TextView>(R.id.wordDialogMeaning2Text)
        val wordDialogMeaning2Type = dialog.findViewById<TextView>(R.id.wordDialogMeaning2Type)
        val wordDialogLinear3 = dialog.findViewById<LinearLayout>(R.id.wordDialogLinear3)
        val wordDialogMeaning3Text = dialog.findViewById<TextView>(R.id.wordDialogMeaning3Text)
        val wordDialogMeaning3Type = dialog.findViewById<TextView>(R.id.wordDialogMeaning3Type)



        wordDialogWordText.text = currentWordInFunc.word
        wordDialogEngLevelText.text = englishLevels[currentWordInFunc.level]

        wordDialogSoundButton.setOnClickListener {
            WordWorkManager.resetWord(wordViewModel, currentUser.engLevel.toString()) {
                it?.let {
                    currentWord = it
                    updateCardViewUI(currentWord)
                    updateDialogUI(currentWord)
                }
            }
        }

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
    }

}
