package com.kotlin.mvvm.ui.integral

import android.os.Bundle
import androidx.activity.viewModels
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kotlin.mvvm.R
import com.kotlin.mvvm.base.BaseActivity
import com.kotlin.mvvm.databinding.ActivityIntegralRankBinding
import com.kotlin.mvvm.ext.onClick
import com.kotlin.mvvm.ext.setLinearLayoutManager
import com.kotlin.mvvm.ext.setToolbarBackColor

class IntegralRankActivity : BaseActivity() {

    private val binding by lazy { ActivityIntegralRankBinding.inflate(layoutInflater) }
    private val mAdapter by lazy { IntegralAdapter() }
    private val mViewModel by viewModels<IntegralViewModel>()
    private var page = 1

    override fun getContentView() = binding.root

    override fun initView(bundle: Bundle?) {
        setLoadSir(binding.refreshLayout)
        setSupportActionBar(binding.layoutTitle.toolbar)
        supportActionBar?.title = StringUtils.getString(R.string.integral_ranking)
        binding.layoutTitle.toolbar.setNavigationOnClickListener { onBackPressed() }
        binding.refreshLayout.setEnableLoadMore(false)
        binding.recyclerView.setLinearLayoutManager(mAdapter)
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.ScaleIn)
        binding.refreshLayout.setOnRefreshListener {
            page = 1
            mViewModel.getIntegralRank(page)
        }
        mAdapter.loadMoreModule.setOnLoadMoreListener {
            page++
            mViewModel.getIntegralRank(page)
        }
        binding.actionButton.onClick { binding.recyclerView.smoothScrollToPosition(0) }
        mViewModel.mRankBean.observe(this) {
            if (it.datas.isNotEmpty()) {
                if (it.curPage == 1) {
                    mAdapter.setList(it.datas)
                    binding.refreshLayout.finishRefresh(true)
                } else {
                    mAdapter.addData(it.datas)
                }
                showContent()
                mAdapter.loadMoreModule.loadMoreComplete()
                if (it.curPage == it.pageCount) {
                    mAdapter.loadMoreModule.loadMoreEnd()
                }
            } else {
                showEmpty()
            }
        }
        mViewModel.getIntegralRank(page)
    }

    override fun initData() {
        setToolbarBackColor(this, binding.layoutTitle.toolbar, binding.actionButton)
    }
}