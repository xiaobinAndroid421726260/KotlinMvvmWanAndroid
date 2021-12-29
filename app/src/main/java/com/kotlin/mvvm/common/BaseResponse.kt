package com.kotlin.mvvm.common

import java.io.Serializable


/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/10 10:23
 */
data class BaseResponse<out T>(val errorMsg: String, val errorCode: Int, val data: T) : Serializable