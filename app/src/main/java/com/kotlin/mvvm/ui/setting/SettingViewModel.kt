package com.kotlin.mvvm.ui.setting

import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.kotlin.mvvm.R
import com.kotlin.mvvm.base.BaseViewModel
import com.kotlin.mvvm.common.BaseResult
import com.kotlin.mvvm.common.UiState
import com.kotlin.mvvm.common.fold
import com.kotlin.mvvm.common.handler_code_logout
import com.kotlin.mvvm.network.RetrofitFactory
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
            val result = RetrofitFactory.instance.service.logout()
            if (result.errorCode == 0) {
                BaseResult.success(StringUtils.getString(R.string.logout))
            } else {
                BaseResult.failure(Throwable("Failed to logout${result.errorMsg}"))
            }
        }
        uiState.value = UiState.LoadComplete
        baseResponse.fold({
            ToastUtils.showShort(it)
            handlerCode.value = handler_code_logout
        }, {
            ToastUtils.showShort(it.message)
        })
    }
}