package com.eneskaen.kelimegnl.fragments

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.eneskaen.kelimegnl.R
import com.eneskaen.kelimegnl.database.UserDatabase
import com.eneskaen.kelimegnl.database.WordDatabase
import com.eneskaen.kelimegnl.databinding.FragmentHomeBinding
import com.eneskaen.kelimegnl.model.User
import com.eneskaen.kelimegnl.model.Word
import com.eneskaen.kelimegnl.repository.UserRepository
import com.eneskaen.kelimegnl.repository.WordRepository
import com.eneskaen.kelimegnl.viewmodel.UserViewModel
import com.eneskaen.kelimegnl.viewmodel.UserViewModelFactory
import com.eneskaen.kelimegnl.viewmodel.WordViewModel
import com.eneskaen.kelimegnl.viewmodel.WordViewModelFactory


class HomeFragment : Fragment() {

    lateinit var binding : FragmentHomeBinding
    lateinit var userViewModel : UserViewModel
    lateinit var wordViewModel : WordViewModel
    lateinit var currentUser : User
    lateinit var currentWord : Word
    private val englishLevels = arrayOf("A1", "A2", "B1", "B2", "C1")
    lateinit var dialog: Dialog
    lateinit var wordDialogSoundButton : ImageView
    private lateinit var randomWords: List<Word>
    private var currentPosition = 0
    private var maxPosition = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        setUpDialog()

        wordDialogSoundButton = dialog.findViewById<ImageView>(R.id.wordDialogSoundButton)

        setUpViewModels()

        observeUser()

        mainActivityWordCardClickListener()

        dialog.setOnDismissListener {
            dialog.dismiss()
        }

