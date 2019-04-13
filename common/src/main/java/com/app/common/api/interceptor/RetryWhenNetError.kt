package com.app.common.api.interceptor

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Created by wr
 * Date: 2019/1/28  19:48
 * mail: 1902065822@qq.com
 * describe:
 * 网络请求异常延迟重新请求
 */

/**
 * @param count retry次数
 * @param delay 延迟
 * @param increaseDelay 叠加延迟
 * @sample
RequestManager.instanceApi
.getLanguageType()
.compose(composeLife(LifeCycleEvent.DESTROY, lifecycleSubject))
.retryWhen(RetryWhenNetError())
.subscribe({
Logger.d("test data" + GsonUtil().toJson(it))
})
 */
class RetryWhenNetError(var count: Int = 3, var delay: Long = 3000L, var increaseDelay: Long = 3000L) : Function<Observable<out Throwable>, Observable<*>> {
    @Throws(Exception::class)
    override fun apply(observable: Observable<out Throwable>): Observable<*> {
        return observable
                .zipWith(Observable.range(1, count + 1), BiFunction<Throwable, Int, Pair<Throwable, Int>> { throwable, integer ->
                    Pair(throwable, integer)
                })
                .flatMap { wrapper ->
                    //如果超出重试次数也抛出错误，否则默认是会进入onCompleted
                    if ((wrapper.first is ConnectException || wrapper.first is SocketTimeoutException
                                    || wrapper.first is TimeoutException) && wrapper.second < count + 1) {
                        Observable.timer(delay + (wrapper.second - 1) * increaseDelay, TimeUnit.MILLISECONDS)
                    } else Observable.error(wrapper.first)
                }
    }
}
