package com.weiyao.zuzuapp.base

import java.io.Serializable

/**
 * Created by wangru
 * Date: 2018/6/28  19:17
 * mail: 1902065822@qq.com
 * describe:
 */

class BaseBean<T> : Serializable {
    var code: Int = 0
    var msg: String = ""
    var data: T? = null
}
