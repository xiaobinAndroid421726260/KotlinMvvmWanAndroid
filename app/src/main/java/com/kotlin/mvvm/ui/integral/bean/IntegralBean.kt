package com.kotlin.mvvm.ui.integral.bean

/**
 * description:
 *
 * @author Db_z
 * @Date 2022/1/22 15:29
 */
data class IntegralBean(
    val coinCount: Int,
    val date: Long,
    val desc: String,
    val id: Int,
    val reason: String,
    val type: Int,
    val userId: Int,
    val userName: String
)