package com.kotlin.mvvm.ui.home

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.youth.banner.adapter.BannerAdapter

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/16 16:31
 */
class HomeBannerAdapter(data: List<String>) :
    BannerAdapter<String, HomeBannerAdapter.BannerViewHolder>(data) {

    class BannerViewHolder(view: ImageView) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView = view
    }

    override fun onCreateHolder(p0: ViewGroup, p1: Int): BannerViewHolder {
        val imageView = ImageView(p0.context)
        // 注意，必须设置为match_parent，这个是viewpager2强制要求的
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        return BannerViewHolder(imageView)
    }

    override fun onBindView(p0: BannerViewHolder, p1: String, p2: Int, p3: Int) {
        Glide.with(p0.imageView.context).load(p1).into(p0.imageView)
    }
}