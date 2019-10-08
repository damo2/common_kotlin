package com.app.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.app.common.R
import com.app.common.bean.ThreeBean
import com.app.common.extensions.getNumExt
import com.app.common.widget.wheelview.WheelView
import kotlinx.android.synthetic.main.dialog_select_date.view.*
import java.util.*

/**
 * Created by wr
 * Date: 2018/9/1  15:52
 * describe:
 * 修改数据后调用 resetWheelView()
 */
class SelectDateView(context: Context, attrs: AttributeSet?, defStyle: Int) : LinearLayout(context, attrs, defStyle) {
    companion object {
        /**年月日*/
        val TYPE_YEAR_MONTH_DAY = 0
        /**年月*/
        val TYPE_YEAR_MONTH = 1
    }

    private var mYearList = ArrayList<Int>()
    private var mMonthList = ArrayList<Int>()
    private var mDayList = ArrayList<Int>()

    private val startDate: ThreeBean<Int, Int, Int> = ThreeBean(1990, 1, 1)
    private val endDate: ThreeBean<Int, Int, Int> = ThreeBean(2030, 12, 30)
    private val selectDate: ThreeBean<Int, Int, Int> = ThreeBean(0, 0, 0)

    private var selectPosition: ThreeBean<Int, Int, Int> = ThreeBean(0, 0, 0)

    var onEndSelectCallback: ((selectDate: ThreeBean<Int, Int, Int>) -> Unit)? = null


    var selectType = TYPE_YEAR_MONTH_DAY
    var range = SelectDateRange.ALL
    var yearOutInt = 1//可选最大年超过当前多少年

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    init {
        LayoutInflater.from(context).inflate(R.layout.dialog_select_date, this)
        initData()
        initValue()
    }

    private fun initData() {
        val calendar = Calendar.getInstance()
        selectDate.first = calendar.get(Calendar.YEAR)
        selectDate.second = calendar.get(Calendar.MONTH) + 1
        selectDate.third = calendar.get(Calendar.DATE)
        refreshStartEndDate()
    }

    private fun initValue() {
        refreshDataYear()
        refreshDataMonth()
        refreshDataDay()
        setWheelView()
    }

    private fun refreshStartEndDate() {
        val calendar = Calendar.getInstance()
        when (range) {
            SelectDateRange.BEFORE -> {
                endDate.first = calendar.get(Calendar.YEAR)
                endDate.second = calendar.get(Calendar.MONTH) + 1
                endDate.third = calendar.get(Calendar.DATE)
            }
            SelectDateRange.AFTER -> {
                startDate.first = calendar.get(Calendar.YEAR)
                startDate.second = calendar.get(Calendar.MONTH) + 1
                startDate.third = calendar.get(Calendar.DATE)
            }
            else -> {
                endDate.first = calendar.get(Calendar.YEAR) + yearOutInt
            }
        }
    }

    private fun refreshDataYear() {
        mYearList.clear()
        mYearList.addAll(startDate.first..endDate.first)
        selectPosition.first = mYearList.indexOf(selectDate.first)
    }

    private fun refreshDataMonth() {
        mMonthList.clear()
        mMonthList.addAll(getIndexStartMonth()..getIndexEndMonth())
        selectPosition.second = mMonthList.indexOf(getSelectMonth())
    }

    private fun refreshDataDay() {
        mDayList.clear()
        mDayList.addAll(getIndexStartDay()..getIndexEndDay())
        selectPosition.third = mDayList.indexOf(getSelectDay())
    }


    private fun setWheelView() {
        if (selectType == TYPE_YEAR_MONTH) {
            wheelViewDay.visibility = View.GONE
        }
        wheelViewYear.apply {
            setData(mYearList.map { it.toString() + "年" })
            itemNumber = 5
            setDefault(selectPosition.first)
            setOnSelectListener(object : WheelView.OnSelectListener {
                override fun endSelect(id: Int, text: String?) {
                    text?.getNumExt()?.let {
                        if (selectDate.first != it) {
                            selectDate.first = it
                            refreshMonth()
                            refreshDay()
                            onEndSelectCallback?.invoke(selectDate)
                        }
                    }
                }

                override fun selecting(id: Int, text: String?) {
                }
            })
            performClick()
        }
        wheelViewMonth.apply {
            setData(mMonthList.map { it.toString() + "月" })
            itemNumber = 5
            setDefault(selectPosition.second)
            setOnSelectListener(object : WheelView.OnSelectListener {
                override fun endSelect(id: Int, text: String?) {
                    text?.getNumExt()?.let {
                        if (selectDate.second != it) {
                            selectDate.second = it
                            refreshDay()
                            onEndSelectCallback?.invoke(selectDate)
                        }
                    }
                }

                override fun selecting(id: Int, text: String?) {
                }
            })

        }
        wheelViewDay.apply {
            setData(mDayList.map { it.toString() + "日" })
            itemNumber = 5
            setDefault(selectPosition.third)
            setOnSelectListener(object : WheelView.OnSelectListener {
                override fun endSelect(id: Int, text: String?) {
                    text?.getNumExt()?.let {
                        if (selectDate.third != it) {
                            selectDate.third = it
                            onEndSelectCallback?.invoke(selectDate)
                        }
                    }
                }

                override fun selecting(id: Int, text: String?) {
                }
            })
        }
    }

