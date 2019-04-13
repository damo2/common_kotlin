package com.app.common.api

/**
 * Created by wangru
 * Date: 2018/6/28  19:27
 * mail: 1902065822@qq.com
 * describe:
 */

class ApiException constructor(var code: Int, msg: String?, result: String?) : RuntimeException(msg)