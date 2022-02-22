package com.kotlin.mvvm.ui.wechat

import androidx.appcompat.widget.AppCompatImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kotlin.mvvm.R
import com.kotlin.mvvm.ext.onClick
import com.kotlin.mvvm.ext.startWebViewActivity
import com.kotlin.mvvm.ui.wechat.bean.WechatPagerBean

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/23 11:18
 */
class WechatAdapter :
    BaseQuickAdapter<WechatPagerBean, BaseViewHolder>(R.layout.item_fragment_pager),
    LoadMoreModule {

    private lateinit var listener: (collect: Boolean, id: Int, position: Int) -> Unit

    override fun convert(holder: BaseViewHolder, item: WechatPagerBean) {
        holder.setText(
            R.id.tv_name,
            if (item.author.isNotEmpty()) item.author else item.shareUser
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
            listener.invoke(item.collect, item.id, getItemPosition(item))
        }
    }

    fun setCollectionListener(listener: (collect: Boolean, id: Int, position: Int) -> Unit) {
        this.listener = listener
    }
}