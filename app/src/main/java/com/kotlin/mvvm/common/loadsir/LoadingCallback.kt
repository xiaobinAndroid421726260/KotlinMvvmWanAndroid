package com.kotlin.mvvm.common.loadsir

import com.kingja.loadsir.callback.Callback
import com.kotlin.mvvm.R

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/24 14:58
 */
class LoadingCallback : Callback() {

    override fun onCreateView() = R.layout.base_layout_loading
}