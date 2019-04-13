package com.app.common.utils

import android.view.View
import com.nineoldandroids.animation.AnimatorSet
import com.nineoldandroids.animation.ObjectAnimator
import com.nineoldandroids.view.ViewHelper

public object DialogAnimas {
    val duration = 700L
    private fun startAnima(view: View, block: (animatorSet: AnimatorSet) -> Unit) {
        ViewHelper.setPivotX(view, view.measuredWidth / 2.0f)
        ViewHelper.setPivotY(view, view.measuredHeight / 2.0f)
        AnimatorSet().apply { block(this) }.start()
    }

    /**
     * 浮现模式
     */
    fun startAnimaByFadeIns(view: View) {
        startAnima(view) { animatorSet ->
            animatorSet.playTogether(ObjectAnimator.ofFloat(this, "alpha", 0f, 1f).setDuration(duration))
        }
    }

    /**
     * 正面浮现
     */
    fun startAnimaByFall(view: View) {
        startAnima(view) { animatorSet ->
            animatorSet.playTogether(ObjectAnimator.ofFloat(view, "scaleX", 2f, 1.5f, 1f).setDuration(duration),
                    ObjectAnimator.ofFloat(view, "scaleY", 2f, 1.5f, 1f).setDuration(duration),
                    ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).setDuration(duration * 3 / 2L))
        }
    }

    /**
     * 右侧出现
     */
    fun startAnimaBySlideRight(view: View) {
        startAnima(view) { animatorSet ->
            animatorSet.playTogether(ObjectAnimator.ofFloat(view, "translationX", 300f, 0f).setDuration(duration),
                    ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).setDuration(duration * 3 / 2L))
        }
    }

    /**
     * 左侧出现
     */
    fun startAnimaBySlideLeft(view: View) {
        startAnima(view) { animatorSet ->
            animatorSet.playTogether(ObjectAnimator.ofFloat(view, "translationX", -300f, 0f).setDuration(duration),
                    ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).setDuration(duration * 3 / 2L))
        }
    }

    /**
     * 上侧出现
     */
    fun startAnimaBySlideTop(view: View) {
        startAnima(view) { animatorSet ->
            animatorSet.playTogether(ObjectAnimator.ofFloat(view, "translationY", -300f, 0f).setDuration(duration),
                    ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).setDuration(duration * 3 / 2L))
        }
    }

    /**
     * 下侧出现
     */
    fun startAnimaBySlideBottom(view: View) {
        startAnima(view) { animatorSet ->
            animatorSet.playTogether(ObjectAnimator.ofFloat(view, "translationY", 300f, 0f).setDuration(duration),
                    ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).setDuration(duration * 3 / 2L))
        }
    }

    /**
     * 震动
     */
    fun startAnimaBySlideShake(view: View) {
        startAnima(view) { animatorSet ->
            animatorSet.playTogether(ObjectAnimator.ofFloat(view, "translationX", 0f, .10f, -25f, .26f, 25f, .42f, -25f, .58f, 25f, .74f, -25f, .90f, 1f, 0f).setDuration(duration))
        }
    }

    /**
     * 3D水平
     */
    fun startAnimaBySlideFliph(view: View) {
        startAnima(view) { animatorSet ->
            animatorSet.playTogether(ObjectAnimator.ofFloat(view, "rotationY", -90f, 0f).setDuration(duration))
        }
    }
}

