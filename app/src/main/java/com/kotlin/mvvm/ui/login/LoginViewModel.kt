package com.kotlin.mvvm.ui.login

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.kotlin.mvvm.base.BaseViewModel
import com.kotlin.mvvm.common.BaseResult
import com.kotlin.mvvm.common.UiState
import com.kotlin.mvvm.common.fold
import com.kotlin.mvvm.network.RetrofitFactory
import com.kotlin.mvvm.ui.login.bean.LoginBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * description:
 *
 * @author Db_z
 * @Date 2022/1/5 16:56
 */
class LoginViewModel : BaseViewModel() {

    val mLoginRegisterBean = MutableLiveData<LoginBean>()

    fun login(username: String, password: String) = launchUI {
        uiState.value = UiState.Loading
        val baseResponse = withContext(Dispatchers.IO) {
            val result = RetrofitFactory.instance.service.login(username, password)
            if (result.errorCode == 0) {
                BaseResult.success(result.data)
            } else {
                BaseResult.failure(Throwable("Failed to login${result.errorMsg}"))
            }
        }
        baseResponse.fold({
            mLoginRegisterBean.value = it
            uiState.value = UiState.LoadComplete
        }, {
            ToastUtils.showShort(it.message)
            uiState.value = UiState.LoadError
        })
    }

    fun register(username: String, password: String, rePassword: String) = launchUI {
        uiState.value = UiState.Loading
        val baseResponse = withContext(Dispatchers.IO) {
            val result = RetrofitFactory.instance.service.register(username, password, rePassword)
            if (result.errorCode == 0) {
                BaseResult.success(result.data)
            } else {
                BaseResult.failure(Throwable("Failed to register${result.errorMsg}"))
            }
        }
        baseResponse.fold({
            mLoginRegisterBean.value = it
            uiState.value = UiState.LoadComplete
        }, {
            ToastUtils.showShort(it.message)
            uiState.value = UiState.LoadError
        })
    }
}