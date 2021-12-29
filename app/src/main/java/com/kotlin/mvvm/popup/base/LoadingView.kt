package com.kotlin.mvvm.popup.base

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import com.kotlin.mvvm.R


/**
 * description:
 *
 * @author Db_z
 * @Date 2021/10/11 11:40
 */
class LoadingView private constructor(context: Context) : BasePopup(context){

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: LoadingView? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: LoadingView(context).also { instance = it }
            }
    }

    init {
        setContentView(R.layout.popup_loading)
        popupGravity = Gravity.CENTER
        setOutSideDismiss(false)
        isOutSideTouchable = false
    }
}