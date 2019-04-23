package com.app.common.adapter.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.recyclerview.widget.RecyclerView
import android.text.util.Linkify
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.*
import com.app.common.extensions.setOnClickExtNoFast
import com.bumptech.glide.Glide

class ViewHolder(private val mContext: Context, val convertView: View) : RecyclerView.ViewHolder(convertView) {
    private val mViews: SparseArray<View> = SparseArray()

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    fun <T : View> getView(viewId: Int): T {
        var view: View? = mViews.get(viewId)
        if (view == null) {
            view = convertView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as T
    }


    /****以下为辅助方法 */

    /**
     * 设置TextView的值
     *
     * @param viewId
     * @param text
     * @return
     */
    fun setText(viewId: Int, text: String?) {
        getView<TextView>(viewId).text = text ?: ""
    }

    fun setImageResource(viewId: Int, resId: Int) {
        getView<ImageView>(viewId).setImageResource(resId)
    }

    fun setImageBitmap(viewId: Int, bitmap: Bitmap) {
        getView<ImageView>(viewId).setImageBitmap(bitmap)
    }

    fun setImageDrawable(viewId: Int, drawable: Drawable) {
        getView<ImageView>(viewId).setImageDrawable(drawable)
    }

    fun setImageLoad(viewId: Int, imgUrl: String) {
        val view = getView<ImageView>(viewId)
        Glide.with(mContext).load(imgUrl).into(view)
    }

    fun setBackgroundColor(viewId: Int, color: Int) {
        getView<View>(viewId).setBackgroundColor(color)
    }

    fun setBackgroundRes(viewId: Int, backgroundRes: Int) {
        getView<View>(viewId).setBackgroundResource(backgroundRes)
    }

    fun setTextColorRes(viewId: Int, textColorRes: Int) {
        getView<TextView>(viewId).setTextColor(mContext.resources.getColor(textColorRes))
    }

    @SuppressLint("NewApi")
    fun setAlpha(viewId: Int, value: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView<View>(viewId).alpha = value
        } else {
            // Pre-honeycomb hack to set Alpha value
            val alpha = AlphaAnimation(value, value)
            alpha.duration = 0
            alpha.fillAfter = true
            getView<View>(viewId).startAnimation(alpha)
        }
    }

    fun setVisible(viewId: Int, visible: Boolean) {
        val view = getView<View>(viewId)
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun linkify(viewId: Int) {
        val view = getView<TextView>(viewId)
        Linkify.addLinks(view, Linkify.ALL)
    }

    fun setTypeface(typeface: Typeface, vararg viewIds: Int) {
        for (viewId in viewIds) {
            val view = getView<TextView>(viewId)
            view.typeface = typeface
            view.paintFlags = view.paintFlags or Paint.SUBPIXEL_TEXT_FLAG
        }
    }

    fun setProgress(viewId: Int, progress: Int) {
        val view = getView<ProgressBar>(viewId)
        view.progress = progress
    }

    fun setProgress(viewId: Int, progress: Int, max: Int) {
        val view = getView<ProgressBar>(viewId)
        view.max = max
        view.progress = progress
    }

    fun setMax(viewId: Int, max: Int) {
        val view = getView<ProgressBar>(viewId)
        view.max = max
    }

    fun setRating(viewId: Int, rating: Float) {
        val view = getView<RatingBar>(viewId)
        view.rating = rating
    }

    fun setRating(viewId: Int, rating: Float, max: Int) {
        val view = getView<RatingBar>(viewId)
        view.max = max
        view.rating = rating
    }

    fun setTag(viewId: Int, tag: Any) {
        val view = getView<View>(viewId)
        view.tag = tag
    }

    fun setTag(viewId: Int, key: Int, tag: Any) {
        val view = getView<View>(viewId)
        view.setTag(key, tag)
    }

    fun setChecked(viewId: Int, checked: Boolean) {
        val view = getView<View>(viewId) as Checkable
        view.isChecked = checked
    }

    /**
     * 关于事件的
     */
    fun setOnClickListener(viewId: Int,
                           block: (v: View) -> Unit) {
        val view = getView<View>(viewId)
        view.setOnClickExtNoFast { block(it) }
    }

    fun setOnTouchListener(viewId: Int,
                           listener: View.OnTouchListener) {
        val view = getView<View>(viewId)
        view.setOnTouchListener(listener)
    }

    fun setOnLongClickListener(viewId: Int,
                               listener: View.OnLongClickListener) {
        val view = getView<View>(viewId)
        view.setOnLongClickListener(listener)
    }

    companion object {

        fun createViewHolder(context: Context, itemView: View): ViewHolder {
            return ViewHolder(context, itemView)
        }

        fun createViewHolder(context: Context,
                             parent: ViewGroup, layoutId: Int): ViewHolder {
            val itemView = LayoutInflater.from(context).inflate(layoutId, parent, false)
            return ViewHolder(context, itemView)
        }
    }


}
