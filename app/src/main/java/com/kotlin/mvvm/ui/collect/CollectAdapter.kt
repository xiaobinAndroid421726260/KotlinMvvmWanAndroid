package com.kotlin.mvvm.ui.collect

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kotlin.mvvm.R
import com.kotlin.mvvm.ext.onClick
import com.kotlin.mvvm.ext.startWebViewActivity

/**
 * description:
 *
 * @author Db_z
 * @Date 2022/1/24 14:15
 */
class CollectAdapter : BaseQuickAdapter<CollectBean, BaseViewHolder>(R.layout.item_collect), LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: CollectBean) {
        holder.setText(R.id.tv_name, item.author)
        holder.setText(R.id.tv_time, item.niceDate)
        holder.setText(R.id.tv_title, item.title)
        holder.setText(R.id.tv_type, item.chapterName)
        holder.setImageResource(
            R.id.iv_collection,
            if (item.collect) R.drawable.ic_like else R.drawable.ic_like_not
        )
        holder.itemView.onClick { startWebViewActivity(item.id, item.link, item.title) }
    }
}