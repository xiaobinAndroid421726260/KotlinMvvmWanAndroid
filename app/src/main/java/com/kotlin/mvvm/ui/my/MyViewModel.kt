package com.kotlin.mvvm.ui.my

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.kotlin.mvvm.base.BaseViewModel
import com.kotlin.mvvm.common.BaseListResponse
import com.kotlin.mvvm.common.BaseResult
import com.kotlin.mvvm.common.UserInfoBean
import com.kotlin.mvvm.common.fold
import com.kotlin.mvvm.network.RetrofitFactory
import com.kotlin.mvvm.ui.my.bean.WendBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * description:
 *
 * @author Db_z
 * @Date 2022/1/21 10:21
 */
class MyViewModel : BaseViewModel() {

    val mWendBean = MutableLiveData<BaseListResponse<MutableList<WendBean>>>()
    val mUserInfoBean = MutableLiveData<UserInfoBean>()

    fun getWendListJson(page: Int, page_size: Int = 20) = launchUI {
        val baseResponse = withContext(Dispatchers.IO){
            val result = RetrofitFactory.instance.service.getWendListJson(page, page_size)
            if (result.errorCode == 0){
                BaseResult.success(result.data)
            } else {
                BaseResult.failure(Throwable("Failed to getWendListJson${result.errorMsg}"))
            }
        }
        baseResponse.fold({
            mWendBean.value = it
        }, {
            ToastUtils.showShort(it.message)
        })
    }

    fun getMessageCountUnread() = launchUI {
        val baseResponse = withContext(Dispatchers.IO){
            val result = RetrofitFactory.instance.service.getMessageCountUnread()
            if (result.errorCode == 0){
                BaseResult.success(result.data)
            } else {
                BaseResult.failure(Throwable("Failed to getMessageCountUnread${result.errorMsg}"))
            }
        }
        baseResponse.fold({
            handlerCode.value = it
        }, {
            ToastUtils.showShort(it.message)
        })
    }

    fun userInfo() = launchUI {
        val baseResponse = withContext(Dispatchers.IO){
            val result = RetrofitFactory.instance.service.userInfo()
            if (result.errorCode == 0){
                BaseResult.success(result.data)
            } else {
                BaseResult.failure(Throwable("Failed to userInfo${result.errorMsg}"))
            }
        }
        baseResponse.fold({
            mUserInfoBean.value = it
        }, {
            ToastUtils.showShort(it.message)
        })
    }
}