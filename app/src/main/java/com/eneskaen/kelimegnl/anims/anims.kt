package com.eneskaen.kelimegnl.anims

import android.content.Context
import android.view.animation.AnimationUtils
import com.eneskaen.kelimegnl.R

class anims(context : Context) {

    val anim_logo = AnimationUtils.loadAnimation(context, R.anim.anim_logo)
    val anim_text = AnimationUtils.loadAnimation(context, R.anim.anim_text)
    val anim_button = AnimationUtils.loadAnimation(context, R.anim.anim_button)

}