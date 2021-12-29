package com.kotlin.mvvm.ui.system

import android.view.LayoutInflater
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kotlin.mvvm.R
import com.kotlin.mvvm.databinding.FragmentSystemItemTwoFlowBinding
import com.kotlin.mvvm.ext.startWebViewActivity
import com.kotlin.mvvm.ui.system.bean.Article
import com.kotlin.mvvm.ui.system.bean.NaviBean
import com.kotlin.mvvm.view.flowlayout.FlowLayout
import com.kotlin.mvvm.view.flowlayout.TagAdapter
import com.kotlin.mvvm.view.flowlayout.TagFlowLayout

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/28 10:34
 */
class SystemTwoContentAdapter :
    BaseQuickAdapter<NaviBean, BaseViewHolder>(R.layout.fragment_system_item_two_content) {

    override fun convert(holder: BaseViewHolder, item: NaviBean) {
        holder.setText(R.id.tv_title, item.name)
        val flowLayout = holder.getView<TagFlowLayout<Article>>(R.id.flowlayout)
        val adapter = object : TagAdapter<Article>(item.articles) {
            override fun getView(parent: FlowLayout?, position: Int, t: Article): View {
                val binding = FragmentSystemItemTwoFlowBinding.inflate(LayoutInflater.from(context), parent, false)
                binding.tvContent.text = t.title
                return binding.root
            }
        }
        flowLayout.setAdapter(adapter)
        flowLayout.setOnTagClickListener { _, position, _ ->
            startWebViewActivity(
                adapter.data[position].id,
                adapter.data[position].link,
                adapter.data[position].title
            )
        }
    }
}