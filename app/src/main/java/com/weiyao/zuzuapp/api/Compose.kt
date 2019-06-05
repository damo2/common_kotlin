package com.weiyao.zuzuapp.api

import com.app.common.api.ApiException
import com.app.common.json.GsonUtil
import com.weiyao.zuzuapp.base.BaseBean
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by wr
 * Date: 2019/4/23  20:39
 * mail: 1902065822@qq.com
 * describe:
 */
fun <T> composeDefault(): ObservableTransformer<T, T> {
    return ObservableTransformer { observable ->
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { t ->
                    var msg = "请求异常"
                    val code = when (t) {
                        is BaseBean<*> -> {
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