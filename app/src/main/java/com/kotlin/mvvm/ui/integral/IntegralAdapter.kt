package com.kotlin.mvvm.ui.integral

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kotlin.mvvm.R
import com.kotlin.mvvm.ui.integral.bean.RankBean

/**
 * description:
 *
 * @author Db_z
 * @Date 2022/01/22 15:21
 */
class IntegralAdapter : BaseQuickAdapter<RankBean, BaseViewHolder>(R.layout.item_integral_rank),
    LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: RankBean) {
        holder.setText(R.id.tv_no, "${getItemPosition(item) + 1}")
        holder.setText(R.id.tv_name, item.username)
        holder.setText(R.id.tv_integral, item.coinCount.toString())
    }
}