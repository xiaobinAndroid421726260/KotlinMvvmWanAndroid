package com.kotlin.mvvm.ui.search

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kotlin.mvvm.R
import com.kotlin.mvvm.ext.onClick
import com.kotlin.mvvm.ext.startWebViewActivity
import com.kotlin.mvvm.ui.search.bean.SearchBean

/**
 * description:
 *
 * @author Db_z
 * @Date 2022/1/26 14:43
 */
class SearchAdapter : BaseQuickAdapter<SearchBean, BaseViewHolder>(R.layout.item_search), LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: SearchBean) {
        holder.setText(
            R.id.tv_name,
            if (item.author.isNotEmpty()) item.author else item.shareUser
        )
        holder.setText(R.id.tv_time, item.niceDate)
        holder.setText(R.id.tv_title, item.title)
        holder.setText(R.id.tv_type, "${item.superChapterName}/${item.chapterName}")
        holder.itemView.onClick { startWebViewActivity(item.id, item.link, item.title) }
    }
}