package com.kotlin.mvvm.ui.system.bean

import java.io.Serializable

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/27 16:22
 */
data class SystemBean(
    val children: MutableList<Children>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
)

data class Children(
    val children: List<Any>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
) : Serializable