package com.kotlin.mvvm.ui.share

import android.os.Bundle
import androidx.activity.viewModels
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kotlin.mvvm.R
import com.kotlin.mvvm.base.BaseActivity
import com.kotlin.mvvm.databinding.ActivityShareListBinding
import com.kotlin.mvvm.ext.onClick
import com.kotlin.mvvm.ext.setLinearLayoutManager
import com.kotlin.mvvm.ext.setToolbarBackColor

class ShareListActivity : BaseActivity() {

    private val binding by lazy { ActivityShareListBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<ShareViewModel>()
    private val mAdapter by lazy { ShareAdapter() }
    private var page = 1

    override fun getContentView() = binding.root

    override fun initView(bundle: Bundle?) {
        setLoadSir(binding.refreshLayout)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = StringUtils.getString(R.string.my_share)
        binding.refreshLayout.setEnableLoadMore(false)
        binding.recyclerView.setLinearLayoutManager(mAdapter)
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.ScaleIn)
        binding.refreshLayout.setOnRefreshListener {
            page = 1
            mViewModel.getUserShareList(page)
        }
        mAdapter.loadMoreModule.setOnLoadMoreListener {
            page++
            mViewModel.getUserShareList(page)
        }
        binding.actionButton.onClick { binding.recyclerView.smoothScrollToPosition(0) }
        mViewModel.mShareBean.observe(this) {
            if (it.shareArticles.datas.isNotEmpty()) {
                if (it.shareArticles.curPage == 1) {
                    mAdapter.setList(it.shareArticles.datas)
                    binding.refreshLayout.finishRefresh(true)
                } else {
                    mAdapter.addData(it.shareArticles.datas)
                }
                mAdapter.loadMoreModule.loadMoreComplete()
                if (it.shareArticles.curPage == it.shareArticles.pageCount) {
                    mAdapter.loadMoreModule.loadMoreEnd()
                }
                showContent()
            } else {
                showEmpty()
            }
        }
        mViewModel.getUserShareList(page)
    }

    override fun initData() {
        setToolbarBackColor(this, binding.toolbar, binding.actionButton)
    }
}