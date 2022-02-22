package com.kotlin.mvvm.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kotlin.mvvm.R
import com.kotlin.mvvm.base.BaseFragment
import com.kotlin.mvvm.common.ScrollToTop
import com.kotlin.mvvm.common.handler_code_collect
import com.kotlin.mvvm.common.handler_code_un_collect
import com.kotlin.mvvm.databinding.FragmentHomeBinding
import com.kotlin.mvvm.ext.setLinearLayoutManager
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.indicator.CircleIndicator

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/7 11:43
 */
class HomeFragment : BaseFragment(), ScrollToTop {

    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<HomeViewModel>()
    private val mAdapter by lazy { HomeAdapter() }
    private var page = 0
    private var position = 0

    override fun getContentView() = binding.root

    override fun initView(bundle: Bundle?) {
        setLoadSir(binding.refreshLayout)
        binding.recyclerView.setLinearLayoutManager(mAdapter)
        val headerView = getHeaderView()
        val banner =
            headerView.findViewById<Banner<String, BannerAdapter<String, HomeBannerAdapter.BannerViewHolder>>>(
                R.id.banner
            )
        banner.indicator = CircleIndicator(activity)
        mAdapter.isAnimationFirstOnly = true
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.ScaleIn)
        mAdapter.addHeaderView(headerView)
        binding.refreshLayout.setEnableLoadMore(false)
        binding.refreshLayout.setOnRefreshListener {
            page = 0
            mViewModel.getBannerJson()
        }
        mAdapter.loadMoreModule.setOnLoadMoreListener {
            page++
            mViewModel.getTopBeanJson(page)
        }
        mAdapter.setCollectionListener { collect, id, position ->
            this.position = position
            if (collect) {
                mViewModel.unCollectList(id)
            } else {
                mViewModel.collect(id)
            }
        }
        mViewModel.run {
            mBannerLists.observe(this@HomeFragment) {
                banner.adapter = HomeBannerAdapter(it)
                mViewModel.getTopBeanJson(page)
            }
            mDataBeans.observe(this@HomeFragment) {
                if (it.curPage == 1) {
                    if (it.datas.isEmpty()){
                        showEmpty()
                    } else {
                        mAdapter.setList(it.datas)
                        showContent()
                    }
                    binding.refreshLayout.finishRefresh(true)
                } else {
                    mAdapter.addData(it.datas)
                    showContent()
                }
                mAdapter.loadMoreModule.loadMoreComplete()
                if (it.curPage == it.pageCount) {
                    mAdapter.loadMoreModule.loadMoreEnd()
                }
            }
            handlerCode.observe(this@HomeFragment) {
                when (it) {
                    handler_code_collect -> {
                        mAdapter.data[position].collect = true
                        ToastUtils.showShort(StringUtils.getString(R.string.collect_success))
                    }
                    handler_code_un_collect -> {
                        mAdapter.data[position].collect = false
                        ToastUtils.showShort(StringUtils.getString(R.string.cancel_collect))
                    }
                }
                mAdapter.notifyItemChanged(position + mAdapter.headerLayoutCount)
            }
        }
    }

    override fun initData() {
        mViewModel.getBannerJson()
    }

    private fun getHeaderView(): View {
        return LayoutInflater.from(activity)
            .inflate(R.layout.layout_header_home, binding.recyclerView, false)
    }

    override fun scrollToTop() {
        binding.recyclerView.smoothScrollToPosition(0)
    }
}