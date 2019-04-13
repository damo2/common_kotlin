package com.app.common.widget.adapter

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.app.common.R
import com.app.common.adapter.statu.RefreshStatuEnum

import java.lang.ref.WeakReference

/**
 * Created by wr on 2017/4/17.
 */

class FooterView(context: Context) : RelativeLayout(context) {
    private val contextReference: WeakReference<Context>
    private val tv: TextView
    private val progressBar: ProgressBar

    init {
        contextReference = WeakReference(context)
        LayoutInflater.from(context).inflate(R.layout.item_foot, this)
        tv = findViewById<TextView>(R.id.tv_foot_name)
        progressBar = findViewById<ProgressBar>(R.id.progressBar_footer)
    }

    fun setStatu(refreshStatuEnum: RefreshStatuEnum?) {
        when (refreshStatuEnum) {
            RefreshStatuEnum.REFRESH_SUC, RefreshStatuEnum.FIRST_SUC -> {
                visibility = View.VISIBLE
                progressBar.visibility = View.VISIBLE
                tv.text = context.getString(R.string.loading)
            }
            RefreshStatuEnum.LOAD -> {
                visibility = View.VISIBLE
                progressBar.visibility = View.VISIBLE
                tv.text = context.getString(R.string.loading)
            }
            RefreshStatuEnum.LOAD_SUC -> {
                visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                tv.text = context.getString(R.string.loadOver)
                Handler().postDelayed({
                    setStatu(RefreshStatuEnum.LOAD)
                }, 600)
            }
            RefreshStatuEnum.LOAD_FAIL -> {
                visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                tv.text = context.getString(R.string.loadFail)
                Handler().postDelayed({
                    visibility = View.GONE
                }, 600)
            }
            RefreshStatuEnum.LOAD_OVER_ALL -> {
                visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                tv.text = context.getString(R.string.loadOverAll)
            }
            RefreshStatuEnum.NULL -> {
                visibility = View.GONE
            }
            else -> {
                visibility = View.GONE
            }
        }
    }

}
