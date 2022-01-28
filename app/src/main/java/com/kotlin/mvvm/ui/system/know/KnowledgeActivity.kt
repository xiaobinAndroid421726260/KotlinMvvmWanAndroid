package com.kotlin.mvvm.ui.system.know

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.GsonUtils
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.reflect.TypeToken
import com.kotlin.mvvm.base.BaseActivity
import com.kotlin.mvvm.common.ScrollToTop
import com.kotlin.mvvm.databinding.ActivityKnowledgeBinding
import com.kotlin.mvvm.ext.setTaLayoutViewTextColor
import com.kotlin.mvvm.ext.setToolbarBackColor
import com.kotlin.mvvm.ui.system.bean.Children

class KnowledgeActivity : BaseActivity() {

    private val binding by lazy { ActivityKnowledgeBinding.inflate(layoutInflater) }
    private var mAdapter: FragmentStateAdapter? = null
    private var dataJson: String? = null
    private var name: String? = null

    companion object {
        fun newInstance(name: String, data: String) {
            val bundle = Bundle().apply {
                putString("name", name)
                putString("data", data)
            }
            ActivityUtils.startActivity(bundle, KnowledgeActivity::class.java)
        }
    }

    override fun getContentView() = binding.root

    override fun initView(bundle: Bundle?) {
        bundle?.apply {
            name = getString("name")
            dataJson = getString("data")
        }
        val dataList = GsonUtils.fromJson<MutableList<Children>>(dataJson, object : TypeToken<MutableList<Children>>(){}.type)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = name
        val mFragment = arrayListOf<Fragment>()
        val mTitles = arrayListOf<String>()
        for (data in dataList) {
            mTitles.add(data.name)
            mFragment.add(KnowledgeFragment.newInstance(data.id))
        }
        mAdapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = mFragment.size

            override fun createFragment(position: Int) = mFragment[position]
        }
        binding.viewPager2.adapter = mAdapter
        binding.viewPager2.offscreenPageLimit = mFragment.size
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = mTitles[position]
        }.attach()
        binding.actionButton.setOnClickListener {
            val fragment = mAdapter?.createFragment(binding.viewPager2.currentItem)
            if (fragment is ScrollToTop) {
                fragment.scrollToTop()
            }
        }
    }

    override fun initData() {
        setToolbarBackColor(this, binding.toolbar, binding.actionButton)
        setTaLayoutViewTextColor(binding.tabLayout)
    }
}