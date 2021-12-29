package com.kotlin.mvvm.common.loadsir

import android.content.Context
import android.view.View
import com.kingja.loadsir.callback.Callback
import com.kotlin.mvvm.R

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/24 15:02
 */
class ErrorCallback : Callback(){

    override fun onCreateView() = R.layout.base_layout_error

    override fun onReloadEvent(context: Context?, view: View?) = true
}