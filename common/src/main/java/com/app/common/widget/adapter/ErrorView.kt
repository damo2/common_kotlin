package com.app.common.widget.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.app.common.R
import com.app.common.adapter.statu.RefreshStatuEnum

import java.lang.ref.WeakReference

/**
 * Created by wr on 2017/4/17.
 */

class ErrorView(context: Context, parent: ViewGroup? = null) : RelativeLayout(context) {
    private val contextReference: WeakReference<Context>
    private val tv: TextView

    init {
        contextReference = WeakReference(context)
        LayoutInflater.from(context).inflate(R.layout.item_error, if (parent == null) this else parent)
        tv = findViewById<TextView>(R.id.tv_error_tip)
    }

    fun setStatu(refreshStatuEnum: RefreshStatuEnum?) {
        if (refreshStatuEnum == null) {
            visibility = View.GONE
            return
        }
        when (refreshStatuEnum) {
            RefreshStatuEnum.FIRST_FAIL -> {
                visibility = View.VISIBLE
                tv.text = context.getString(R.string.loadFailToRefresh)
            }
            RefreshStatuEnum.NULL -> {
                visibility = View.VISIBLE
                tv.text = context.getString(R.string.loadNull)
            }
            else -> visibility = View.GONE
        }
    }

}
