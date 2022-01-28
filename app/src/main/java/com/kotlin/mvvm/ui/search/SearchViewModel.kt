package com.kotlin.mvvm.ui.search

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.kotlin.mvvm.base.BaseViewModel
import com.kotlin.mvvm.common.BaseListResponse
import com.kotlin.mvvm.common.BaseResult
import com.kotlin.mvvm.common.fold
import com.kotlin.mvvm.network.RetrofitFactory
import com.kotlin.mvvm.ui.search.bean.SearchBean
import com.kotlin.mvvm.ui.share.bean.HotKeyBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * description:
 *
 * @author Db_z
 * @Date 2022/1/26 14:13
 */
class SearchViewModel : BaseViewModel() {

    val mK = MutableLiveData<String>()
    val mSearchBean = MutableLiveData<BaseListResponse<MutableList<SearchBean>>>()
    val mHotKeyBean = MutableLiveData<MutableList<HotKeyBean>>()
    val mSearch = MutableLiveData<String>()

    fun getArticleQuery(page: Int, k: String = "") = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            val result = RetrofitFactory.instance.service.getArticleQuery(page, k)
            if (result.errorCode == 0) {
                BaseResult.success(result.data)
            } else {
                BaseResult.failure(Throwable("Failed to getArticleQuery${result.errorMsg}"))
            }
        }
        mK.value = k
        handlerCode.value = page
        baseResponse.fold({
            mSearchBean.value = it
        }, {
            ToastUtils.showShort(it.message)
        })
    }

    fun getSearchHotKeyJson() = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            val result = RetrofitFactory.instance.service.getSearchHotKeyJson()
            if (result.errorCode == 0) {
                BaseResult.success(result.data)
            } else {
                BaseResult.failure(Throwable("Failed to getSearchHotKeyJson${result.errorMsg}"))
            }
        }
        baseResponse.fold({
            mHotKeyBean.value = it
        }, {
            ToastUtils.showShort(it.message)
        })
    }

    /**
     * 点击搜索热词、历史记录搜索
     */
    fun responseSearch(k: String){
        mSearch.value = k
    }
}