package com.kotlin.mvvm.common.base

import java.io.Serializable

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/16 17:04
 */
data class BaseListResponse<T>(
    val offset: Int,
    val size: Int,
    val total: Int,
    val pageCount: Int,
    val curPage: Int,
    val over: Boolean,
    val datas: T
) : Serializable