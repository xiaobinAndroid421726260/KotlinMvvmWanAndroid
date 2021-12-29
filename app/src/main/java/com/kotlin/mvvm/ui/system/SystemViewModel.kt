package com.kotlin.mvvm.ui.system

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.kotlin.mvvm.base.BaseViewModel
import com.kotlin.mvvm.common.BaseResult
import com.kotlin.mvvm.common.fold
import com.kotlin.mvvm.network.RetrofitFactory
import com.kotlin.mvvm.ui.system.bean.SystemBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/27 16:45
 */
class SystemViewModel : BaseViewModel() {

    val mSystemBean = MutableLiveData<MutableList<SystemBean>>()

    fun getTreeJson() = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            val result = RetrofitFactory.instance.service.getTreeJson()
            if (result.errorCode == 0) {
                BaseResult.success(result.data)
            } else {
                BaseResult.failure(Throwable("Failed to get getTreeJson${result.errorMsg}"))
            }
        }
        baseResponse.fold({
            mSystemBean.value = it
        }, {
            ToastUtils.showShort(it.message)
        })
    }
}