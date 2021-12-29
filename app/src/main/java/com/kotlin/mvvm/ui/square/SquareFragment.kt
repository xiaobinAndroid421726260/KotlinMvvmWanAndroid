package com.kotlin.mvvm.ui.square

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kotlin.mvvm.base.BaseFragment
import com.kotlin.mvvm.common.ScrollToTop
import com.kotlin.mvvm.databinding.FragmentSquareBinding
import com.kotlin.mvvm.ext.setLinearLayoutManager

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/14 11:11
 */
class SquareFragment : BaseFragment(), ScrollToTop {

    private val binding by lazy { FragmentSquareBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<SquareViewModel>()
    private val mAdapter by lazy { SquareAdapter() }
    private var page = 0

    override fun getContentView() = binding.root

    override fun initView(bundle: Bundle?) {
        setLoadSir(binding.refreshLayout)
        binding.recyclerView.setLinearLayoutManager(mAdapter)
        mAdapter.isAnimationFirstOnly = true
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.ScaleIn)
        binding.refreshLayout.setEnableLoadMore(false)
        binding.refreshLayout.setOnRefreshListener {
            page = 0
            mViewModel.getUserArticleJson(page)
        }
        mAdapter.loadMoreModule.setOnLoadMoreListener {
            page++
            mViewModel.getUserArticleJson(page)
        }
        mViewModel.mDataBeans.observe(this){
            if (it.curPage == 1){
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
    }

    override fun initData() {
        mViewModel.getUserArticleJson(page)
    }

    override fun scrollToTop() {
        binding.recyclerView.smoothScrollToPosition(0)
    }
}