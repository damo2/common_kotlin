package com.app.common.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.app.common.extensions.sp2px


class SideBar @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val paint = Paint()

    private var choose = -1

    private var onChooseLetterChangedListener: OnChooseLetterChangedListener? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        setBackgroundColor(Color.parseColor("#FFEEEEEE"))
        val height = height
        val width = width
        //平均每个字母占的高度
        val singleHeight = height / letters.size
        for (i in letters.indices) {
            paint.isAntiAlias = true
            paint.textSize = context.sp2px(15f)
            if (i == choose) {
                paint.color = Color.parseColor("#FFFFFF")
                paint.isFakeBoldText = true
            }else{
                paint.color = Color.parseColor("#5cc2d0")
            }
            val x = width / 2 - paint.measureText(letters[i]) / 2
            val y = (singleHeight * i + singleHeight).toFloat()
            canvas.drawText(letters[i], x, y, paint)
            paint.reset()
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        val y = event.y
        val oldChoose = choose
        val c = (y / height * letters.size).toInt()
        when (action) {
            MotionEvent.ACTION_DOWN -> if (oldChoose != c && onChooseLetterChangedListener != null) {
                if (c > -1 && c < letters.size) {
                    //获取触摸位置的字符
                    onChooseLetterChangedListener!!.onChooseLetter(letters[c])
                    choose = c
                    invalidate()
                }
            }
            MotionEvent.ACTION_MOVE -> if (oldChoose != c && onChooseLetterChangedListener != null) {
                if (c > -1 && c < letters.size) {
                    //获取触摸位置的字符
                    onChooseLetterChangedListener!!.onChooseLetter(letters[c])
                    choose = c
                    invalidate()
                }
            }
            MotionEvent.ACTION_UP -> {
                choose = -1
                if (onChooseLetterChangedListener != null) {
                    //手指离开
                    onChooseLetterChangedListener!!.onNoChooseLetter()
                }
                invalidate()
            }
        }
        return true
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return super.onTouchEvent(event)
    }

    fun setOnTouchingLetterChangedListener(onChooseLetterChangedListener: OnChooseLetterChangedListener) {
        this.onChooseLetterChangedListener = onChooseLetterChangedListener
    }

    interface OnChooseLetterChangedListener {
        /**
         * 滑动时
         */
        fun onChooseLetter(s: String)

        /**
         * 手指离开
         */
        fun onNoChooseLetter()
    }

    companion object {

        var letters = arrayOf("#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")
    }
}