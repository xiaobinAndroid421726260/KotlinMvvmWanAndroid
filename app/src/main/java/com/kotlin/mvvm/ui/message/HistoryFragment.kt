package com.kotlin.mvvm.ui.message

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kotlin.mvvm.base.BaseFragment
import com.kotlin.mvvm.common.ScrollToTop
import com.kotlin.mvvm.databinding.FragmentHistoryBinding
import com.kotlin.mvvm.ext.setLinearLayoutManager

/**
 * description:
 *
 * @author Db_z
 * @Date 2022/1/25 11:06
 */
class HistoryFragment : BaseFragment(), ScrollToTop {

    private val binding by lazy { FragmentHistoryBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<MessageViewModel>()
    private val mAdapter by lazy { MessageAdapter() }
    private var page = 1

    override fun getContentView() = binding.root

    override fun initView(bundle: Bundle?) {
        setLoadSir(binding.refreshLayout)
        binding.refreshLayout.setEnableLoadMore(false)
        binding.recyclerView.setLinearLayoutManager(mAdapter)
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.ScaleIn)
        binding.refreshLayout.setOnRefreshListener {
            page = 1
            mViewModel.getMessageReadList(page)
        }
        mAdapter.loadMoreModule.setOnLoadMoreListener {
            page++
            mViewModel.getMessageReadList(page)
        }
        mViewModel.mReadListBean.observe(this){
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
        mViewModel.getMessageReadList(page)
    }

    override fun initData() {

    }

    override fun scrollToTop() {
        binding.recyclerView.smoothScrollToPosition(0)
    }
}