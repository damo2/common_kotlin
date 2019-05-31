package com.app.common.bean

import java.io.File

/**
 * Created by wr
 * Date: 2019/5/31  15:17
 * mail: 1902065822@qq.com
 * describe:
 */
/** 下载apk事件 */
data class DownApkEvent(val isSuc: Boolean, val file: File?)