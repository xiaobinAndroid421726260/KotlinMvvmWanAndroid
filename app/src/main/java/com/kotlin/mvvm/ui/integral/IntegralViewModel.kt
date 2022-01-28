package com.kotlin.mvvm.ui.integral

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.kotlin.mvvm.base.BaseViewModel
import com.kotlin.mvvm.common.BaseListResponse
import com.kotlin.mvvm.common.BaseResult
import com.kotlin.mvvm.common.UiState
import com.kotlin.mvvm.common.fold
import com.kotlin.mvvm.network.RetrofitFactory
import com.kotlin.mvvm.ui.integral.bean.IntegralBean
import com.kotlin.mvvm.ui.integral.bean.RankBean
import com.kotlin.mvvm.ui.integral.bean.UserIntegralBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * description:
 *
 * @author Db_z
 * @Date 2022/1/22 15:42
 */
class IntegralViewModel : BaseViewModel() {

    val mIntegralBean = MutableLiveData<BaseListResponse<MutableList<IntegralBean>>>()
    val mUserIntegralBean = MutableLiveData<UserIntegralBean>()
    val mRankBean = MutableLiveData<BaseListResponse<MutableList<RankBean>>>()

    fun getMyIntegralList(page: Int) = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            val result = RetrofitFactory.instance.service.getMyIntegralList(page)
            if (result.errorCode == 0) {
                BaseResult.success(result.data)
            } else {
                BaseResult.failure(Throwable("Failed to getMyIntegralList${result.errorMsg}"))
            }
        }
        baseResponse.fold({
            mIntegralBean.value = it
        }, {
            ToastUtils.showShort(it.message)
        })
    }

    fun getMyIntegral() = launchUI {
        uiState.value = UiState.Loading
        val baseResponse = withContext(Dispatchers.IO) {
            val result = RetrofitFactory.instance.service.getMyIntegral()
            if (result.errorCode == 0) {
                BaseResult.success(result.data)
            } else {
                BaseResult.failure(Throwable("Failed to getMyIntegral${result.errorMsg}"))
            }
        }
        baseResponse.fold({
            mUserIntegralBean.value = it
            uiState.value = UiState.LoadComplete
        }, {
            ToastUtils.showShort(it.message)
            uiState.value = UiState.LoadError
        })
    }

    fun getIntegralRank(page: Int) = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            val result = RetrofitFactory.instance.service.getIntegralRank(page)
            if (result.errorCode == 0) {
                BaseResult.success(result.data)
            } else {
                BaseResult.failure(Throwable("Failed to getMyIntegral${result.errorMsg}"))
            }
        }
        baseResponse.fold({
            mRankBean.value = it
        }, {
            ToastUtils.showShort(it.message)
        })
    }
}