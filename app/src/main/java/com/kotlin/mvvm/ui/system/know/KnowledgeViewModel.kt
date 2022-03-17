package com.kotlin.mvvm.ui.system.know

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
import com.kotlin.mvvm.ui.system.bean.KnowBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/29 11:56
 */
class KnowledgeViewModel : BaseViewModel() {

    val mKnowBean = MutableLiveData<BaseListResponse<MutableList<KnowBean>>>()

    fun getKnowledgeTreeJson(page: Int, id: Int?) = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            callRequest {
                handlerResponse(RetrofitFactory.instance.service.getKnowledgeTreeJson(page, id))
            }
        }
        baseResponse.fold({
            mKnowBean.value = it
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