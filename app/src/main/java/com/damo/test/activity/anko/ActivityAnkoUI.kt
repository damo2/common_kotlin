package com.damo.test.activity.anko

import android.view.View
import android.widget.ImageView
import com.damo.test.R
import com.damo.test.activity.AnkoActivity
import org.jetbrains.anko.*

/**
 * Created by wr
 * Date: 2019/5/7  11:13
 * mail: 1902065822@qq.com
 * describe:
 */
class ActivityAnkoUI : AnkoComponent<AnkoActivity> {
    override fun createView(ui: AnkoContext<AnkoActivity>): View = with(ui) {
        return relativeLayout {
            button("登录")

        }
    }
}