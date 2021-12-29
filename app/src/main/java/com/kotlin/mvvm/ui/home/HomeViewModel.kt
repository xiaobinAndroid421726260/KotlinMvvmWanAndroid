package com.kotlin.mvvm.ui.home

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ToastUtils
import com.kotlin.mvvm.base.BaseViewModel
import com.kotlin.mvvm.common.BaseListResponse
import com.kotlin.mvvm.common.BaseResult
import com.kotlin.mvvm.common.UiState
import com.kotlin.mvvm.common.fold
import com.kotlin.mvvm.network.RetrofitFactory
import com.kotlin.mvvm.ui.home.bean.BannerBean
import com.kotlin.mvvm.ui.home.bean.HomeBean
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.*

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/7 11:46
 */
class HomeViewModel : BaseViewModel() {

    private val bannerUrl = arrayListOf<String>()
    val mBannerLists = MutableLiveData<MutableList<String>>()
    val mHomeBeans = arrayListOf<HomeBean>()
    val mDataBeans = MutableLiveData<BaseListResponse<MutableList<HomeBean>>>()

    fun getBannerJson() = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            val bannerBean = RetrofitFactory.instance.service.getBannerJson()
            if (bannerBean.errorCode == 0) {
                BaseResult.success(bannerBean.data)
            } else {
                BaseResult.failure(Throwable("Failed to getBannerJson${bannerBean.errorMsg}"))
            }
        }
        baseResponse.fold({
            setBannerUrl(it)
        }, {
            ToastUtils.showShort(it.message)
        })
    }

    private fun setBannerUrl(data: List<BannerBean>) {
        if (data.isNotEmpty()) {
            Observable.fromIterable(data).subscribe(object : Observer<BannerBean> {

                override fun onSubscribe(d: Disposable) {
                    bannerUrl.clear()
                    if (null != mBannerLists.value) {
                        mBannerLists.value?.clear()
                    }
                }

                override fun onNext(t: BannerBean) {
                    bannerUrl.add(t.imagePath)
                }

                override fun onError(e: Throwable) {
                    uiState.value = UiState.LoadError
                }

                override fun onComplete() {
                    mBannerLists.value = bannerUrl
                }
            })
        }
    }

    fun getTopBeanJson(page: Int) = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            val topJson = RetrofitFactory.instance.service.getTopJson()
            if (topJson.errorCode == 0) {
                BaseResult.success(topJson.data)
            } else {
                BaseResult.failure(Throwable("Failed to getTopJson${topJson.errorMsg}"))
            }
        }
        baseResponse.fold({
            reHomeBeanData(it, page)
        }, {
            ToastUtils.showShort(it.message)
        })
    }

    private fun reHomeBeanData(data: List<HomeBean>, page: Int) {
        if (data.isNotEmpty()) {
            Observable.fromIterable(data).subscribe(object : Observer<HomeBean> {
                override fun onSubscribe(d: Disposable) {
                    mHomeBeans.clear()
                }

                override fun onNext(t: HomeBean) {
                    t.top = true
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {
                    if (page == 0) {
                        mHomeBeans.clear()
                        mHomeBeans.addAll(data)
                    }
                    getArticleJson(page)
                }
            })
        }
    }

    private fun getArticleJson(page: Int) = launchUI {
        val baseResponse = withContext(Dispatchers.IO) {
            val articleJson = RetrofitFactory.instance.service.getArticleJson(page)
            if (articleJson.errorCode == 0) {
                BaseResult.success(articleJson.data)
            } else {
                BaseResult.failure(Throwable("Failed to getArticleJson${articleJson.errorMsg}"))
            }
        }
        baseResponse.fold({
            mHomeBeans.addAll(it.datas)
            it.datas.clear()
            it.datas.addAll(mHomeBeans)
            mDataBeans.value = it
        }, {
            ToastUtils.showShort(it.message)
        })
    }
}