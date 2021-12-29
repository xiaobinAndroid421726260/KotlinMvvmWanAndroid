package com.kotlin.mvvm.ui.wechat

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.kotlin.mvvm.base.BaseFragment
import com.kotlin.mvvm.common.ScrollToTop
import com.kotlin.mvvm.ext.setTaLayoutViewTextColor
import com.kotlin.mvvm.databinding.FragmentWechatBinding

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/14 11:12
 */
class WechatFragment : BaseFragment(), ScrollToTop {

    private val binding by lazy { FragmentWechatBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<WechatViewModel>()
    private val mAdapter by lazy { FragmentAdapter(childFragmentManager) }

    override fun getContentView() = binding.root

    override fun initView(bundle: Bundle?) {
        binding.viewPager.adapter = mAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
        mViewModel.mWechatBean.observe(this) {
            if (it.isNotEmpty() && it.size > 0) {
                binding.viewPager.offscreenPageLimit = it.size
                mAdapter.addData(it)
            }
        }
        mViewModel.getWechatArticleJson()
    }

    override fun initData() {
        setTaLayoutViewTextColor(binding.tabLayout)
    }

    override fun scrollToTop() {
        val fragment = mAdapter.getItem(binding.viewPager.currentItem)
        if (fragment is ScrollToTop){
            fragment.scrollToTop()
        }
    }
}