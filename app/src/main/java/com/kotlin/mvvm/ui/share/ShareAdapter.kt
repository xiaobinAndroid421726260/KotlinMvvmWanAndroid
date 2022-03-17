package com.kotlin.mvvm.ui.share

import androidx.appcompat.widget.AppCompatImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kotlin.mvvm.R
import com.kotlin.mvvm.ext.checkLogin
import com.kotlin.mvvm.ext.onClick
import com.kotlin.mvvm.ext.startWebViewActivity
import com.kotlin.mvvm.ui.share.bean.Share

/**
 * description:
 *
 * @author Db_z
 * @Date 2022/1/24 16:53
 */
class ShareAdapter : BaseQuickAdapter<Share, BaseViewHolder>(R.layout.item_share), LoadMoreModule {

    private lateinit var listener: (collect: Boolean, id: Int, position: Int) -> Unit

    override fun convert(holder: BaseViewHolder, item: Share) {
        holder.setText(
            R.id.tv_name,
            item.author.ifEmpty { item.shareUser }
        )
        holder.setText(R.id.tv_time, item.niceDate)
        holder.setText(R.id.tv_title, item.title)
        holder.setText(R.id.tv_type, "${item.superChapterName}/${item.chapterName}")
        holder.setImageResource(
            R.id.iv_collection,
            if (item.collect) R.drawable.ic_like else R.drawable.ic_like_not
        )
        holder.itemView.onClick { startWebViewActivity(item.id, item.link, item.title) }
        holder.getView<AppCompatImageView>(R.id.iv_collection).onClick {
            checkLogin {
                listener.invoke(item.collect, item.id, getItemPosition(item))
            }
        }
    }

    fun setCollectionListener(listener: (collect: Boolean, id: Int, position: Int) -> Unit) {
        this.listener = listener
    }
}