package com.kotlin.mvvm.ui.project.bean

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/24 9:47
 */
data class ProjectBean(
    val children: List<Any>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
)