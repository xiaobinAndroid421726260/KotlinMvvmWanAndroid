package com.kotlin.mvvm.ui.system

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.kotlin.mvvm.base.BaseViewModel
import com.kotlin.mvvm.common.base.fold
import com.kotlin.mvvm.network.RetrofitFactory
import com.kotlin.mvvm.network.callRequest
import com.kotlin.mvvm.network.handlerResponse
import com.kotlin.mvvm.ui.system.bean.NaviBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/27 17:27
 */
class NaviViewModel : BaseViewModel() {

    val mNaviBean = MutableLiveData<MutableList<NaviBean>>()

    fun getNaviJson() = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            callRequest {
                handlerResponse(RetrofitFactory.instance.service.getNaviJson())
            }
        }
        baseResponse.fold({
            mNaviBean.value = it
        }, {
            ToastUtils.showShort(it.message)
        })
    }
}