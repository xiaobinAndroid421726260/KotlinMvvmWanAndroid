package com.kotlin.mvvm.ui.wechat.bean

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/22 15:54
 */
data class WechatBean(
    val children: List<Any>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
)