package com.kotlin.mvvm.network

import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.StringUtils
import com.kotlin.mvvm.R
import com.kotlin.mvvm.common.base.BaseResponse
import com.kotlin.mvvm.common.base.BaseResult
import com.kotlin.mvvm.ext.d
import okio.IOException

/**
 * description:
 *
 * @author Db_z
 * @Date 2022/3/17 11:20
 */

/**
 * 请求
 */
suspend fun <T : Any> callRequest(
    call: suspend () -> BaseResult<T>
): BaseResult<T> {
    return try {
        call()
    } catch (e: Exception) {
        //这里统一处理异常
        e.printStackTrace()
        val recordMethodName = Thread.currentThread().stackTrace
        d("className = ${recordMethodName[4].className} fileName = ${recordMethodName[4].fileName} lineNumber = ${recordMethodName[4].lineNumber} methodName = ${recordMethodName[4].methodName}")
        BaseResult.failure(if (!NetworkUtils.isConnected()) IOException(StringUtils.getString(R.string.common_connect_error)) else e)
    }
}

/**
 * 处理返回结果
 *
 *  请注意：当前在IO线程
 *  @param successBlock  成功之后处理
 *  @param errorBlock    失败之后处理
 */
suspend fun <T : Any> handlerResponse(
    response: BaseResponse<T>,
    successBlock: (suspend () -> Unit)? = null,
    errorBlock: (suspend () -> Unit)? = null
): BaseResult<T> {
//    return coroutineScope {
    return if (response.errorCode == 0) {
        val recordMethodName = Thread.currentThread().stackTrace
        d("className = ${recordMethodName[4].className} fileName = ${recordMethodName[4].fileName} lineNumber = ${recordMethodName[4].lineNumber} methodName = ${recordMethodName[4].methodName}")
        // 成功之后处理
        successBlock?.let { it() }
        BaseResult.success(response.data)
    } else {
        val recordMethodName = Thread.currentThread().stackTrace
        d("className = ${recordMethodName[4].className} fileName = ${recordMethodName[4].fileName} lineNumber = ${recordMethodName[4].lineNumber} methodName = ${recordMethodName[4].methodName}")
        // 失败之后处理
        errorBlock?.let { it() }
        BaseResult.failure(Exception("Failed to ${recordMethodName[4].className}${response.errorMsg}"))
    }
//    }
}