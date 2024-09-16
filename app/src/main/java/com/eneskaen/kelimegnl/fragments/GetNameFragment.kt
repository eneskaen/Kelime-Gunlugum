package com.eneskaen.kelimegnl.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.eneskaen.kelimegnl.R
import com.eneskaen.kelimegnl.anims.anims
import com.eneskaen.kelimegnl.databinding.FragmentGetNameBinding

class GetNameFragment : Fragment() {

    lateinit var binding : FragmentGetNameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGetNameBinding.inflate(inflater, container, false)

        binding.getNameTextview.animation = anims.getAnimText(requireContext())
        binding.getNameNextbutton.animation = anims.getAnimButton(requireContext())
        binding.getNameInputlayout.animation = anims.getAnimButton(requireContext())

        binding.getNameNextbutton.setOnClickListener {

            val name = binding.getNameInputlayout.editText?.text.toString().trim()
            if (name.isNotEmpty()){

                findNavController().navigate(R.id.action_getNameFragment_to_mainActivity)
            }
            else{
                binding.getNameInputlayout.error = "Lütfen bir isim giriniz"
            }

        }
        // Inflate the layout for this fragment
        return binding.root
    }

}