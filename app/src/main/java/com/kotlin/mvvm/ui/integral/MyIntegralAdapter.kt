package com.kotlin.mvvm.ui.integral

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kotlin.mvvm.R
import com.kotlin.mvvm.ui.integral.bean.IntegralBean

/**
 * description:
 *
 * @author Db_z
 * @Date 2022/01/22 15:21
 */
class MyIntegralAdapter : BaseQuickAdapter<IntegralBean, BaseViewHolder>(R.layout.item_my_integral),
    LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: IntegralBean) {
        holder.setText(R.id.tv_reason, item.reason)
        holder.setText(R.id.tv_time, item.desc)
        holder.setText(R.id.tv_point, "+${item.coinCount}")
    }
}