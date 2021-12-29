package com.kotlin.mvvm.ui.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.flyco.tablayout.listener.OnTabSelectListener
import com.kotlin.mvvm.base.BaseFragment
import com.kotlin.mvvm.common.ScrollToTop
import com.kotlin.mvvm.databinding.FragmentProjectBinding
import com.kotlin.mvvm.ext.e
import com.kotlin.mvvm.ui.project.bean.ProjectBean

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/14 11:17
 */
class ProjectFragment : BaseFragment(), ScrollToTop {

    private val binding by lazy { FragmentProjectBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<ProjectViewModel>()
    private var mAdapter: FragmentStatePagerAdapter? = null

    override fun getContentView() = binding.root

    override fun initView(bundle: Bundle?) {
        mViewModel.mProjectBean.observe(this) {
            if (it.isNotEmpty()){
                onCreateTagFragment(it)
            }
        }
        mViewModel.getProjectTreeJson()
    }

    private fun onCreateTagFragment(dataBean: MutableList<ProjectBean>){
        val mTitles = arrayListOf<String>()
        val mFragments = arrayListOf<Fragment>()
        for (i in dataBean.indices){
            mTitles.add(dataBean[i].name)
            mFragments.add(ProjectViewPagerFragment.newInstance(dataBean[i].id))
        }
        mAdapter = object : FragmentStatePagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getCount() = mFragments.size

            override fun getItem(position: Int) = mFragments[position]
        }
        binding.viewPager.adapter = mAdapter
        binding.viewPager.offscreenPageLimit = dataBean.size
        binding.tabLayout.setViewPager(binding.viewPager, mTitles.toTypedArray())
        binding.tabLayout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                binding.viewPager.currentItem = position
            }

            override fun onTabReselect(position: Int) {

            }
        })
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