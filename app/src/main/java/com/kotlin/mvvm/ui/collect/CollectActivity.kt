package com.kotlin.mvvm.ui.collect

import android.os.Bundle
import androidx.activity.viewModels
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kotlin.mvvm.R
import com.kotlin.mvvm.base.BaseActivity
import com.kotlin.mvvm.common.handler_code_collect
import com.kotlin.mvvm.common.handler_code_un_collect
import com.kotlin.mvvm.databinding.ActivityCollectBinding
import com.kotlin.mvvm.ext.onClick
import com.kotlin.mvvm.ext.setLinearLayoutManager
import com.kotlin.mvvm.ext.setToolbarBackColor

class CollectActivity : BaseActivity() {

    private val binding by lazy { ActivityCollectBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<CollectViewModel>()
    private val mAdapter by lazy { CollectAdapter() }
    private var page = 0
    private var position = 0

    override fun getContentView() = binding.root

    override fun initView(bundle: Bundle?) {
        setLoadSir(binding.refreshLayout)
        setSupportActionBar(binding.layoutTitle.toolbar)
        supportActionBar?.title = StringUtils.getString(R.string.my_collect)
        binding.layoutTitle.toolbar.setNavigationOnClickListener { onBackPressed() }
        binding.refreshLayout.setEnableLoadMore(false)
        binding.recyclerView.setLinearLayoutManager(mAdapter)
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.ScaleIn)
        binding.refreshLayout.setOnRefreshListener {
            page = 0
            mViewModel.getCollectList(page)
        }
        mAdapter.loadMoreModule.setOnLoadMoreListener {
            page++
            mViewModel.getCollectList(page)
        }
        binding.actionButton.onClick { binding.recyclerView.smoothScrollToPosition(0) }
        mAdapter.setCollectionListener { collect, id, originId, position ->
            this.position = position
            if (collect) {
                mViewModel.unCollect(id, originId)
            } else {
                mViewModel.collect(id)
            }
        }
        mViewModel.mCollectBean.observe(this) {
            for (data in it.datas) {
                data.collect = true
            }
            if (it.datas.isNotEmpty()) {
                if (it.curPage == 1) {
                    mAdapter.setList(it.datas)
                    binding.refreshLayout.finishRefresh(true)
                } else {
                    mAdapter.addData(it.datas)
                }
                mAdapter.loadMoreModule.loadMoreComplete()
                if (it.curPage == it.pageCount) {
                    mAdapter.loadMoreModule.loadMoreEnd()
                }
                showContent()
            } else {
                showEmpty()
            }
        }
        mViewModel.handlerCode.observe(this) {
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
            mAdapter.notifyItemChanged(position)
        }
        mViewModel.getCollectList(page)
    }

    override fun initData() {
        setToolbarBackColor(this, binding.layoutTitle.toolbar, binding.actionButton)
    }
}