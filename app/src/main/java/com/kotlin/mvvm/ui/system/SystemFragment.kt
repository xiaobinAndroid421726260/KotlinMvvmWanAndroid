package com.kotlin.mvvm.ui.system

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.flyco.tablayout.listener.OnTabSelectListener
import com.kotlin.mvvm.base.BaseFragment
import com.kotlin.mvvm.common.ScrollToTop
import com.kotlin.mvvm.databinding.FragmentSystemBinding

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/14 11:16
 */
class SystemFragment : BaseFragment(), ScrollToTop {

    private val binding by lazy { FragmentSystemBinding.inflate(layoutInflater) }
    private var mAdapter: FragmentStatePagerAdapter? = null
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
        binding.tabLayout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                binding.viewPager.currentItem = position
            }

            override fun onTabReselect(position: Int) {

            }
        })
        mAdapter = object : FragmentStatePagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getCount() = mFragments.size

            override fun getItem(position: Int) = mFragments[position]
        }
        binding.viewPager.adapter = mAdapter
        binding.tabLayout.setViewPager(binding.viewPager, mTitles.toTypedArray())
    }

    override fun initData() {
    }

    override fun scrollToTop() {
        val fragment = mAdapter?.getItem(binding.viewPager.currentItem)
        if (fragment is ScrollToTop){
            fragment.scrollToTop()
        }
    }
}