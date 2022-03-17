package com.kotlin.mvvm.ui.project

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
import com.kotlin.mvvm.ui.project.bean.ProjectBean
import com.kotlin.mvvm.ui.project.bean.ProjectPagerBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/24 9:53
 */
class ProjectViewModel : BaseViewModel() {

    val mProjectBean = MutableLiveData<MutableList<ProjectBean>>()
    val mProjectPagerBean = MutableLiveData<BaseListResponse<MutableList<ProjectPagerBean>>>()

    fun getProjectTreeJson() = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            callRequest { handlerResponse(RetrofitFactory.instance.service.getProjectTreeJson()) }
        }
        baseResponse.fold({
            mProjectBean.value = it
        }, {
            ToastUtils.showShort(it.message)
        })
    }

    fun getProjectCidJson(page: Int, cid: Int?) = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            callRequest { handlerResponse(RetrofitFactory.instance.service.getProjectCidJson(page, cid)) }
        }
        baseResponse.fold({
            mProjectPagerBean.value = it
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