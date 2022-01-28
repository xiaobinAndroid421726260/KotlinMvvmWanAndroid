package com.kotlin.mvvm.ui.project

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.kotlin.mvvm.ui.project.bean.ProjectBean

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/24 11:06
 */
class ProjectViewPagerAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(
        fragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {

    private val mDataBean = arrayListOf<ProjectBean>()
    private val mFragments = arrayListOf<Fragment>()

    fun addData(dataBean: MutableList<ProjectBean>, isRefresh: Boolean = true) {
        if (isRefresh) {
            clear()
        }
        mDataBean.addAll(dataBean)
        for (i in mDataBean.indices) {
            mFragments.add(ProjectViewPagerFragment.newInstance(mDataBean[i].id))
        }
        notifyDataSetChanged()
    }

    private fun clear() {
        mDataBean.clear()
        mFragments.clear()
        notifyDataSetChanged()
    }

    override fun getCount() = mFragments.size

    override fun getItem(position: Int) = mFragments[position]
}