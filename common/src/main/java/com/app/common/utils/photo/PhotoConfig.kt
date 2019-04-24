package com.app.common.utils.photo

import android.app.Activity
import androidx.fragment.app.Fragment

/**
 * Created by wr
 * Date: 2019/2/19  17:02
 * mail: 1902065822@qq.com
 * describe:
 */
class PhotoConfig {
    var activity: Activity? = null
    var fragment: Fragment? = null
    var tag: String? = null//标记

    var chooseType: Int = CHOOSE_CAMERA //获取照片方式 默认拍照
    var isCuted: Boolean = false//是否需要裁剪
    var cameraPath: String? = null//拍照路径
    var cutPath: String? = null//裁剪路径
    var isCutAuto: Boolean = false//true为如果原图小于裁剪大小则按原图占比小的标准裁剪
    var isDeleteOld: Boolean = false//是否删除拍照裁剪之前的图片
    var cutWidth = CUT_WIDTH_HEIGHT_DEFAULT
    var cutHeight = CUT_WIDTH_HEIGHT_DEFAULT
    var onPathCallback: ((path: String) -> Unit)? = null

    constructor(activity: Activity, chooseType: Int, onPathCallback: (path: String) -> Unit) {
        this.activity = activity
        this.chooseType = chooseType
        this.onPathCallback = onPathCallback
    }

    constructor(fragment: Fragment, chooseType: Int, onPathCallback: (path: String) -> Unit) {
        this.fragment = fragment
        this.chooseType = chooseType
        this.onPathCallback = onPathCallback
    }

    companion object {
        private val CUT_WIDTH_HEIGHT_DEFAULT = 256//
        const val CHOOSE_CAMERA = 1
        const val CHOOSE_PHOTO = 2
    }
}