    private fun refreshYear() {
        refreshDataYear()
        try {
            wheelViewYear.refreshData(mYearList.map { it.toString() + "年" })
            wheelViewYear.setDefault(selectPosition.first)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun refreshMonth() {
        refreshDataMonth()
        try {
            wheelViewMonth.refreshData(mMonthList.map { it.toString() + "月" })
            wheelViewMonth.setDefault(selectPosition.second)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun refreshDay() {
        if (selectType == TYPE_YEAR_MONTH_DAY) {
            refreshDataDay()
            //todo refreshDay java.lang.NullPointerException: Attempt to invoke virtual method 'void com.app.common.widget.wheelview.WheelView.refreshData(java.util.ArrayList)' on a null object reference
            try {
                wheelViewDay.refreshData(mDayList.map { it.toString() + "日" })
                wheelViewDay.setDefault(selectPosition.third)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }


    private fun getIndexStartMonth(): Int {
        val isStartYear = selectDate.first == startDate.first
        return if (isStartYear) startDate.second else 1
    }

    private fun getIndexEndMonth(): Int {
        val isEndYear = selectDate.first == endDate.first
        return if (isEndYear) endDate.second else 12
    }

    private fun getIndexStartDay(): Int {
        val isStartMonth = selectDate.first == startDate.first && selectDate.second == startDate.second
        return if (isStartMonth) startDate.third else 1
    }

    private fun getIndexEndDay(): Int {
        val isEndMonth = selectDate.first == endDate.first && selectDate.second == endDate.second
        return if (isEndMonth) selectDate.third else Calendar.getInstance().apply {
            set(Calendar.YEAR, selectDate.first)
            set(Calendar.MONTH, selectDate.second - 1)
        }.getActualMaximum(Calendar.DATE)
    }


    private fun getSelectMonth(): Int {
        val isLessStartMonth = selectDate.first == startDate.first && selectDate.second < startDate.second
        val isGreaterEndMonth = selectDate.first == endDate.first && selectDate.second > endDate.second
        return if (isLessStartMonth) {
            startDate.second
        } else if (isGreaterEndMonth) {
            endDate.second
        } else {
            selectDate.second
        }
    }

    private fun getSelectDay(): Int {
        val isLessStartDay = selectDate.first == startDate.first && selectDate.second == startDate.second && selectDate.third < startDate.third
        val isGreaterEndDay = selectDate.first == endDate.first && selectDate.second == endDate.second && selectDate.third > endDate.third
        return if (isLessStartDay) {
            startDate.third
        } else if (isGreaterEndDay) {
            endDate.third
        } else {
            selectDate.third
        }
    }


    //设置默认值 如 2019-10-21
    fun setDefault(dateStr: String?) {
        dateStr?.let {
            val dataIntList = dateStr.split("-", "/", " ").map { it.toInt() }
            dataIntList.getOrNull(0)?.let {
                selectDate.first = it
            }
            dataIntList.getOrNull(1)?.let {
                selectDate.second = it
            }
            dataIntList.getOrNull(2)?.let {
                selectDate.third = it
            }
            resetWheelView()
        }
    }

    fun setDate(startDate: ThreeBean<Int, Int, Int>? = null, endDate: ThreeBean<Int, Int, Int>? = null, selectDate: ThreeBean<Int, Int, Int>? = null) {
        startDate?.let {
            this.startDate.first = it.first
            this.startDate.second = it.second
            this.startDate.third = it.third
        }
        endDate?.let {
            this.endDate.first = it.first
            this.endDate.second = it.second
            this.endDate.third = it.third
        }
        selectDate?.let {
            this.selectDate.first = it.first
            this.selectDate.second = it.second
            this.selectDate.third = it.third
        }
        resetWheelView()
    }

    //设置开始结束时间后，重新刷新值
    fun resetWheelView() {
        refreshStartEndDate()
        refreshDataYear()
        refreshDataMonth()
        refreshDataDay()
        refreshYear()
        refreshMonth()
        refreshDay()
    }

    fun getStartTime() = startDate
    fun getEndTime() = endDate
    //获取选择的值
    fun getSelectDate(): ThreeBean<Int, Int, Int> {
        try {
            wheelViewYear.stopNestedScroll()
            wheelViewMonth.stopNestedScroll()
            wheelViewDay.stopNestedScroll()
        } catch (e: Exception) {
        }
        return selectDate
    }

}

object SelectDateRange {
    const val ALL = 0
    const val BEFORE = 1 //选择之前的时间
    const val AFTER = 2//选择之后的时间
}