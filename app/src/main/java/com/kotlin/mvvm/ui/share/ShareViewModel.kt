package com.kotlin.mvvm.ui.share

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.kotlin.mvvm.base.BaseViewModel
import com.kotlin.mvvm.common.BaseListResponse
import com.kotlin.mvvm.common.BaseResult
import com.kotlin.mvvm.common.fold
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
}