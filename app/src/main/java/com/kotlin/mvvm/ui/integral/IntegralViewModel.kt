package com.kotlin.mvvm.ui.integral

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.kotlin.mvvm.base.BaseViewModel
import com.kotlin.mvvm.common.UiState
import com.kotlin.mvvm.common.base.BaseListResponse
import com.kotlin.mvvm.common.base.fold
import com.kotlin.mvvm.network.RetrofitFactory
import com.kotlin.mvvm.network.callRequest
import com.kotlin.mvvm.network.handlerResponse
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
            callRequest { handlerResponse(RetrofitFactory.instance.service.getMyIntegralList(page)) }
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
            callRequest { handlerResponse(RetrofitFactory.instance.service.getMyIntegral()) }
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
            callRequest { handlerResponse(RetrofitFactory.instance.service.getIntegralRank(page)) }
        }
        baseResponse.fold({
            mRankBean.value = it
        }, {
            ToastUtils.showShort(it.message)
        })
    }
}