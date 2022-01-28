package com.kotlin.mvvm.ui.message

/**
 * description:
 *
 * @author Db_z
 * @Date 2022/1/24 15:48
 */
data class MsgBean(
    val category: Int,
    val date: Long,
    val fromUser: String,
    val fromUserId: Int,
    val fullLink: String,
    val id: Int,
    val isRead: Int,
    val link: String,
    val message: String,
    val niceDate: String,
    val tag: String,
    val title: String,
    val userId: Int
)