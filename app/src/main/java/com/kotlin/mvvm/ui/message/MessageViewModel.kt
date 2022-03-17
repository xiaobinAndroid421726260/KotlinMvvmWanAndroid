package com.kotlin.mvvm.ui.message

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.kotlin.mvvm.base.BaseViewModel
import com.kotlin.mvvm.common.base.BaseListResponse
import com.kotlin.mvvm.common.base.fold
import com.kotlin.mvvm.network.RetrofitFactory
import com.kotlin.mvvm.network.callRequest
import com.kotlin.mvvm.network.handlerResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * description:
 *
 * @author Db_z
 * @Date 2022/1/25 10:02
 */
class MessageViewModel : BaseViewModel() {

    val mReadListBean = MutableLiveData<BaseListResponse<MutableList<MsgBean>>>()
    val mUnreadListBean = MutableLiveData<BaseListResponse<MutableList<MsgBean>>>()

    fun getMessageReadList(page: Int) = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            callRequest { handlerResponse(RetrofitFactory.instance.service.getMessageReadList(page)) }
        }
        baseResponse.fold({
            mReadListBean.value = it
        }, {
            ToastUtils.showShort(it.message)
        })
    }

    fun getMessageUnreadList(page: Int) = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            callRequest { handlerResponse(RetrofitFactory.instance.service.getMessageUnreadList(page)) }
        }
        baseResponse.fold({
            mUnreadListBean.value = it
        }, {
            ToastUtils.showShort(it.message)
        })
    }
}