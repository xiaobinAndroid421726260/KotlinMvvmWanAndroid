package com.kotlin.mvvm.ui.system

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.kotlin.mvvm.base.BaseFragment
import com.kotlin.mvvm.common.ScrollToTop
import com.kotlin.mvvm.databinding.FragmentSystemBinding
import com.kotlin.mvvm.ext.setTaLayoutViewTextColor

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/14 11:16
 */
class SystemFragment : BaseFragment(), ScrollToTop {

    private val binding by lazy { FragmentSystemBinding.inflate(layoutInflater) }
    private var mAdapter: FragmentStateAdapter? = null
    private val mFragments = arrayListOf<Fragment>().apply {
        add(SystemOneFragment())
        add(SystemTwoFragment())
    }
    private val mTitles = arrayListOf<String>().apply {
        add("体系")
        add("导航")
    }

    override fun getContentView() = binding.root

    override fun initView(bundle: Bundle?) {
        mAdapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = mFragments.size

            override fun createFragment(position: Int) = mFragments[position]
        }
        binding.viewPager2.adapter = mAdapter
        binding.viewPager2.offscreenPageLimit = mFragments.size
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = mTitles[position]
        }.attach()
    }

    override fun initData() {
        setTaLayoutViewTextColor(binding.tabLayout)
    }

    override fun scrollToTop() {
        val fragment = mAdapter?.createFragment(binding.viewPager2.currentItem)
        if (fragment is ScrollToTop) {
            fragment.scrollToTop()
        }
    }
}