package com.kotlin.mvvm.ui

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import com.just.agentweb.AgentWeb
import com.just.agentweb.AgentWebSettingsImpl
import com.just.agentweb.DefaultWebClient
import com.just.agentweb.WebChromeClient
import com.kotlin.mvvm.base.BaseActivity
import com.kotlin.mvvm.databinding.ActivityWebViewBinding
import com.kotlin.mvvm.ext.getAppThemeColor
import com.kotlin.mvvm.ext.getNightMode
import com.kotlin.mvvm.ext.getThemeTextColor
import com.kotlin.mvvm.ext.setToolbarBackColor

class WebViewActivity : BaseActivity() {

    private val binding by lazy { ActivityWebViewBinding.inflate(layoutInflater) }
    private var id: Int? = 0
    private var url: String? = null
    private var title: String? = null
    private var mAgentWeb: AgentWeb? = null

    override fun getContentView() = binding.root

    override fun initView(bundle: Bundle?) {
        bundle?.apply {
            id = getInt("id")
            url = getString("url")
            title = getString("title")
        }
        setSupportActionBar(binding.toolbar)
        binding.tvTitle.text = "正在加载中..."
        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(
                binding.llRootView,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            .useDefaultIndicator(getThemeTextColor(), 3)
            .interceptUnkownUrl()
            .setAgentWebWebSettings(AgentWebSettingsImpl.getInstance())
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
            /**
             * 直接打开跳转页 DERECT
             * 咨询用户是否打开 ASK
             * 禁止打开其他页面 DISALLOW
             */
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DISALLOW)
            .setWebChromeClient(object : WebChromeClient() {
                override fun onReceivedTitle(view: WebView?, title: String?) {
                    binding.tvTitle.text = title
                    super.onReceivedTitle(view, title)
                }
            })
            .createAgentWeb()
            .ready()
            .get()
        mAgentWeb?.webCreator?.webView?.run {
            overScrollMode = WebView.OVER_SCROLL_NEVER
            settings.run {
                javaScriptCanOpenWindowsAutomatically = false
                loadsImagesAutomatically = true
                useWideViewPort = true
                loadWithOverviewMode = true
            }
        }
        mAgentWeb?.urlLoader?.loadUrl(url)
    }

    override fun initData() {
        if (getNightMode()) {
            binding.tvTitle.setTextColor(Color.WHITE)
        } else {
            binding.tvTitle.setTextColor(if (getAppThemeColor() == Color.WHITE) Color.BLACK else Color.WHITE)
        }
        setToolbarBackColor(this, binding.toolbar, null)
    }

    override fun onPause() {
        mAgentWeb?.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onResume() {
        mAgentWeb?.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        mAgentWeb?.webLifeCycle?.onDestroy()
        super.onDestroy()
    }
}