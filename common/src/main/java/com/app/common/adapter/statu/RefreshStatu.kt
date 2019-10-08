package com.app.common.adapter.statu

/**
 * 刷新加载类型
 * Created by wangru
 * Date: 2017/12/28  10:12
 * mail: 1902065822@qq.com
 * describe:
 */
enum class RefreshStatu {
    /**
     * 首次进入
     */
    FIRST,
    /**
     * 刷新
     */
    REFRESH,
    /**
     * 加载
     */
    LOAD,
    /**
     * 首次完成
     */
    FIRST_SUC,
    /**
     * 首次失败
     */
    FIRST_FAIL,
    /**
     * 刷新完成
     */
    REFRESH_SUC,
    /**
     * 刷新完成
     */
    REFRESH_FAIL,
    /**
     * 加载完成
     */
    LOAD_SUC,
    /**
     * 加载失败
     */
    LOAD_FAIL,
    /**
     * 全部加在完成
     */
    LOAD_OVER_ALL,
    /**
     * 没有数据
     */
    NULL,
    /**
     * 显示不全
     */
    NOFULL;

    /**
     * 正在请求数据（刷新或加载）
     */
    val isDoing: Boolean
        get() = this == RefreshStatu.REFRESH || this == RefreshStatu.LOAD || this == FIRST

    val isNoDoing: Boolean
        get() = !isDoing

    val isRefresh: Boolean
        get() = this == RefreshStatu.REFRESH || this == REFRESH_SUC || this == REFRESH_FAIL

    val isLoad: Boolean
        get() = this == RefreshStatu.LOAD || this == LOAD_SUC || this == REFRESH_FAIL

    val isFirst: Boolean
        get() = this == RefreshStatu.FIRST || this == FIRST_SUC || this == FIRST_FAIL

    val hasMore: Boolean
        get() = this != RefreshStatu.LOAD_OVER_ALL && this != RefreshStatu.NULL

    val isRefreshOrFirst: Boolean
        get() = isRefresh || isFirst

    companion object {


        /**
         * 得到完成状态
         */
        fun getStatuSuc(mRefreshStatu: RefreshStatu): RefreshStatu {
            if (mRefreshStatu == RefreshStatu.REFRESH) {
                return RefreshStatu.REFRESH_SUC
            } else if (mRefreshStatu == RefreshStatu.LOAD) {
                return RefreshStatu.LOAD_SUC
            } else if (mRefreshStatu == RefreshStatu.FIRST) {
                return RefreshStatu.FIRST_SUC
            }
            return mRefreshStatu
        }

        fun getStatuFail(mRefreshStatu: RefreshStatu): RefreshStatu {
            if (mRefreshStatu == RefreshStatu.REFRESH) {
                return RefreshStatu.REFRESH_FAIL
            } else if (mRefreshStatu == RefreshStatu.LOAD) {
                return RefreshStatu.LOAD_FAIL
            } else if (mRefreshStatu == RefreshStatu.FIRST) {
                return RefreshStatu.FIRST_FAIL
            }
            return mRefreshStatu
        }
    }

}
