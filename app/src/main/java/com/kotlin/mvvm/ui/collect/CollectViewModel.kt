package com.kotlin.mvvm.ui.collect

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.kotlin.mvvm.base.BaseViewModel
import com.kotlin.mvvm.common.BaseListResponse
import com.kotlin.mvvm.common.BaseResult
import com.kotlin.mvvm.common.fold
import com.kotlin.mvvm.network.RetrofitFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * description:
 *
 * @author Db_z
 * @Date 2022/1/24 13:53
 */
class CollectViewModel : BaseViewModel() {

    val mCollectBean = MutableLiveData<BaseListResponse<MutableList<CollectBean>>>()

    fun getCollectList(page: Int) = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            val result = RetrofitFactory.instance.service.getCollectList(page)
            if (result.errorCode == 0) {
                BaseResult.success(result.data)
            } else {
                BaseResult.failure(Throwable("Failed to getCollectList${result.errorMsg}"))
            }
        }
        baseResponse.fold({
            mCollectBean.value = it
        }, {
            ToastUtils.showShort(it.message)
        })
    }
}