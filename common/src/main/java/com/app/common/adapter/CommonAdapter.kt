package com.app.common.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import com.app.common.adapter.util.ItemViewDelegate
import com.app.common.adapter.util.ViewHolder

/**
 * https://github.com/hongyangAndroid/baseAdapter
 * Created by zhy on 16/4/9.
 */
class CommonAdapter<T>(protected var context: Context, protected var mLayoutId: Int, data: List<T>?,
                       holderConvert: (holder: ViewHolder, data: T, position: Int, payloads: List<Any>?) -> Unit,
                       onItemClick: ((view: View, holder: RecyclerView.ViewHolder, position: Int) -> Unit)? = null,
                       onItemLongClick: ((view: View, holder: RecyclerView.ViewHolder, position: Int) -> Boolean?)? = null

) : MultiItemTypeAdapter<T>(context, data,onItemClick,onItemLongClick) {
    protected var mInflater: LayoutInflater

    init {
        mInflater = LayoutInflater.from(context)

        addItemViewDelegate(object : ItemViewDelegate<T> {
            override fun getItemViewLayoutId(): Int {
                return mLayoutId
            }

            override fun isForViewType(item: T, position: Int): Boolean {
                return true
            }

            override fun convert(holder: ViewHolder, item: T, position: Int, payloads: List<Any>?) {
                holderConvert(holder, item, position, payloads)
            }
        })
    }


    fun setData(data: List<T>) {
        this.datas = data
    }
}
