package com.kotlin.mvvm.ui.setting

import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.kotlin.mvvm.R
import com.kotlin.mvvm.base.BaseViewModel
import com.kotlin.mvvm.common.UiState
import com.kotlin.mvvm.common.base.fold
import com.kotlin.mvvm.common.handler_code_logout
import com.kotlin.mvvm.network.RetrofitFactory
import com.kotlin.mvvm.network.callRequest
import com.kotlin.mvvm.network.handlerResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * description:
 *
 * @author Db_z
 * @Date 2022/1/6 11:33
 */
class SettingViewModel : BaseViewModel() {

    fun logout() = launchUI {
        uiState.value = UiState.Loading
        val baseResponse = withContext(Dispatchers.IO) {
            callRequest { handlerResponse(RetrofitFactory.instance.service.logout()) }
        }
        uiState.value = UiState.LoadComplete
        baseResponse.fold({
            handlerCode.value = handler_code_logout
        }, {
            ToastUtils.showShort(it.message)
        })
    }
}