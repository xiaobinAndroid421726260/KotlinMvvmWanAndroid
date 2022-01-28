package com.kotlin.mvvm.ui.wechat

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.kotlin.mvvm.base.BaseFragment
import com.kotlin.mvvm.common.ScrollToTop
import com.kotlin.mvvm.databinding.FragmentWechatBinding
import com.kotlin.mvvm.ext.setTaLayoutViewTextColor
import com.kotlin.mvvm.ui.wechat.bean.WechatBean

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/14 11:12
 */
class WechatFragment : BaseFragment(), ScrollToTop {

    private val binding by lazy { FragmentWechatBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<WechatViewModel>()
    private var mAdapter: FragmentStateAdapter? = null

    override fun getContentView() = binding.root

    override fun initView(bundle: Bundle?) {
        mViewModel.mWechatBean.observe(this) {
            if (it.isNotEmpty() && it.size > 0) {
                onCreateTagFragment(it)
            }
        }
        mViewModel.getWechatArticleJson()
    }

    private fun onCreateTagFragment(dataBean: MutableList<WechatBean>) {
        val mFragments = arrayListOf<Fragment>()
        val mTitles = arrayListOf<String>()
        for (data in dataBean) {
            mTitles.add(data.name)
            mFragments.add(WechatViewPagerFragment.newInstance(data.id))
        }
        mAdapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = mFragments.size

            override fun createFragment(position: Int) = mFragments[position]
        }
        binding.viewPager2.adapter = mAdapter
        binding.viewPager2.offscreenPageLimit = dataBean.size
        val tabLayoutMediator =
            TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
                tab.text = mTitles[position]
            }
        tabLayoutMediator.attach()
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