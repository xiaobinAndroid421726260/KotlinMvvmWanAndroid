package com.kotlin.mvvm.ui.message

import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.SpanUtils
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
 * @Date 2022/1/25 10:30
 */
class MessageAdapter : BaseQuickAdapter<MsgBean, BaseViewHolder>(R.layout.item_msg), LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: MsgBean) {
        holder.setText(R.id.tv_article, item.tag)
        holder.setVisible(R.id.tv_article, item.tag.isNotEmpty())
        holder.setText(R.id.tv_time, item.niceDate)
        val textView = holder.getView<AppCompatTextView>(R.id.tv_name)
        SpanUtils.with(textView)
            .append("@${item.fromUser}").setForegroundColor(ContextCompat.getColor(context, R.color.accent_pale_blue))
            .append("\t\t")
            .append(item.title).setForegroundColor(ContextCompat.getColor(context, R.color.item_title))
            .create()
        holder.setText(R.id.tv_title, item.message)
        holder.itemView.onClick { startWebViewActivity(item.id, item.fullLink, item.message) }
    }
}