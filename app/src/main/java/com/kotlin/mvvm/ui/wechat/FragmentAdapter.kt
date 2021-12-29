package com.kotlin.mvvm.ui.wechat

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.kotlin.mvvm.ui.wechat.bean.WechatBean

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/23 11:52
 */
class FragmentAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(
        fragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {

    private val mDataBean = arrayListOf<WechatBean>()
    private val mFragments = arrayListOf<Fragment>()

    fun addData(dataBean: MutableList<WechatBean>, isRefresh: Boolean = true) {
        if (isRefresh) {
            clear()
        }
        mDataBean.addAll(dataBean)
        for (data in mDataBean) {
            mFragments.add(WechatViewPagerFragment.newInstance(data.id))
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

    override fun getPageTitle(position: Int) = mDataBean[position].name
}