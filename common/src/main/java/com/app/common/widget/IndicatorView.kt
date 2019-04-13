package com.app.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.app.common.R
import com.app.common.extensions.dp2px
import java.util.*

/**
 * Description:自定义指示器
 */
class IndicatorView @JvmOverloads constructor(var mContext: Context, attrs: AttributeSet? = null) : LinearLayout(mContext, attrs) {

    private var dotViews: MutableList<ImageView> = ArrayList()
    //指示器宽高
    var indicatorSize = 8
    //间距
    var space = 6

    private val viewLayoutParams: LinearLayout.LayoutParams
        get() = LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)

    private val imageLayoutParams: RelativeLayout.LayoutParams
        get() {
            val layoutParams = RelativeLayout.LayoutParams(mContext.dp2px(indicatorSize), mContext.dp2px(indicatorSize))
            layoutParams.setMargins(space, 0, space, 0)
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
            return layoutParams
        }

    init {
        gravity = Gravity.CENTER_HORIZONTAL
    }


    fun init(count: Int) {
        dotViews = ArrayList()
        for (i in 0 until count) {
            val rl = RelativeLayout(mContext)
            val params = viewLayoutParams
            val layoutParams = imageLayoutParams
            val imageView = ImageView(mContext)
            if (i == 0) {
                imageView.setImageResource(R.drawable.common_shape_bg_indicator_point_select)
            } else {
                imageView.setImageResource(R.drawable.common_shape_bg_indicator_point_nomal)
            }
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            rl.addView(imageView, layoutParams)
            this.addView(rl, params)
            dotViews.add(imageView)
        }
    }


    fun updateIndicator(count: Int) {
        for (i in dotViews.indices) {
            if (i >= count) {
                dotViews[i].visibility = View.GONE
                (dotViews[i].parent as View).visibility = View.GONE
            } else {
                dotViews[i].visibility = View.VISIBLE
                (dotViews[i].parent as View).visibility = View.VISIBLE
            }
        }
        if (count > dotViews.size) {
            val diff = count - dotViews.size
            for (i in 0 until diff) {
                val rl = RelativeLayout(mContext)
                val params = viewLayoutParams
                val layoutParams = imageLayoutParams
                val imageView = ImageView(mContext)
                imageView.setImageResource(R.drawable.common_shape_bg_indicator_point_nomal)
                rl.addView(imageView, layoutParams)
                this.addView(rl, params)
                dotViews.add(imageView)
                rl.visibility = View.GONE
                imageView.visibility = View.GONE
            }
        }
    }


    fun selectTo(position: Int) {
        for (iv in dotViews) {
            iv.setImageResource(R.drawable.common_shape_bg_indicator_point_nomal)
        }
        dotViews[position].setImageResource(R.drawable.common_shape_bg_indicator_point_select)
    }


    fun selectTo(startPosition: Int, targetPostion: Int) {
        val startView = dotViews[startPosition]
        val targetView = dotViews[targetPostion]
        startView.setImageResource(R.drawable.common_shape_bg_indicator_point_nomal)
        targetView.setImageResource(R.drawable.common_shape_bg_indicator_point_select)
    }
}
