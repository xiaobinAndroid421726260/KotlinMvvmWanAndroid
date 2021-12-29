package com.kotlin.mvvm.api

import com.kotlin.mvvm.api.HttpsApi.BASE_URL
import com.kotlin.mvvm.api.HttpsApi.GANK_IO_URL

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/10 14:42
 */

const val ANDROID_URL = 1
const val WAN_ANDROID = 2

fun getHost(hostType: Int): String {
    return when (hostType) {
        ANDROID_URL -> BASE_URL
        WAN_ANDROID -> GANK_IO_URL
        else -> BASE_URL
    }
}