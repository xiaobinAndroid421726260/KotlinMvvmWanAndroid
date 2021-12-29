package com.kotlin.mvvm.ui.system.know

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kotlin.mvvm.base.BaseFragment
import com.kotlin.mvvm.common.ScrollToTop
import com.kotlin.mvvm.databinding.FragmentKnowledgeBinding
import com.kotlin.mvvm.ext.e
import com.kotlin.mvvm.ext.setLinearLayoutManager

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/29 11:56
 */
class KnowledgeFragment : BaseFragment(), ScrollToTop {

    private val binding by lazy { FragmentKnowledgeBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<KnowledgeViewModel>()
    private val mAdapter by lazy { KnowledgeAdapter() }
    private var page = 0
    private var cid: Int? = 0

    companion object {
        fun newInstance(id: Int): KnowledgeFragment {
            val fragment = KnowledgeFragment()
            val bundle = Bundle().apply {
                putInt("id", id)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getContentView() = binding.root

    override fun initView(bundle: Bundle?) {
        cid = bundle?.getInt("id", 0)
        setLoadSir(binding.refreshLayout)
        binding.recyclerView.setLinearLayoutManager(mAdapter)
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.ScaleIn)
        binding.refreshLayout.setEnableLoadMore(false)
        binding.refreshLayout.setOnRefreshListener {
            page = 0
            mViewModel.getKnowledgeTreeJson(page, cid)
        }
        mAdapter.loadMoreModule.setOnLoadMoreListener {
            page++
            mViewModel.getKnowledgeTreeJson(page, cid)
        }
        mViewModel.mKnowBean.observe(this) {
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
    }

    override fun initData() {
        page = 0
        mViewModel.getKnowledgeTreeJson(page, cid)
    }

    override fun scrollToTop() {
        binding.recyclerView.smoothScrollToPosition(0)
    }
}