package com.kotlin.mvvm.ui.wechat

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kotlin.mvvm.base.BaseFragment
import com.kotlin.mvvm.common.ScrollToTop
import com.kotlin.mvvm.databinding.FragmentWechatViewpagerBinding
import com.kotlin.mvvm.ext.*

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/22 17:01
 */
class WechatViewPagerFragment : BaseFragment(), ScrollToTop {

    private val binding by lazy { FragmentWechatViewpagerBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<WechatViewModel>()
    private val mAdapter by lazy { WechatAdapter() }
    private var id: Int? = 0
    private var page = 1

    companion object {
        fun newInstance(id: Int): WechatViewPagerFragment {
            val fragment = WechatViewPagerFragment()
            val bundle = Bundle().apply {
                putInt("id", id)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getContentView() = binding.root

    override fun initView(bundle: Bundle?) {
        id = bundle?.getInt("id", 0)
        setLoadSir(binding.refreshLayout)
        binding.recyclerView.setLinearLayoutManager(mAdapter)
        mAdapter.isAnimationFirstOnly = true
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.ScaleIn)
        binding.refreshLayout.setEnableLoadMore(false)
        binding.refreshLayout.setOnRefreshListener {
            page = 1
            mViewModel.getUserWechatArticleJson(id, page)
        }
        mAdapter.loadMoreModule.setOnLoadMoreListener {
            page++
            mViewModel.getUserWechatArticleJson(id, page)
        }
        mViewModel.mWechatPagerBean.observe(this) {
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
        page = 1
        mViewModel.getUserWechatArticleJson(id, page)
    }

    override fun scrollToTop() {
        binding.recyclerView.smoothScrollToPosition(0)
    }
}