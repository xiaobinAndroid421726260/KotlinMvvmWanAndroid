package com.kotlin.mvvm.popup

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import com.kotlin.mvvm.R

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/10/26 17:03
 */
class LoadPopup(context: Context) : BasePopup(context) {

    init {
        setBackgroundColor(Color.TRANSPARENT)
        setContentView(R.layout.popup_loading)
        popupGravity = Gravity.CENTER
        setOutSideDismiss(false)
        isOutSideTouchable = false
        showPopupWindow()
    }
}