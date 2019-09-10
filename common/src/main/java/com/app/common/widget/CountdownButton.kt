package com.app.common.widget

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.widget.Button
import com.app.common.CommonConst
import com.app.common.R
import com.app.common.save.Preference

/**
 * 点击倒计时。
 * 退出依然倒计时（isExitTiming 为true） 时 CountdownButton必须设置id，eg: android:id="@+id/btnGetCode"
 * id为倒计时唯一标识，如果id名字一样倒计时时间也一样。
 */
class CountdownButton(context: Context, attrs: AttributeSet) : Button(context, attrs) {
    //总时长,默认60s
    private val total: Int
    //间隔，默认1s
    private val interval: Int
    private val endTxt: String?
    //背景
    private val bgResource: Int
    //倒计时格式
    private val downFormat: String
    //是否退出依然倒计时，默认false
    private val isExitTiming: Boolean
    //倒计时开始时间
    private var countdownTime: Long? by Preference(context, "countdown_time_$id", 0L, CommonConst.PREFERENCE_FILENAME)

    private var onTimeCallBack: ((time: Long, isFinish: Boolean) -> Unit)? = null

    companion object {
        const val DOWNFORMAT_DEFAULT = "%s 秒"
        const val TOTAL_DEFAULT = 60000
        const val INTERVAL_DEFAULT = 1000
    }

    init {
        // 获取自定义属性，并赋值
        val typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CountdownButton)
        total = typedArray.getInteger(R.styleable.CountdownButton_cb_totalTime, TOTAL_DEFAULT)
        interval = typedArray.getInteger(R.styleable.CountdownButton_cb_timeInterval, INTERVAL_DEFAULT)
        endTxt = typedArray.getString(R.styleable.CountdownButton_cb_endTxt)
        downFormat = typedArray.getString(R.styleable.CountdownButton_cb_downFormat)
                ?: DOWNFORMAT_DEFAULT
        bgResource = typedArray.getResourceId(R.styleable.CountdownButton_cb_bg, R.drawable.countdown_button)
        isExitTiming = typedArray.getBoolean(R.styleable.CountdownButton_cb_isExitTiming, false)

        typedArray.recycle()
        initView()
    }

    private fun initView() {
        setBackgroundResource(bgResource)
        setOnClickListener {
            startCountdown()
        }
        if (isExitTiming) {
            val time = total - (System.currentTimeMillis() - (countdownTime ?: 0))
            if (time > 0) {
                val timeCount = TimeCount(time, interval.toLong())
                timeCount.start()
                onTimeCallBack?.invoke(time, false)
            }
        }
    }

    //执行
    fun startCountdown() {
        val time = TimeCount(total.toLong(), interval.toLong())
        time.start()
        onTimeCallBack?.invoke(total.toLong(), false)
        if (isExitTiming) {
            countdownTime = System.currentTimeMillis()
        }
    }

    fun setOnTimeCallback(onTimeCallBack: ((time: Long, isFinish: Boolean) -> Unit)?) {
        this.onTimeCallBack = onTimeCallBack
    }

    //millisInFuture 总时长, countDownInterval 时间间隔
    inner class TimeCount(millisInFuture: Long, private val countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {

        override fun onFinish() {
            text = endTxt
            isEnabled = true
            if (isExitTiming) {
                countdownTime = 0
            }
            onTimeCallBack?.invoke(0, true)
        }

        override fun onTick(millisUntilFinished: Long) {
            isEnabled = false
            val count = millisUntilFinished / countDownInterval
            onTimeCallBack?.invoke(count, false)
            if (count > 0) {
                text = String.format(downFormat, "$count")
            }
        }
    }
}