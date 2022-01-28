package com.kotlin.mvvm.ui.questions

import android.os.Bundle
import androidx.activity.viewModels
import com.blankj.utilcode.util.StringUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kotlin.mvvm.R
import com.kotlin.mvvm.base.BaseActivity
import com.kotlin.mvvm.databinding.ActivityQuestionsAnswersBinding
import com.kotlin.mvvm.ext.onClick
import com.kotlin.mvvm.ext.setLinearLayoutManager
import com.kotlin.mvvm.ext.setToolbarBackColor
import com.kotlin.mvvm.ui.my.MyAdapter
import com.kotlin.mvvm.ui.my.MyViewModel

class QuestionsAnswersActivity : BaseActivity() {

    private val binding by lazy { ActivityQuestionsAnswersBinding.inflate(layoutInflater) }
    private val mAdapter by lazy { MyAdapter() }
    private val mViewModel by viewModels<MyViewModel>()
    private var page = 0

    override fun getContentView() = binding.root

    override fun initView(bundle: Bundle?) {
        setLoadSir(binding.refreshLayout)
        setSupportActionBar(binding.layoutTitle.toolbar)
        supportActionBar?.title = StringUtils.getString(R.string.questions_and_answers)
        binding.refreshLayout.setEnableLoadMore(false)
        binding.refreshLayout.setOnRefreshListener {
            page = 0
            mViewModel.getWendListJson(page)
        }
        binding.recyclerView.setLinearLayoutManager(mAdapter)
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.ScaleIn)
        mAdapter.loadMoreModule.setOnLoadMoreListener {
            page++
            mViewModel.getWendListJson(page)
        }
        binding.actionButton.onClick {
            binding.recyclerView.smoothScrollToPosition(0)
        }
        mViewModel.mWendBean.observe(this){
            if (it.datas.isNotEmpty()){
                if (it.curPage == 0){
                    mAdapter.setList(it.datas)
                    binding.refreshLayout.finishRefresh(true)
                } else {
                    mAdapter.addData(it.datas)
                }
                showContent()
                mAdapter.loadMoreModule.loadMoreComplete()
                if (it.curPage == it.pageCount){
                    mAdapter.loadMoreModule.loadMoreEnd()
                }
            } else {
                showEmpty()
            }
        }
        mViewModel.getWendListJson(page)
    }

    override fun initData() {
        setToolbarBackColor(this, binding.layoutTitle.toolbar, binding.actionButton)
    }
}