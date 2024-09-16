package com.eneskaen.kelimegnl.fragments

import android.animation.Animator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.eneskaen.kelimegnl.R
import com.eneskaen.kelimegnl.anims.anims
import com.eneskaen.kelimegnl.databinding.FragmentWelcomeBinding


class WelcomeFragment : Fragment() {
    lateinit var binding : FragmentWelcomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)

        binding.welcomeApplogo.animation = anims.getAnimLogo(requireContext())
        binding.welcomeTextview.animation = anims.getAnimText(requireContext())
        binding.welcomeStartbutton.animation = anims.getAnimButton(requireContext())


        binding.welcomeStartbutton.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_getNameFragment)
        }

        return binding.root
    }


}