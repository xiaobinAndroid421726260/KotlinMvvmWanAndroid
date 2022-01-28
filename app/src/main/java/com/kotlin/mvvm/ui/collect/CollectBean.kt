package com.kotlin.mvvm.ui.collect

/**
 * description:
 *
 * @author Db_z
 * @Date 2022/1/24 14:03
 */
data class CollectBean(
    val author: String,
    val chapterId: Int,
    val chapterName: String,
    val collect: Boolean = true,
    val courseId: Int,
    val desc: String,
    val envelopePic: String,
    val id: Int,
    val link: String,
    val niceDate: String,
    val origin: String,
    val originId: Int,
    val publishTime: Long,
    val title: String,
    val userId: Int,
    val visible: Int,
    val zan: Int
)