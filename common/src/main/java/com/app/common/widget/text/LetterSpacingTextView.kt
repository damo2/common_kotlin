package com.app.common.widget.text


import android.content.Context
import android.graphics.Canvas
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ScaleXSpan
import android.util.AttributeSet
import android.widget.TextView
import com.app.common.R

/**
 * 文字间距
 *  app:letterAlignLength="5"  按照5个字左右对齐
 *  app:letterSpace="2" 2间距(4间距为一个中文字大小)
 *  letterAlignLength、letterSpace 不能同时使用
 */
class LetterSpacingTextView : TextView {

    private var letterSpacing = LetterSpacing.NORMAL
    private var letterAlignLength = LetterSpacing.LENGTH
    private var originalText: CharSequence = ""


    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initView(attrs)
    }

    fun initView(attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.LetterSpacing)
        letterSpacing = a.getFloat(R.styleable.LetterSpacing_letterSpace, LetterSpacing.NORMAL)
        letterAlignLength = a.getFloat(R.styleable.LetterSpacing_letterAlignLength, LetterSpacing.LENGTH)
        a.recycle()
    }

    override fun getLetterSpacing(): Float {
        return letterSpacing
    }

    override fun setLetterSpacing(letterSpacing: Float) {
        this.letterSpacing = letterSpacing
        applyLetterSpacing()
    }

    override fun setText(text: CharSequence, type: TextView.BufferType) {
        originalText = text
        val textlength = originalText.length
        if (letterAlignLength > textlength) {
            letterSpacing = (letterAlignLength - textlength) / (textlength - 1) * 4
        }
        applyLetterSpacing()
    }

    override fun getText(): CharSequence {
        return originalText
    }

    private fun applyLetterSpacing() {
        if (originalText != null) {
            if (originalText.isNotBlank() && letterSpacing != LetterSpacing.NORMAL) {
                val builder = StringBuilder()
                for (i in 0 until originalText.length) {
                    builder.append(originalText[i])
                    if (i + 1 < originalText.length) {
                        builder.append("\u00A0")
                    }
                }
                val finalText = SpannableString(builder.toString())
                if (builder.toString().length > 1) {
                    var i = 1
                    while (i < builder.toString().length) {
                        finalText.setSpan(ScaleXSpan(letterSpacing), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        i += 2
                    }
                }
                super.setText(finalText, TextView.BufferType.SPANNABLE)
            } else {
                super.setText(originalText, TextView.BufferType.SPANNABLE)
            }
        }

    }

    object LetterSpacing {
        val NORMAL = 0f
        val LENGTH = 0f
    }
}