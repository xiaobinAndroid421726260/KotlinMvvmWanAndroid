package com.kotlin.mvvm.ui.integral

import android.os.Bundle
import androidx.activity.viewModels
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kotlin.mvvm.R
import com.kotlin.mvvm.base.BaseActivity
import com.kotlin.mvvm.databinding.ActivityMyIntegralBinding
import com.kotlin.mvvm.databinding.LayoutHeaderMyIntegralBinding
import com.kotlin.mvvm.ext.onClick
import com.kotlin.mvvm.ext.setLinearLayoutManager
import com.kotlin.mvvm.ext.setToolbarBackColor

class MyIntegralActivity : BaseActivity() {

    private val binding by lazy { ActivityMyIntegralBinding.inflate(layoutInflater) }
    private val mAdapter by lazy { MyIntegralAdapter() }
    private val mViewModel by viewModels<IntegralViewModel>()
    private var page = 1

    override fun getContentView() = binding.root

    override fun initView(bundle: Bundle?) {
        setLoadSir(binding.refreshLayout)
        refreshUiState(mViewModel.uiState)
        setSupportActionBar(binding.layoutTitle.toolbar)
        supportActionBar?.title = StringUtils.getString(R.string.my_integral)
        binding.layoutTitle.toolbar.setNavigationOnClickListener { onBackPressed() }
        binding.refreshLayout.setEnableLoadMore(false)
        binding.recyclerView.setLinearLayoutManager(mAdapter)
        val headerView = LayoutHeaderMyIntegralBinding.inflate(layoutInflater)
        mAdapter.setHeaderView(headerView.root)
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.ScaleIn)
        binding.refreshLayout.setOnRefreshListener {
            page = 1
            mViewModel.getMyIntegralList(page)
        }
        mAdapter.loadMoreModule.setOnLoadMoreListener {
            page++
            mViewModel.getMyIntegralList(page)
        }
        mViewModel.mUserIntegralBean.observe(this){
            headerView.tvTotalIntegral.text = it.coinCount.toString()
            headerView.tvLevelRank.text = getString(R.string.level_rank, it.level, it.rank)
        }
        mViewModel.mIntegralBean.observe(this) {
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
        binding.actionButton.onClick {
            binding.recyclerView.smoothScrollToPosition(0)
        }
        mViewModel.getMyIntegral()
        mViewModel.getMyIntegralList(page)
    }

    override fun initData() {
        setToolbarBackColor(this, binding.layoutTitle.toolbar, binding.actionButton)
    }
}