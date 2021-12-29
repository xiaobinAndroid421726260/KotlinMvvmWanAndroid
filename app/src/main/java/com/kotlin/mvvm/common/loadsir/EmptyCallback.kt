package com.kotlin.mvvm.common.loadsir

import com.kingja.loadsir.callback.Callback
import com.kotlin.mvvm.R

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/24 14:54
 */
class EmptyCallback : Callback() {

    override fun onCreateView() = R.layout.base_layout_empty
}