package com.eneskaen.kelimegnl.anims

import android.content.Context
import android.view.animation.AnimationUtils
import com.eneskaen.kelimegnl.R

abstract class anims{

    companion object{
        fun getAnimText(context : Context) = AnimationUtils.loadAnimation(context, R.anim.anim_text)
        fun getAnimButton(context : Context) = AnimationUtils.loadAnimation(context, R.anim.anim_button)
    }


}