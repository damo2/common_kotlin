package com.weiyao.zuzuapp.api

import com.app.common.api.ApiException
import com.app.common.json.GsonUtil
import com.weiyao.zuzuapp.base.BaseBean
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.schedulers.Schedulers

/**
 * Created by wr
 * Date: 2019/4/23  20:39
 * mail: 1902065822@qq.com
 * describe:
 * 统一处理，失败请求
 */
fun <T> composeDefault(): ObservableTransformer<T, T> {
    return ObservableTransformer { observable ->
        observable.composeDefault()
    }
}

fun <T> Observable<T>.composeDefault(): Observable<T> =
        subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io()).map { t ->
                    var msg = "请求异常"
                     when (t) {
                        is BaseBean<*> -> {
                            msg = t.msg
                            if (t.code != 12000) {
                                throw ApiException(t.code, msg)
                            }
                        }
                    }
                    t
                }