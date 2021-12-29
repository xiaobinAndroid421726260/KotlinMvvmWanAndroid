package com.kotlin.mvvm.ui.project

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kotlin.mvvm.R
import com.kotlin.mvvm.ext.onClick
import com.kotlin.mvvm.ext.startWebViewActivity
import com.kotlin.mvvm.ui.project.bean.ProjectPagerBean

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/24 10:53
 */
class ProjectAdapter : BaseQuickAdapter<ProjectPagerBean, BaseViewHolder>(R.layout.item_project), LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: ProjectPagerBean) {
        val imageView = holder.getView<ImageView>(R.id.image)
        if (item.envelopePic.isNotEmpty()) {
            Glide.with(context)
                .load(item.envelopePic)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(40)))
                .into(imageView)
        }
        holder.setText(R.id.tv_title, item.title)
        holder.setText(R.id.tv_content, item.desc)
        holder.setText(R.id.tv_name, if (item.author.isNotEmpty()) item.author else item.shareUser)
        holder.setText(R.id.tv_time, item.niceDate)
        holder.setImageResource(
            R.id.iv_collection,
            if (item.collect) R.drawable.ic_like else R.drawable.ic_like_not
        )
        holder.itemView.onClick { startWebViewActivity(item.id, item.link, item.title) }
    }
}