package com.kotlin.mvvm.ui.square

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
}