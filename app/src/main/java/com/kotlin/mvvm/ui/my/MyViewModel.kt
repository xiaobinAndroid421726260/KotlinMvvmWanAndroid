package com.kotlin.mvvm.ui.my

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

    fun getWendListJson(page: Int, page_size: Int = 20) = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            callRequest {
                handlerResponse(
                    RetrofitFactory.instance.service.getWendListJson(
                        page,
                        page_size
                    )
                )
            }
        }
        baseResponse.fold({
            mWendBean.value = it
        }, {
            ToastUtils.showShort(it.message)
        })
    }

    fun getMessageCountUnread() = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            callRequest { handlerResponse(RetrofitFactory.instance.service.getMessageCountUnread()) }
        }
        baseResponse.fold({
            handlerCode.value = it
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