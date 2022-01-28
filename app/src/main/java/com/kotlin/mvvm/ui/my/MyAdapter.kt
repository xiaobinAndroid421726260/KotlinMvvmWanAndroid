package com.kotlin.mvvm.ui.my

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kotlin.mvvm.R
import com.kotlin.mvvm.ext.onClick
import com.kotlin.mvvm.ext.startWebViewActivity
import com.kotlin.mvvm.ui.my.bean.WendBean

/**
 * description:
 *
 * @author Db_z
 * @Date 2022/1/21 11:30
 */
class MyAdapter : BaseQuickAdapter<WendBean, BaseViewHolder>(R.layout.item_my), LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: WendBean) {
        holder.setText(R.id.tv_time, item.niceDate)
        holder.setText(R.id.tv_title, item.title)
        holder.setText(R.id.tv_type, "${item.superChapterName}/${item.author}")
        holder.setImageResource(
            R.id.iv_collection,
            if (item.collect) R.drawable.ic_like else R.drawable.ic_like_not
        )
        holder.itemView.onClick { startWebViewActivity(item.id, item.link, item.title) }
    }
}