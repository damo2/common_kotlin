package com.app.common.widget

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.widget.Button
import com.app.common.R


/**
 * Created by wr
 * Date: 2019/6/3  13:23
 * mail: 1902065822@qq.com
 * describe:
 */
class CountdownButton(context: Context, attrs: AttributeSet) : Button(context, attrs) {
    private val total: Int
    private val interval: Int
    private val endTxt: String?
    private var bgResource: Int = R.drawable.countdown_button
    private var downFormat = "%s 秒"

    init {
        // 获取自定义属性，并赋值
        val typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CountdownButton)
        total = typedArray.getInteger(R.styleable.CountdownButton_cb_totalTime, 60000)
        interval = typedArray.getInteger(R.styleable.CountdownButton_cb_timeInterval, 1000)
        endTxt = typedArray.getString(R.styleable.CountdownButton_cb_endTxt)
        downFormat = typedArray.getString(R.styleable.CountdownButton_cb_downFormat)
        bgResource = typedArray.getResourceId(R.styleable.CountdownButton_cb_bg, R.drawable.countdown_button)
        typedArray.recycle()
        initView()
    }

    private fun initView() {
        setBackgroundResource(bgResource)
        setOnClickListener {
            startCountdown()
        }
    }

    //执行
    fun startCountdown() {
        val time = TimeCount(total.toLong(), interval.toLong())
        time.start()
    }

    //millisInFuture 总时长, countDownInterval 时间间隔
    inner class TimeCount(millisInFuture: Long, private val countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {

        override fun onFinish() {
            text = endTxt
            isEnabled = true
        }

        override fun onTick(millisUntilFinished: Long) {
            isEnabled = false
            text = String.format(downFormat, (millisUntilFinished / countDownInterval).toString())
        }
    }
}