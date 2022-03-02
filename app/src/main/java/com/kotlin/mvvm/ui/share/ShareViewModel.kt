package com.kotlin.mvvm.ui.share

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.kotlin.mvvm.base.BaseViewModel
import com.kotlin.mvvm.common.*
import com.kotlin.mvvm.network.RetrofitFactory
import com.kotlin.mvvm.ui.share.bean.Share
import com.kotlin.mvvm.ui.share.bean.ShareBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * description:
 *
 * @author Db_z
 * @Date 2022/1/24 16:27
 */
class ShareViewModel : BaseViewModel() {

    val mShareBean = MutableLiveData<ShareBean<BaseListResponse<MutableList<Share>>>>()

    fun getUserShareList(page: Int) = launchUI {
        val baseResponse = withContext(Dispatchers.IO){
            val result = RetrofitFactory.instance.service.getUserShareList(page)
            if (result.errorCode == 0){
                BaseResult.success(result.data)
            } else {
                BaseResult.failure(Throwable("Failed to getUserShareList${result.errorMsg}"))
            }
        }
        baseResponse.fold({
            mShareBean.value = it
        }, {
            ToastUtils.showShort(it.message)
        })
    }

    fun collect(id: Int) = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            val result = RetrofitFactory.instance.service.collectList(id)
            if (result.errorCode == 0) {
                BaseResult.success(result.errorCode)
            } else {
                BaseResult.failure(Throwable("Failed to collect${result.errorMsg}"))
            }
        }
        baseResponse.fold({
            handlerCode.value = handler_code_collect
        }, {
            ToastUtils.showShort(it.message)
        })
    }

    fun unCollectList(id: Int) = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            val result = RetrofitFactory.instance.service.unCollectList(id)
            if (result.errorCode == 0) {
                BaseResult.success(result.errorCode)
            } else {
                BaseResult.failure(Throwable("Failed to unCollect${result.errorMsg}"))
            }
        }
        baseResponse.fold({
            handlerCode.value = handler_code_un_collect
        }, {
            ToastUtils.showShort(it.message)
        })
    }
}