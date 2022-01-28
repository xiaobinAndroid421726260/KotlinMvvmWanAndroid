package com.kotlin.mvvm.ui.message

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blankj.utilcode.util.StringUtils
import com.google.android.material.tabs.TabLayoutMediator
import com.kotlin.mvvm.R
import com.kotlin.mvvm.base.BaseActivity
import com.kotlin.mvvm.common.ScrollToTop
import com.kotlin.mvvm.databinding.ActivityMessageBinding
import com.kotlin.mvvm.ext.onClick
import com.kotlin.mvvm.ext.setTaLayoutViewTextColor
import com.kotlin.mvvm.ext.setToolbarBackColor

class MessageActivity : BaseActivity() {

    private val binding by lazy { ActivityMessageBinding.inflate(layoutInflater) }
    private var mAdapter: FragmentStateAdapter? = null
    private val mFragments = arrayListOf<Fragment>().apply {
        add(NewMessageFragment())
        add(HistoryFragment())
    }
    private val mTitles = arrayListOf<String>().apply {
        add("新消息")
        add("历史消息")
    }

    override fun getContentView() = binding.root

    override fun initView(bundle: Bundle?) {
        setSupportActionBar(binding.layoutTitle.toolbar)
        supportActionBar?.title = StringUtils.getString(R.string.message)
        mAdapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = mFragments.size

            override fun createFragment(position: Int) = mFragments[position]
        }
        binding.viewPager2.adapter = mAdapter
        binding.viewPager2.offscreenPageLimit = mFragments.size
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = mTitles[position]
        }.attach()
        binding.actionButton.onClick {
            val fragment = mAdapter?.createFragment(binding.viewPager2.currentItem)
            if (fragment is ScrollToTop) {
                fragment.scrollToTop()
            }
        }
    }

    override fun initData() {
        setToolbarBackColor(this, binding.layoutTitle.toolbar, binding.actionButton)
        setTaLayoutViewTextColor(binding.tabLayout)
    }
}