package com.eneskaen.kelimegnl.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.eneskaen.kelimegnl.R
import com.eneskaen.kelimegnl.anims.anims
import com.eneskaen.kelimegnl.database.UserDatabase
import com.eneskaen.kelimegnl.databinding.FragmentGetNameBinding
import com.eneskaen.kelimegnl.model.User
import com.eneskaen.kelimegnl.repository.UserRepository
import com.eneskaen.kelimegnl.viewmodel.UserViewModel
import com.eneskaen.kelimegnl.viewmodel.UserViewModelFactory

class GetNameFragment : Fragment() {

    lateinit var binding : FragmentGetNameBinding
    private lateinit var userViewModel : UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGetNameBinding.inflate(inflater, container, false)

        val userDao = UserDatabase.getDatabase(requireContext()).userDao()
        val repository = UserRepository(userDao)

        // UserViewModelFactory ile ViewModel'i oluşturun
        val factory = UserViewModelFactory(repository)
        userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        binding.getNameTextview.animation = anims.getAnimText(requireContext())
        binding.getNameButtonCardView.animation = anims.getAnimButton(requireContext())
        binding.getNameTextInputLayout.animation = anims.getAnimButton(requireContext())

        binding.getNameButtonCardView.setOnClickListener {

            val name = binding.getNameTextInputLayout.editText?.text.toString().trim()
            if (name.isNotEmpty()){
                userViewModel.insert(User(0, name))
                findNavController().navigate(R.id.action_getNameFragment_to_mainActivity)
            }
            else{
                binding.getNameTextInputLayout.error = "Lütfen bir isim giriniz"
            }
        }
        // Inflate the layout for this fragment
        return binding.root
    }
}
