package com.eneskaen.kelimegnl.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
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

        binding.getNameTextview.animation = anims(requireContext()).anim_text
        binding.getNameTextview.animation = anims(requireContext()).anim_text
        binding.getNameNextbutton.animation = anims(requireContext()).anim_button

        // Inflate the layout for this fragment
        return binding.root
    }

}