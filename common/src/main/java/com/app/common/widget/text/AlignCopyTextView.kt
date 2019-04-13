package com.app.common.widget.text

import android.content.ClipboardManager
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Paint
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import com.app.common.R

import java.lang.reflect.Method
import java.util.ArrayList


/**
 * 对齐的TextView
 * https://github.com/androiddevelop/AlignTextView
 *
 * 如果需要支持android默认的选择复制，请在xml中加入以下代码: android:textIsSelectable="true"
 * CBAlignTextView中增加了以下方法获取TextView的文本内容，请不要使用getText()获取getRealText()
 * 在设置CBAlignTextView文本前(setText),调用以下方法:setPunctuationConvert(boolean convert)
 * 如果需要多次设置文本，或者复用组件(如RecyclerView中)，在后面每次设置文本前，请调用以下方法:reset()
 *
 *
 * 为了能够使TextView能够很好的进行排版，同时考虑到原生TextView中以word进行分割排版，
 * 那么我们可以将要换行的地方进行添加空格处理，这样就可以在合适的位置换行，同时也不会
 * 打乱原生的TextView的排版换行选择复制等问题。为了能够使右端尽可能的对齐，将右侧多出的空隙
 * 尽可能的分配到该行的标点后面。达到两段对齐的效果。
 *
 *
 *
 * 重新设置文本前，请调用reset()进行状态重置。
 *
 * Created by yuedong.lyd on 6/28/15.
 */
