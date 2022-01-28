package com.kotlin.mvvm.ui.search

import androidx.appcompat.widget.AppCompatImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kotlin.mvvm.R
import com.kotlin.mvvm.ext.onClick

/**
 * description:
 *
 * @author Db_z
 * @Date 2022/1/27 14:03
 */
class HistoryAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_search_history) {

    private lateinit var mDeteleListener: (k: String) -> Unit
    private lateinit var mSearchListener: (k: String) -> Unit

    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.tv_name, item)
        holder.getView<AppCompatImageView>(R.id.iv_delete).onClick { mDeteleListener.invoke(item) }
        holder.itemView.onClick { mSearchListener.invoke(item) }
    }

    fun setOnDeleteListener(listener: (k: String) -> Unit) {
        mDeteleListener = listener
    }

    fun setOnSearchListener(listener: (k: String) -> Unit) {
        mSearchListener = listener
    }
}