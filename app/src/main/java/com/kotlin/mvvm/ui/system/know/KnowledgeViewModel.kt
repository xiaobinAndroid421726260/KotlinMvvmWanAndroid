package com.kotlin.mvvm.ui.system.know

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.kotlin.mvvm.base.BaseViewModel
import com.kotlin.mvvm.common.BaseListResponse
import com.kotlin.mvvm.common.BaseResult
import com.kotlin.mvvm.common.fold
import com.kotlin.mvvm.ext.e
import com.kotlin.mvvm.network.RetrofitFactory
import com.kotlin.mvvm.ui.system.bean.Article
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
            val result = RetrofitFactory.instance.service.getKnowledgeTreeJson(page, id)
            if (result.errorCode == 0) {
                BaseResult.success(result.data)
            } else {
                BaseResult.failure(Throwable("Failed to getKnowledgeTreeJson${result.errorMsg}"))
            }
        }
        baseResponse.fold({
            mKnowBean.value = it
        }, {
            ToastUtils.showShort(it.message)
        })
    }
}