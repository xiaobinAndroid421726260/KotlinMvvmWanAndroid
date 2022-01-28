package com.kotlin.mvvm.ui.system

import com.blankj.utilcode.util.GsonUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kotlin.mvvm.R
import com.kotlin.mvvm.ext.onClick
import com.kotlin.mvvm.ui.system.bean.SystemBean
import com.kotlin.mvvm.ui.system.know.KnowledgeActivity

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/27 16:37
 */
class SystemOneAdapter : BaseQuickAdapter<SystemBean, BaseViewHolder>(R.layout.item_one),
    LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: SystemBean) {
        holder.setText(R.id.tv_title, item.name)
        if (item.children.isNotEmpty()) {
            val stringBuilder = StringBuilder()
            for (data in item.children) {
                stringBuilder.append(data.name).append("\t\t")
            }
            holder.setText(R.id.tv_title_second, stringBuilder)
        }
        holder.itemView.onClick { KnowledgeActivity.newInstance(item.name, GsonUtils.toJson(item.children)) }
    }
}