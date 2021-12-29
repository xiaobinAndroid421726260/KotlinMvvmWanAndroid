package com.kotlin.mvvm.network

import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.kotlin.mvvm.R
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Desc : 监听网络变化，只在联网状态下发起请求
 */
class NetworkStatusInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showShort(StringUtils.getString(R.string.network_connection_error))
        }
        return chain.proceed(chain.request())
    }
}