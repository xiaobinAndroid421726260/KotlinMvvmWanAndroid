package com.kotlin.mvvm.common.loadsir

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.ProgressBar
import com.kingja.loadsir.callback.Callback
import com.kotlin.mvvm.R
import com.kotlin.mvvm.base.App
import com.kotlin.mvvm.ext.getThemeTextColor

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/24 14:58
 */
class LoadingCallback : Callback() {

    override fun onCreateView() = R.layout.base_layout_loading

    override fun onViewCreate(context: Context?, view: View?) {
        view?.let {
            val progressBar = it.findViewById<ProgressBar>(R.id.progress)
            val color = App.CONTEXT.getThemeTextColor()
            progressBar.indeterminateTintList = ColorStateList.valueOf(color)
        }
    }
}