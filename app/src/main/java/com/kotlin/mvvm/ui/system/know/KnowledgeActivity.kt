package com.kotlin.mvvm.ui.system.know

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.blankj.utilcode.util.ActivityUtils
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
    private var mAdapter: FragmentStatePagerAdapter? = null
    private val mFragment = arrayListOf<Fragment>()
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
        binding.toolbar.title = name
        binding.toolbar.setTitleTextColor(
            if (getAppThemeColor() == R.color.white) ContextCompat.getColor(
                this,
                R.color.black
            ) else ContextCompat.getColor(this, R.color.white)
        )
        binding.toolbar.setNavigationIcon(
            if (getAppThemeColor() == R.color.white)
                R.drawable.ic_arrow_black else R.drawable.ic_arrow_white
        )
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
        val mTitles = arrayListOf<String>()
        for (data in datas) {
            mTitles.add(data.name)
            mFragment.add(KnowledgeFragment.newInstance(data.id))
        }
        mAdapter = object : FragmentStatePagerAdapter(
            supportFragmentManager,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {
            override fun getCount() = mFragment.size

            override fun getItem(position: Int) = mFragment[position]
        }
        binding.viewPager.offscreenPageLimit = mFragment.size
        binding.viewPager.adapter = mAdapter
        binding.tabLayout.setViewPager(binding.viewPager, mTitles.toTypedArray())
        binding.actionButton.setOnClickListener {
            val fragment = mAdapter?.getItem(binding.viewPager.currentItem)
            if (fragment is ScrollToTop) {
                fragment.scrollToTop()
            }
        }
    }

    override fun initData() {
        setToolbarBackColor(this, binding.toolbar, binding.actionButton)
    }
}