class AlignCopyTextView : TextView {
    private val addCharPosition = ArrayList<Int>()  //增加空格的位置
    /**
     * 获取真正的text
     *
     * @return 返回text
     */
    var realText: CharSequence? = ""
        private set //旧文本，本来应该显示的文本
    private var newText: CharSequence = "" //新文本，真正显示的文本
    private var inProcess = false //旧文本是否已经处理为新文本
    private var isAddPadding = false //是否添加过边距
    private var isConvert = false //是否转换标点符号

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.CBAlignTextView)
        isConvert = ta.getBoolean(R.styleable.CBAlignTextView_punctuationConvert, false)
        ta.recycle()

        //判断使用xml中是用android:text
        val tsa = context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.text))
        val text = tsa.getString(0)
        tsa.recycle()
        if (!TextUtils.isEmpty(text)) {
            setText(text)
        }
    }

    /**
     * 监听文本复制，对于复制的文本进行空格剔除
     *
     * @param id 操作id(复制，全部选择等)
     * @return 是否操作成功
     */
    override fun onTextContextMenuItem(id: Int): Boolean {
        if (id == android.R.id.copy) {

            if (isFocused) {
                val selStart = selectionStart
                val selEnd = selectionEnd

                val min = Math.max(0, Math.min(selStart, selEnd))
                val max = Math.max(0, Math.max(selStart, selEnd))

                //利用反射获取选择的文本信息，同时关闭操作框
                try {
                    val cls = javaClass.superclass
                    //*arrayOf<Class<*>>(Int::class.java, Int::class.java))  java 代码为  Method getSelectTextMethod = cls.getDeclaredMethod("getTransformedText", newClass[] {int.class, int.class});
                    val getSelectTextMethod = cls.getDeclaredMethod("getTransformedText", *arrayOf<Class<*>>(Int::class.java, Int::class.java))
                    getSelectTextMethod.isAccessible = true
                    val selectedText = getSelectTextMethod.invoke(this,
                            min, max) as CharSequence
                    copy(selectedText.toString())

                    val closeMenuMethod: Method
                    if (Build.VERSION.SDK_INT < 23) {
                        closeMenuMethod = cls.getDeclaredMethod("stopSelectionActionMode")
                    } else {
                        closeMenuMethod = cls.getDeclaredMethod("stopTextActionMode")
                    }
                    closeMenuMethod.isAccessible = true
                    closeMenuMethod.invoke(this)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return true
        } else {
            return super.onTextContextMenuItem(id)
        }
    }


    /**
     * 复制文本到剪切板，去除添加字符
     *
     * @param text 文本
     */
    private fun copy(text: String) {
        val clipboard = context.getSystemService(Context
                .CLIPBOARD_SERVICE) as ClipboardManager
        val start = newText.toString().indexOf(text)
        val end = start + text.length
        val sb = StringBuilder(text)
        for (i in addCharPosition.indices.reversed()) {
            val position = addCharPosition[i]
            if (position < end && position >= start) {
                sb.deleteCharAt(position - start)
            }
        }
        try {
            val clip = android.content.ClipData.newPlainText(null, sb.toString())
            clipboard.primaryClip = clip
        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }

    }

    /**
     * 重置状态
     */
    fun reset() {
        inProcess = false
        addCharPosition.clear()
        newText = ""
        newText = ""
    }

    /**
     * 处理多行文本
     *
     * @param paint 画笔
     * @param text  文本
     * @param width 最大可用宽度
     * @return 处理后的文本
     */
    private fun processText(paint: Paint, text: String?, width: Int): String {
        if (text == null || text.length == 0) {
            return ""
        }
        val lines = text.split("\\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val newText = StringBuilder()
        for (line in lines) {
            newText.append('\n')
            newText.append(processLine(paint, line, width, newText.length - 1))
        }
        if (newText.length > 0) {
            newText.deleteCharAt(0)
        }
        return newText.toString()
    }


    /**
     * 处理单行文本
     *
     * @param paint                     画笔
     * @param text                      文本
     * @param width                     最大可用宽度
     * @param addCharacterStartPosition 添加文本的起始位置
     * @return 处理后的文本
     */
    private fun processLine(paint: Paint, text: String?, width: Int, addCharacterStartPosition: Int): String {
        if (text == null || text.length == 0) {
            return ""
        }

        val old = StringBuilder(text)
        var startPosition = 0 // 起始位置

        val chineseWidth = paint.measureText("中")
        val spaceWidth = paint.measureText(SPACE + "")

        //最大可容纳的汉字，每一次从此位置向后推进计算
        var maxChineseCount = (width / chineseWidth).toInt()

        //减少一个汉字宽度，保证每一行前后都有一个空格
        maxChineseCount--

        //如果不能容纳汉字，直接返回空串
        if (maxChineseCount <= 0) {
            return ""
        }

        var i = maxChineseCount
        while (i < old.length) {
            if (paint.measureText(old.substring(startPosition, i + 1)) > width - spaceWidth) {

                //右侧多余空隙宽度
                val gap = width.toFloat() - spaceWidth - paint.measureText(old.substring(startPosition,
                        i))

                val positions = ArrayList<Int>()
                for (j in startPosition until i) {
                    val ch = old[j]
                    if (punctuation.contains(ch)) {
                        positions.add(j + 1)
                    }
                }

                //空隙最多可以使用几个空格替换
                var number = (gap / spaceWidth).toInt()

                //多增加的空格数量
                var use = 0

                if (positions.size > 0) {
                    var k = 0
                    while (k < positions.size && number > 0) {
                        val times = number / (positions.size - k)
                        val position = positions[k / positions.size]
                        for (m in 0 until times) {
                            old.insert(position + m, SPACE)
                            addCharPosition.add(position + m + addCharacterStartPosition)
                            use++
                            number--
                        }
                        k++
                    }
                }

                //指针移动，将段尾添加空格进行分行处理
                i = i + use
                old.insert(i, SPACE)
                addCharPosition.add(i + addCharacterStartPosition)

                startPosition = i + 1
                i = i + maxChineseCount
            }
            i++
        }

        return old.toString()
    }

    override fun setText(text: CharSequence?, type: TextView.BufferType) {
        //父类初始化的时候子类暂时没有初始化, 覆盖方法会被执行，屏蔽掉
        if (addCharPosition == null) {
            super.setText(text, type)
            return
        }
        if (!inProcess && text != null && text != newText) {
            realText = text
            process(false)
            super.setText(newText, type)
        } else {
            //恢复初始状态
            inProcess = false
            super.setText(text, type)
        }
    }

    /**
     * 文本转化
     *
     * @param setText 是否设置textView的文本
     */
    private fun process(setText: Boolean) {
        if (realText == null) {
            realText = ""
        }
        if (!inProcess && visibility == View.VISIBLE) {
            addCharPosition.clear()

            //转化字符，5.0系统对字体处理有所变动
            if (isConvert) {
                realText = replacePunctuation(realText!!.toString())
            }

            if (width == 0) {
                //没有测量完毕，等待测量完毕后处理
                post { process(true) }
                return
            }

            //添加过边距之后不再次添加
            if (!isAddPadding) {
                val spaceWidth = paint.measureText(SPACE + "").toInt()
                newText = processText(paint, realText!!.toString(), width - paddingLeft -
                        paddingRight - spaceWidth)
                setPadding(paddingLeft + spaceWidth, paddingTop, paddingRight,
                        paddingBottom)
                isAddPadding = true
            } else {
                newText = processText(paint, realText!!.toString(), width - paddingLeft -
                        paddingRight)
            }
            inProcess = true
            if (setText) {
                text = newText
            }
        }
    }

    /**
     * 是否转化标点符号，将中文标点转化为英文标点
     *
     * @param convert 是否转化
     */
    fun setPunctuationConvert(convert: Boolean) {
        isConvert = convert
    }

    companion object {
        private val TAG = AlignCopyTextView::class.java.simpleName
        private val SPACE = ' ' //空格;
        private val punctuation = ArrayList<Char>() //标点符号

        //标点符号用于在textview右侧多出空间时，将空间加到标点符号的后面,以便于右端对齐
        init {
            punctuation.clear()
            punctuation.add(',')
            punctuation.add('.')
            punctuation.add('?')
            punctuation.add('!')
            punctuation.add(';')
            punctuation.add('，')
            punctuation.add('。')
            punctuation.add('？')
            punctuation.add('！')
            punctuation.add('；')
            punctuation.add('）')
            punctuation.add('】')
            punctuation.add(')')
            punctuation.add(']')
            punctuation.add('}')
        }

        private fun replacePunctuation(text: String): String {
            var text = text
            text = text.replace('，', ',').replace('。', '.').replace('【', '[').replace('】', ']')
                    .replace('？', '?').replace('！', '!').replace('（', '(').replace('）', ')').replace('“', '"').replace('”', '"')
            return text
        }
    }
}
