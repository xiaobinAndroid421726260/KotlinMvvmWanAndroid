package com.kotlin.mvvm.ui.square

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.kotlin.mvvm.base.BaseViewModel
import com.kotlin.mvvm.common.*
import com.kotlin.mvvm.network.RetrofitFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/22 14:20
 */
class SquareViewModel : BaseViewModel() {

    val mDataBeans = MutableLiveData<BaseListResponse<MutableList<SquareBean>>>()

    fun getUserArticleJson(page: Int) = launchUI {
        val baseResponse = withContext(Dispatchers.IO){
            val squareBean = RetrofitFactory.instance.service.getUserArticleJson(page)
            if (squareBean.errorCode == 0){
                BaseResult.success(squareBean.data)
            } else {
                BaseResult.failure(Throwable("Failed to get getUserArticleJson${squareBean.errorMsg}"))
            }
        }
        baseResponse.fold({
            mDataBeans.value = it
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