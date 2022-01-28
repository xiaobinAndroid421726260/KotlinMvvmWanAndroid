package com.kotlin.mvvm.ui.my

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kotlin.mvvm.R
import com.kotlin.mvvm.ui.my.bean.OptionBean

/**
 * description:
 *
 * @author Db_z
 * @Date 2022/1/24 10:07
 */
class MyOptionAdapter : BaseQuickAdapter<OptionBean, BaseViewHolder>(R.layout.item_my_recycler) {

    override fun convert(holder: BaseViewHolder, item: OptionBean) {
        holder.setImageResource(R.id.imageView, item.resId)
        holder.setText(R.id.textView, item.title)
        holder.setVisible(R.id.tv_count, item.count != 0)
        holder.setText(R.id.tv_count, item.count.toString())
    }
}