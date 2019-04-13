package com.app.common.widget.wheelview

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.app.common.R

class WheelViewOne : ScrollView {

    private lateinit var views: LinearLayout
    private var items: MutableList<String> = arrayListOf()
    internal var offset = OFF_SET_DEFAULT // 偏移量（需要在最前面和最后面补全）
    internal var displayItemCount = SHOW_COUNT_DEFAULT // 每页显示的数量
    internal var selectedIndex = SELETCT_ITEM_DEFAULT//默认显示

    //默认样式
    private val colorDefault = R.color.common_textgray
    private val colorSizeDefault = 14
    //选中样式
    private val colorSelect = R.color.common_blank
    private val colorSizeSelect = 14

    //
    private val colorLine = R.color.common_lineColor//线颜色

    private var initialY: Int = 0
    private var scrollerTask: Runnable? = null

    internal var itemHeight = 0

    /**
     * 获取选中区域的边界
     */
    internal var selectedAreaBorder: IntArray? = null


    private var scrollDirection = -1

    internal var paint: Paint? = null
    internal var viewWidth: Int = 0

    val seletedItem: String
        get() = items[selectedIndex]

    val seletedIndex: Int
        get() = selectedIndex - offset

    //    private var onWheelViewListener: OnWheelViewListener? = null
    private var mOnSelectCallBack: ((index: Int, value: String) -> Unit)? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }


    private fun init() {
        Log.d(TAG, "parent: " + this.parent)
        this.isVerticalScrollBarEnabled = false
        this.overScrollMode = View.OVER_SCROLL_NEVER
        views = LinearLayout(context)
        views.orientation = LinearLayout.VERTICAL
        this.addView(views)

        scrollerTask = Runnable {
            val newY = scrollY
            if (initialY - newY == 0) { // stopped
                val remainder = initialY % itemHeight
                val divided = initialY / itemHeight
                if (remainder == 0) {
                    selectedIndex = divided + offset

                    onSelectedCallBack()
                } else {
                    if (remainder > itemHeight / 2) {
                        this@WheelViewOne.post {
                            this@WheelViewOne.smoothScrollTo(0, initialY - remainder + itemHeight)
                            selectedIndex = divided + offset + 1
                            onSelectedCallBack()
                        }
                    } else {
                        this@WheelViewOne.post {
                            this@WheelViewOne.smoothScrollTo(0, initialY - remainder)
                            selectedIndex = divided + offset
                            onSelectedCallBack()
                        }
                    }
                }
            } else {
                initialY = scrollY
                this@WheelViewOne.postDelayed(scrollerTask, newCheck.toLong())
            }
        }
    }


    //补全行数
    fun setOffset(offset: Int) {
        this.offset = offset
        selectedIndex = offset
        displayItemCount = offset * 2 + 1
    }

    private fun getItems(): List<String> {
        return items
    }

    fun setItems(list: List<String>) {
        items.clear()
        items.addAll(list)

        //         前面和后面补全
        for (i in 0 until offset) {
            items.add(0, "")
            items.add("")
        }
        initData()
        refreshItemView(0)
    }

    fun startScrollerTask() {

        initialY = scrollY
        this.postDelayed(scrollerTask, newCheck.toLong())
    }

    private fun initData() {
        displayItemCount = offset * 2 + 1

        views.removeAllViews()
        for (item in items) {
            views.addView(createView(item))
        }
    }

    private fun createView(item: String): TextView {
        val tv = TextView(context)
        tv.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        tv.setSingleLine(true)
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, colorSizeDefault.toFloat())
        tv.text = item
        tv.gravity = Gravity.CENTER
        tv.setTextColor(context!!.resources.getColor(colorDefault))
        val padding = dip2px(8f)
        tv.setPadding(padding, padding, padding, padding)
        if (0 == itemHeight) {
            itemHeight = getViewMeasuredHeight(tv)
            Log.d(TAG, "itemHeight: " + itemHeight)
            views.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, itemHeight * displayItemCount, Gravity.CENTER_HORIZONTAL)
            views.gravity = Gravity.CENTER

            this.layoutParams.height = itemHeight * displayItemCount
        }
        return tv
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        refreshItemView(t)

        if (t > oldt) {
            //            Log.d(TAG, "向下滚动");
            scrollDirection = SCROLL_DIRECTION_DOWN
        } else {
            //            Log.d(TAG, "向上滚动");
            scrollDirection = SCROLL_DIRECTION_UP
        }
    }

    private fun refreshItemView(y: Int) {
        var position = y / itemHeight + offset
        val remainder = y % itemHeight
        val divided = y / itemHeight

        if (remainder == 0) {
            position = divided + offset
        } else {
            if (remainder > itemHeight / 2) {
                position = divided + offset + 1
            }
        }

        val childSize = views.childCount
        for (i in 0 until childSize) {
            val itemView = views.getChildAt(i) as TextView ?: return
            if (position == i) {
                itemView.textSize = colorSizeSelect.toFloat()
                itemView.setTextColor(context!!.resources.getColor(colorSelect))
            } else {
                itemView.textSize = colorSizeDefault.toFloat()
                itemView.setTextColor(context!!.resources.getColor(colorDefault))
            }
        }
    }

    private fun obtainSelectedAreaBorder(): IntArray {
        if (null == selectedAreaBorder) {
            selectedAreaBorder = IntArray(2)
            selectedAreaBorder!![0] = itemHeight * offset
            selectedAreaBorder!![1] = itemHeight * (offset + 1)
        }
        return selectedAreaBorder!!
    }

    override fun setBackgroundDrawable(background: Drawable?) {
        var background = background

        if (viewWidth == 0) {
            //            viewWidth = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
            viewWidth = views.width
            Log.d(TAG, "viewWidth: " + viewWidth)
        }

        if (null == paint) {
            paint = Paint()
            paint!!.color = context!!.resources.getColor(colorLine)
            paint!!.strokeWidth = dip2px(1f).toFloat()
        }

        background = object : Drawable() {
            override fun draw(canvas: Canvas) {
                //                canvas.drawLine(viewWidth * 1 / 6, obtainSelectedAreaBorder()[0], viewWidth * 5 / 6, obtainSelectedAreaBorder()[0], paint);
                //                canvas.drawLine(viewWidth * 1 / 6, obtainSelectedAreaBorder()[1], viewWidth * 5 / 6, obtainSelectedAreaBorder()[1], paint);

                canvas.drawLine(0f, obtainSelectedAreaBorder()[0].toFloat(), viewWidth.toFloat(), obtainSelectedAreaBorder()[0].toFloat(), paint!!)
                canvas.drawLine(0f, obtainSelectedAreaBorder()[1].toFloat(), viewWidth.toFloat(), obtainSelectedAreaBorder()[1].toFloat(), paint!!)
            }

            override fun setAlpha(alpha: Int) {

            }

            override fun setColorFilter(cf: ColorFilter?) {

            }

            override fun getOpacity(): Int {
                return PixelFormat.UNKNOWN
            }
        }
        super.setBackgroundDrawable(background)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.d(TAG, "w: $w, h: $h, oldw: $oldw, oldh: $oldh")
        viewWidth = w
        setBackgroundDrawable(null)
    }

    /**
     * 选中回调
     */
    private fun onSelectedCallBack() {
        mOnSelectCallBack?.let { it(selectedIndex - offset, items[selectedIndex]) }
    }

    fun setOnSelectedCallBack(onSelectCallBack: (index: Int, value: String) -> Unit) {
        mOnSelectCallBack = onSelectCallBack
//        // 默认初始不滑动时执行一次回调
//        if (selectedIndex < items.size) {
//            mOnSelectCallBack?.let { it(selectedIndex - offset, items[selectedIndex]) }
//        }
    }
    fun setSeletion(position: Int) {
        selectedIndex = position + offset
        this.post { this@WheelViewOne.smoothScrollTo(0, position * itemHeight) }

    }


    override fun fling(velocityY: Int) {
        super.fling(velocityY / 3)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_UP) {

            startScrollerTask()
        }
        return super.onTouchEvent(ev)
    }


    private fun dip2px(dpValue: Float): Int {
        val scale = context!!.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    private fun getViewMeasuredHeight(view: View): Int {
        val width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2, View.MeasureSpec.AT_MOST)
        view.measure(width, expandSpec)
        return view.measuredHeight
    }

    open class OnWheelViewListener {
        open fun onSelected(selectedIndex: Int, item: String) {}
    }

    companion object {
        val TAG = WheelViewOne::class.java.simpleName
        val OFF_SET_DEFAULT = 1
        val SHOW_COUNT_DEFAULT = 3
        val SELETCT_ITEM_DEFAULT = 1
        private val newCheck = 50
        private val SCROLL_DIRECTION_UP = 0
        private val SCROLL_DIRECTION_DOWN = 1
    }
}