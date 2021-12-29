package com.kotlin.mvvm.ui.system

import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kotlin.mvvm.R
import com.kotlin.mvvm.ui.system.bean.NaviBean

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/28 9:56
 */
class SystemTwoTitleAdapter :
    BaseQuickAdapter<NaviBean, BaseViewHolder>(R.layout.fragment_system_item_two_title) {

    override fun convert(holder: BaseViewHolder, item: NaviBean) {
        holder.setText(R.id.tv_title, item.name)
        holder.setBackgroundColor(
            R.id.tv_title,
            if (item.check) ContextCompat.getColor(
                context,
                R.color.vertical_tab_layout_indicator_color
            ) else ContextCompat.getColor(context, R.color.vertical_tab_layout_bg)
        )
        holder.setTextColor(
            R.id.tv_title, if (item.check) ContextCompat.getColor(
                context, R.color.colorPrimary
            ) else ContextCompat.getColor(
                context, R.color.item_chapter
            )
        )
    }
}