package com.kotlin.mvvm.ui.login.bean

/**
 * description:
 *
 * @author Db_z
 * @Date 2022/1/5 17:26
 */
data class LoginBean(
    val admin: Boolean,
    val chapterTops: List<Any>,
    val coinCount: Int,
    val collectIds: List<Int>,
    val email: String,
    val icon: String,
    val id: Int,
    val nickname: String,
    val password: String,
    val publicName: String,
    val token: String,
    val type: Int,
    val username: String
)