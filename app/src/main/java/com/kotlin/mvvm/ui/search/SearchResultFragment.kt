package com.kotlin.mvvm.ui.search

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kotlin.mvvm.base.BaseFragment
import com.kotlin.mvvm.common.ScrollToTop
import com.kotlin.mvvm.databinding.FragmentSearchResultBinding
import com.kotlin.mvvm.ext.setLinearLayoutManager

/**
 * description:
 *
 * @author Db_z
 * @Date 2022/1/26 14:33
 */
class SearchResultFragment : BaseFragment(), ScrollToTop {

    private val binding by lazy { FragmentSearchResultBinding.inflate(layoutInflater) }
    private val mViewModel by activityViewModels<SearchViewModel>()
    private val mAdapter by lazy { SearchAdapter() }
    private var page = 0
    private var k = ""

    override fun getContentView() = binding.root

    override fun initView(bundle: Bundle?) {
        setLoadSir(binding.refreshLayout)
        binding.refreshLayout.setEnableLoadMore(false)
        binding.recyclerView.setLinearLayoutManager(mAdapter)
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.ScaleIn)
        binding.refreshLayout.setOnRefreshListener {
            page = 0
            mViewModel.getArticleQuery(page, k)
        }
        mAdapter.loadMoreModule.setOnLoadMoreListener {
            page++
            mViewModel.getArticleQuery(page, k)
        }
        mViewModel.handlerCode.observe(this) { page = it }
        mViewModel.mK.observe(this) { k = it }
        mViewModel.mSearchBean.observe(this) {
            if (it.datas.isNotEmpty()) {
                if (page == 0) {
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
    }

    override fun initData() {

    }

    override fun scrollToTop() {
        binding.recyclerView.smoothScrollToPosition(0)
    }
}