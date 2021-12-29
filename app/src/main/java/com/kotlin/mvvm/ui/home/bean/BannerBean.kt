package com.kotlin.mvvm.ui.home.bean

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/16 16:19
 */
data class BannerBean(
    val desc: String,
    val id: Int,
    val imagePath: String,
    val isVisible: Int,
    val order: Int,
    val title: String,
    val type: Int,
    val url: String
)