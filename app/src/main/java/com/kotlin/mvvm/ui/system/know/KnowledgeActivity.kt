package com.kotlin.mvvm.ui.system.know

import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.blankj.utilcode.util.ActivityUtils
import com.google.android.material.tabs.TabLayoutMediator
import com.kotlin.mvvm.R
import com.kotlin.mvvm.base.BaseActivity
import com.kotlin.mvvm.common.ScrollToTop
import com.kotlin.mvvm.databinding.ActivityKnowledgeBinding
import com.kotlin.mvvm.ext.getAppThemeColor
import com.kotlin.mvvm.ext.setTaLayoutViewTextColor
import com.kotlin.mvvm.ext.setToolbarBackColor
import com.kotlin.mvvm.ui.system.bean.Children
import java.io.Serializable

class KnowledgeActivity : BaseActivity() {

    private val binding by lazy { ActivityKnowledgeBinding.inflate(layoutInflater) }
    private var mAdapter: FragmentStateAdapter? = null
    private var datas = mutableListOf<Children>()
    private var name: String? = null

    companion object {
        fun newInstance(name: String, data: MutableList<Children>) {
            val bundle = Bundle().apply {
                putString("name", name)
                putSerializable("data", data as Serializable)
            }
            ActivityUtils.startActivity(bundle, KnowledgeActivity::class.java)
        }
    }

    override fun getContentView() = binding.root

    override fun initView(bundle: Bundle?) {
        bundle?.apply {
            name = getString("name")
            datas = getSerializable("data") as MutableList<Children>
        }
        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = name
        binding.toolbar.setTitleTextColor(
            if (getAppThemeColor() == Color.WHITE) ContextCompat.getColor(
                this,
                R.color.black
            ) else ContextCompat.getColor(this, R.color.white)
        )
        binding.toolbar.setNavigationIcon(
            if (getAppThemeColor() == Color.WHITE)
                R.drawable.ic_arrow_black else R.drawable.ic_arrow_white
        )
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        val mFragment = arrayListOf<Fragment>()
        val mTitles = arrayListOf<String>()
        for (data in datas) {
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