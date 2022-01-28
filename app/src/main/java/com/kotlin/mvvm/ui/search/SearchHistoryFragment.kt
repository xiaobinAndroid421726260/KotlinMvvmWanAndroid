package com.kotlin.mvvm.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kotlin.mvvm.base.BaseFragment
import com.kotlin.mvvm.databinding.FragmentHistorySearchBinding
import com.kotlin.mvvm.databinding.FragmentSystemItemTwoFlowBinding
import com.kotlin.mvvm.ext.deleteSearchHistoryData
import com.kotlin.mvvm.ext.getSearchHistoryData
import com.kotlin.mvvm.ext.setLinearLayoutManager
import com.kotlin.mvvm.ui.share.bean.HotKeyBean
import com.kotlin.mvvm.view.flowlayout.FlowLayout
import com.kotlin.mvvm.view.flowlayout.TagAdapter

/**
 * description:
 *
 * @author Db_z
 * @Date 2022/1/26 14:35
 */
class SearchHistoryFragment : BaseFragment() {

    private val binding by lazy { FragmentHistorySearchBinding.inflate(layoutInflater) }
    private val mViewModel by activityViewModels<SearchViewModel>()
    private val mAdapter by lazy { HistoryAdapter() }

    override fun getContentView() = binding.root

    override fun initView(bundle: Bundle?) {
        binding.recyclerView.setLinearLayoutManager(mAdapter)
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.ScaleIn)
        mAdapter.setOnDeleteListener {
            deleteSearchHistoryData(it)
            mAdapter.setList(getSearchHistoryData())
        }
        mAdapter.setOnSearchListener { mViewModel.responseSearch(it) }
        mViewModel.mHotKeyBean.observe(this){
            val adapter = object : TagAdapter<HotKeyBean>(it) {
                override fun getView(parent: FlowLayout?, position: Int, t: HotKeyBean): View {
                    val binding = FragmentSystemItemTwoFlowBinding.inflate(LayoutInflater.from(context), parent, false)
                    binding.tvContent.text = t.name
                    return binding.root
                }
            }
            binding.flowlayout.setAdapter(adapter)
            binding.flowlayout.setOnTagClickListener { _, position, _ ->
                mViewModel.responseSearch(adapter.data[position as Int].name)
            }
        }
        mViewModel.getSearchHotKeyJson()
    }

    override fun initData() {
        mAdapter.setList(getSearchHistoryData())
    }
}