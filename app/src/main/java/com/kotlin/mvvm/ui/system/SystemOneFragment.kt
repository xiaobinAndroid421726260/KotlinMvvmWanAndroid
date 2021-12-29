package com.kotlin.mvvm.ui.system

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kotlin.mvvm.base.BaseFragment
import com.kotlin.mvvm.common.ScrollToTop
import com.kotlin.mvvm.databinding.FragmentSystemOneBinding
import com.kotlin.mvvm.ext.setLinearLayoutManager

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/27 15:00
 */
class SystemOneFragment : BaseFragment(), ScrollToTop {

    private val binding by lazy { FragmentSystemOneBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<SystemViewModel>()
    private val mAdapter by lazy { SystemOneAdapter() }

    override fun getContentView() = binding.root

    override fun initView(bundle: Bundle?) {
        setLoadSir(binding.refreshLayout)
        binding.recyclerView.setLinearLayoutManager(mAdapter)
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.ScaleIn)
        binding.refreshLayout.setEnableLoadMore(false)
        binding.refreshLayout.setOnRefreshListener { mViewModel.getTreeJson() }
        mViewModel.mSystemBean.observe(this) {
            mAdapter.setList(it)
            binding.refreshLayout.finishRefresh(true)
            showContent()
        }
    }

    override fun initData() {
        mViewModel.getTreeJson()
    }

    override fun scrollToTop() {
        binding.recyclerView.smoothScrollToPosition(0)
    }
}