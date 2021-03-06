package com.weiyao.zuzuapp.activity.test

import android.annotation.SuppressLint
import android.os.Parcelable
import com.app.common.extensions.add
import com.app.common.logger.logd
import com.app.common.utils.*
import com.app.common.view.asyncLayout.AsyncLayoutLoader
import com.app.common.view.toastInfo
import com.weiyao.zuzuapp.R
import com.weiyao.zuzuapp.base.BaseActivity
import com.weiyao.zuzuapp.testmodule.javafun.JavaFunTest
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_test2.*
import java.math.BigDecimal
import java.util.*

/**
 * Created by wr
 * Date: 2019/8/12  9:39
 * mail: 1902065822@qq.com
 * describe:
 */
class Test2Activity : BaseActivity() {
    override fun bindLayout(): Int = R.layout.activity_test2

    override fun initListener() {
        super.initListener()
        btnData.setOnClickListener {
            val a = BigDecimal(1.01)
            val b = BigDecimal(1.02)
            val c = BigDecimal("1.01")
            val d = BigDecimal("1.02")

            System.out.println(1.01 + 1.02)
            System.out.println(a.add(b))
            System.out.println(c.add(d))

            val e = 1.01.add(1.02)

            val f = 1.01 add 1.02
            System.out.println(e)
            System.out.println(f)

            val date = Date()

            val beforeDay = date - 1
            val lastDay = date + 1

            val beforeHour = date - hour(3)
            val lastHour = date + hour(3)


            logd("现在" + date.stringFormat())
            logd("前一天" + beforeDay.stringFormat())
            logd("后一天" + lastDay.stringFormat())
            logd("前3个小时" + beforeHour.stringFormat())
            logd("后3个小时" + lastHour.stringFormat())

            logd("${date[0]}年  ${date[1]}月    ${date[2]}日  ${date[3]}时    ${date[4]}分  ${date[5]}秒" )

            val isBefore = beforeDay < date

            val count = justCount()
            count()  // 输出结果：0
            count()  // 输出结果：1
            count()  // 输出结果：2

            JavaFunTest.test()
        }

        btnEvent.setOnClickListener {
            logd("button点击")
            toastInfo("button点击")
        }
        AsyncLayoutLoader.getLayoutLoader(R.layout.activity_test2)

    }


    /**
     * 计数统计
     */
    fun justCount():() -> Unit{
        var count = 0
        return {
            println(count++)
        }
    }


}

//@SuppressLint("ParcelCreator")
//@Parcelize
//open class BaseBean() : Parcelable {}
