package com.kotlin.mvvm.common

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/24 15:08
 */
interface BaseView {
    /**
     * 显示内容
     */
    fun showContent()

    /**
     * 显示加载提示
     */
    fun showLoading()

    /**
     * 显示空页面
     */
    fun showEmpty()

    /**
     * 刷新失败
     */
    fun showFailure(message: String?)
}