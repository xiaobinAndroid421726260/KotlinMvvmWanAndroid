package com.kotlin.mvvm.ui.square

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.kotlin.mvvm.base.BaseViewModel
import com.kotlin.mvvm.common.base.BaseListResponse
import com.kotlin.mvvm.common.base.fold
import com.kotlin.mvvm.common.handler_code_collect
import com.kotlin.mvvm.common.handler_code_un_collect
import com.kotlin.mvvm.network.RetrofitFactory
import com.kotlin.mvvm.network.callRequest
import com.kotlin.mvvm.network.handlerResponse
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
        val baseResponse = withContext(Dispatchers.IO) {
            callRequest { handlerResponse(RetrofitFactory.instance.service.getUserArticleJson(page)) }
        }
        baseResponse.fold({
            mDataBeans.value = it
        }, {
            ToastUtils.showShort(it.message)
        })
    }

    fun collect(id: Int) = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            callRequest { handlerResponse(RetrofitFactory.instance.service.collectList(id)) }
        }
        baseResponse.fold({
            handlerCode.value = handler_code_collect
        }, {
            ToastUtils.showShort(it.message)
        })
    }

    fun unCollectList(id: Int) = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            callRequest { handlerResponse(RetrofitFactory.instance.service.unCollectList(id)) }
        }
        baseResponse.fold({
            handlerCode.value = handler_code_un_collect
        }, {
            ToastUtils.showShort(it.message)
        })
    }
}