        return binding.root
    }

    private fun setUpViewModels() {
        Log.d("Deneme123", "setUpViewModels")
        val wordDao = WordDatabase.getDatabase(requireContext()).wordDao()
        val userDao = UserDatabase.getDatabase(requireContext()).userDao()

        val userRepository = UserRepository(userDao)
        val wordRepository = WordRepository(wordDao)

        val userFactory = UserViewModelFactory(userRepository)
        val wordFactory = WordViewModelFactory(wordRepository)

        userViewModel = ViewModelProvider(this, userFactory)
            .get(UserViewModel::class.java)

        wordViewModel = ViewModelProvider(this, wordFactory)
            .get(WordViewModel::class.java)


    }


    private fun observeUser() {
        Log.d("Deneme123", "observeUserAndWord")
        userViewModel.user.observe(viewLifecycleOwner){
            it?.let {
                currentUser = it
                observeWords()

            }
        }
    }

    private fun observeWords() {
        Log.d("Deneme123", "observeWord")
        wordViewModel.getRandomWords(currentUser.engLevel.toString(), currentUser.dailyWordLimit)
        wordViewModel.randomWords.observe(viewLifecycleOwner){
            it?.let {
                wordViewModel.randomWords.removeObservers(viewLifecycleOwner)
                randomWords = it
                currentWord = randomWords[currentPosition]
                updateCardViewUI(currentWord)
                updateDialogUI(currentWord)

            }
        }
    }

    private fun mainActivityWordCardClickListener() {
        Log.d("Deneme123", "mainActivityWordCardClickListener")
        binding.mainActivityWordCard.setOnClickListener {
            dialog.window?.setDimAmount(0.8f)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
            dialog.show()
            updateDialogUI(currentWord)

        }
    }




    private fun setUpDialog() {
        Log.d("Deneme123", "setUpDialog")
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.word_dialog_card)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)

        dialog.window?.decorView?.let { swipeListener(it) }

    }

    private fun swipeListener(view: View?) {
        Log.d("Deneme123", "swipeListener")
        var downX = 0f
        var upX = 0f
        val SWIPE_THRESHOLD = 150 // Min kaydırma mesafesi

        view?.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> { // Kullanıcı dokunduğu an
                    downX = event.x // Dokunulan yerin x koordinatı al ve downX'e kaydet
                    true
                }
                MotionEvent.ACTION_UP -> { // Kullanıcı bıraktığı an
                    upX = event.x // Bırakılan yerin x koordinatı al ve upX'e kaydet
                    val deltaX = downX - upX // Sürükleme mesafesi

                    // Eğer sola doğru kaydırma yeterli mesafede ise
                    if (deltaX > SWIPE_THRESHOLD) {
                        swipedLeft(view)
                        true
                    } else if (deltaX < -SWIPE_THRESHOLD) { // Sağa kaydırma kontrolü
                        swipedRight(view)
                        true
                    } else {
                        v.performClick()
                        false
                    }
                }
                else -> false
            }
        }
    }

    private fun swipedRight(view: View) {
        if (currentPosition > 0) {
            view.animate()
                .translationX(view.width.toFloat())
                .alpha(0f)
                .setDuration(200)
                .withEndAction {
                    // Önceki kelimeyi güncelle
                    currentPosition -= 1
                    currentWord = randomWords[currentPosition]
                    updateCardViewUI(currentWord)
                    updateDialogUI(currentWord)
                    // Yeni kelimeyi içeri kaydır
                    view.translationX = -view.width.toFloat()
                    view.alpha = 1f
                    view.animate()
                        .translationX(0f)
                        .alpha(1f)
                        .setDuration(200)
                        .start()
                }
                .start()
        } else {
            Toast.makeText(requireContext(), "İlk kelime!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun swipedLeft(view: View) {
        if (currentPosition < currentUser.dailyWordLimit - 1) {
            view.animate()
                .translationX(-view.width.toFloat())
                .alpha(0f)
                .setDuration(200)
                .withEndAction {
                    // Sonraki kelimeyi güncelle
                    currentPosition += 1
                    currentWord = randomWords[currentPosition]
                    updateCardViewUI(currentWord)
                    updateDialogUI(currentWord)
                    // Yeni kelimeyi içeri kaydır
                    view.translationX = view.width.toFloat()
                    view.alpha = 0f
                    view.animate()
                        .translationX(0f)
                        .alpha(1f)
                        .setDuration(200)
                        .start()
                }
                .start()
        } else {
            Toast.makeText(requireContext(), "Günlük Kelime Listenin Sonuna Geldin!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun previousWordWithAnimation(view: View) {
        currentPosition -= 1
        currentWord = randomWords[currentPosition]

        view?.let {
            it.translationX = 0f // Başlangıç pozisyonu normalde kalsın
            it.alpha = 1f // Görünür olsun

            it.animate()
                .translationX(it.width.toFloat()) // Sağda kaydır
                .alpha(0f) // Görünmez hale getir
                .setDuration(200) // 200ms animasyon süresi
                .withEndAction {
                    // Yeni kelime yükle
                    updateCardViewUI(currentWord)
                    updateDialogUI(currentWord)
                    it.translationX = -it.width.toFloat() // Çıkmadan önce tekrar ayarla
                    it.alpha = 1f // Görünür hale getir
                    // Sonra yeni kelimeyi içeri kaydır
                    it.animate()
                        .translationX(0f)
                        .alpha(1f)
                        .setDuration(200)
                        .start()
                }
                .start()
        }
    }

    private fun nextWordWithAnimation(view: View) {
        currentPosition += 1
        if (maxPosition<currentPosition){
            maxPosition = currentPosition
        }
        currentWord = randomWords[currentPosition]

        // Yeni kelime yüklendikten sonra dialog'u tekrar içeri kaydır
        view?.let {
            it.translationX = it.width.toFloat() // Başlangıç pozisyonu sağda olsun
            it.alpha = 0f

            it.animate()
                .translationX(0f) // Eski yerine geri getir
                .alpha(1f) // Görünürlüğü geri getir
                .setDuration(200) // 200ms animasyon süresi
                .start()
        }
    }




    private fun updateDialogUI(currentWordInFunc: Word) {
        Log.d("Deneme123", "updateDialogUI")


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
        wordDialogCountText.text = "${currentPosition+1}.Kelime /${currentUser.dailyWordLimit}"


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
        Log.d("Deneme123", "wordDialogSoundButtonClickListener")
        wordDialogSoundButton.setOnClickListener {

        }
    }

    private fun setMeaningText(meaningNumber: Int, meaning: String, meaningTextView: TextView, meaningTypeTextView: TextView, linearLayout: LinearLayout) {
        Log.d("Deneme123", "setMeaningText")

        val parts = meaning.split(" (")
        meaningTextView.text = "${meaningNumber.toString()}. ${parts[0]}"

        if (parts.size > 1 && parts[1].endsWith(")")) {
            meaningTypeTextView.text = "(${parts[1].substringBefore(")")})"
        } else {
            meaningTypeTextView.visibility = View.GONE
        }
    }

    private fun updateCardViewUI(currentWord: Word?) {
        Log.d("Deneme123", "updateCardViewUI")
        binding.mainActivityWordText.text = currentWord?.word
        binding.mainActivityUserNameText.text = "Hoşgeldin ${currentUser.name}"
        binding.mainActivityWordEngLevel.text = englishLevels[currentUser.engLevel]
        binding.mainActivityWordMeaningText.text = currentWord?.meaning

    }



}