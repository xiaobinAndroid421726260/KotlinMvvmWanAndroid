package com.kotlin.mvvm.ui.wechat

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.kotlin.mvvm.base.BaseViewModel
import com.kotlin.mvvm.common.*
import com.kotlin.mvvm.network.RetrofitFactory
import com.kotlin.mvvm.ui.wechat.bean.WechatBean
import com.kotlin.mvvm.ui.wechat.bean.WechatPagerBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/22 15:57
 */
class WechatViewModel : BaseViewModel() {

    val mWechatBean = MutableLiveData<MutableList<WechatBean>>()
    val mWechatPagerBean = MutableLiveData<BaseListResponse<MutableList<WechatPagerBean>>>()

    fun getWechatArticleJson() = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            val wechatBean = RetrofitFactory.instance.service.getWechatArticleJson()
            if (wechatBean.errorCode == 0) {
                BaseResult.success(wechatBean.data)
            } else {
                BaseResult.failure(Throwable("Failed to getWechatArticleJson${wechatBean.errorMsg}"))
            }
        }
        baseResponse.fold({
            mWechatBean.value = it
        }, {
            ToastUtils.showShort(it.message)
        })
    }

    fun getUserWechatArticleJson(user_id: Int?, page: Int) = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            val articleBean =
                RetrofitFactory.instance.service.getUserWechatArticleJson(user_id, page)
            if (articleBean.errorCode == 0) {
                BaseResult.success(articleBean.data)
            } else {
                BaseResult.failure(Throwable("Failed to getUserWechatArticleJson${articleBean.errorMsg}"))
            }
        }
        baseResponse.fold({
            mWechatPagerBean.value = it
        }, {
            ToastUtils.showShort(it.message)
        })
    }
}