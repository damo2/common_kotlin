package com.app.common.api.transformer

import com.app.common.api.ApiException
import com.app.common.json.GsonUtil
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by wangru
 * Date: 2018/12/17  14:47
 * mail: 1902065822@qq.com
 * describe:
 */
fun <T> composeCommonBean(): ObservableTransformer<T, T> {
    return ObservableTransformer { observable ->
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { t ->
                    var msg = ""
                    val code = when (t) {
                        is CommonBaseBean<*> -> {
                            msg = t.msg
                            t.code
                        }
                        else -> 12000
                    }
                    if (code != 12000) {
                        throw ApiException(code, msg, GsonUtil().toJson(t))
                    }
                    t
                }
    }
}
