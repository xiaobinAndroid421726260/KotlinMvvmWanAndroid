package com.kotlin.mvvm.ui

import android.graphics.Color
import android.os.Bundle
import com.kotlin.mvvm.R
import com.kotlin.mvvm.base.BaseActivity
import com.kotlin.mvvm.databinding.ActivityWebViewBinding
import com.kotlin.mvvm.ext.*

class WebViewActivity : BaseActivity() {

    private val binding by lazy { ActivityWebViewBinding.inflate(layoutInflater) }
    private var id: Int? = 0
    private var url: String? = null
    private var title: String? = null

    override fun getContentView() = binding.root

    override fun initView(bundle: Bundle?) {
        bundle?.apply {
            id = getInt("id")
            url = getString("url")
            title = getString("title")
        }
        setSupportActionBar(binding.toolbar)
        binding.tvTitle.text = "正在加载中..."
        binding.tvTitle.visible()
        binding.toolbar.setNavigationIcon(
            if (getAppThemeColor() == Color.WHITE)
                R.drawable.ic_arrow_black else R.drawable.ic_arrow_white
        )
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
        title?.let { binding.tvTitle.text = it }
        url?.let { binding.x5WebView.loadUrl(it) }
    }

    override fun initData() {
        if (getNightMode()) {
            binding.tvTitle.setTextColor(Color.WHITE)
        } else {
            val color = getAppThemeColor()
            binding.tvTitle.setTextColor(if (color == Color.WHITE) Color.BLACK else Color.WHITE)
        }
        setToolbarBackColor(this, binding.toolbar, null)
    }
}