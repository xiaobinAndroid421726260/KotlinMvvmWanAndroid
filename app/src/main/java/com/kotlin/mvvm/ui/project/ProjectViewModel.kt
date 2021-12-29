package com.kotlin.mvvm.ui.project

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.kotlin.mvvm.base.BaseViewModel
import com.kotlin.mvvm.common.BaseListResponse
import com.kotlin.mvvm.common.BaseResult
import com.kotlin.mvvm.common.fold
import com.kotlin.mvvm.network.RetrofitFactory
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
            val projectBean = RetrofitFactory.instance.service.getProjectTreeJson()
            if (projectBean.errorCode == 0) {
                BaseResult.success(projectBean.data)
            } else {
                BaseResult.failure(Throwable("Failed to getProjectTreeJson${projectBean.errorMsg}"))
            }
        }
        baseResponse.fold({
            mProjectBean.value = it
        }, {
            ToastUtils.showShort(it.message)
        })
    }

    fun getProjectCidJson(page: Int, cid: Int?) = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            val projectPagerBean = RetrofitFactory.instance.service.getProjectCidJson(page, cid)
            if (projectPagerBean.errorCode == 0) {
                BaseResult.success(projectPagerBean.data)
            } else {
                BaseResult.failure(Throwable("Failed to getProjectCidJson${projectPagerBean.errorMsg}"))
            }
        }
        baseResponse.fold({
            mProjectPagerBean.value = it
        }, {
            ToastUtils.showShort(it.message)
        })
    }
}