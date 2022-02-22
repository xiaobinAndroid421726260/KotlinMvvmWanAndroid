package com.kotlin.mvvm.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.*
import android.net.Uri
import android.net.http.SslError
import android.util.AttributeSet
import android.webkit.*
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.SizeUtils.dp2px
import com.kotlin.mvvm.R
import com.kotlin.mvvm.ext.gone
import com.kotlin.mvvm.ext.visible
import me.jessyan.autosize.AutoSize

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/22 11:07
 */
class X5WebView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) :
    WebView(context, attrs, defStyleAttr) {

    private var mProgressBar: ProgressBar? = null

    init {
        initWebViewSettings(context)
        webViewClient = object : WebViewClient() {
            /**
             * 防止加载网页时调起系统浏览器
             */
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
//            return true;
//        }
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return try {
                    if (url.startsWith("http:") || url.startsWith("https:")) {
                        view.loadUrl(url)
                    } else {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        getContext().startActivity(intent)
                    }
                    true
                } catch (e: Exception) {
                    false
                }
            }

            override fun onReceivedSslError(
                webView: WebView,
                sslErrorHandler: SslErrorHandler,
                sslError: SslError
            ) {
                super.onReceivedSslError(webView, sslErrorHandler, sslError)
                sslErrorHandler.proceed()
            }
        }
        webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(webView: WebView, progress: Int) {
                super.onProgressChanged(webView, progress)
                if (progress == 100) {
                    mProgressBar?.gone()
                } else {
                    if (mProgressBar?.visibility == GONE) {
                        mProgressBar?.visible()
                    }
                    mProgressBar?.progress = progress
                }
            }
        }
    }

    /**
     * 修改加载的进度颜色
     */
    @SuppressLint("NewApi")
    fun setGradientDrawableColor(color: Int) {
        val colors = intArrayOf(color, color)
        val drawable = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors)
        val layerDrawable = mProgressBar?.progressDrawable as LayerDrawable
        val clipDrawable = layerDrawable.getDrawable(layerDrawable.numberOfLayers - 1) as ClipDrawable
        clipDrawable.drawable = drawable
        mProgressBar?.apply {
            progressDrawable = layerDrawable
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebViewSettings(context: Context) {
        mProgressBar = ProgressBar(
            context, null,
            android.R.attr.progressBarStyleHorizontal
        )
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, dp2px(3f)
        )
        mProgressBar?.apply {
            setLayoutParams(layoutParams)
            progressDrawable = ContextCompat.getDrawable(context, R.drawable.progress_back)
            progress = 0
        }
        addView(mProgressBar)
        val appCachePath = context.applicationContext.getDir("cache", Context.MODE_PRIVATE).path
        settings.apply {
            //如果访问的页面中要与Javascript交互，则WebView必须设置支持Javascript。默认为false
            javaScriptEnabled = true
            //允许JS自动打开新窗口，默认为false
            javaScriptCanOpenWindowsAutomatically = true
            //支持屏幕缩放。默认为true，若设置setBuiltInZoomControls(true)，则必须开启此功能
            setSupportZoom(true)
            builtInZoomControls = true       //设置内置的缩放控件。默认为false，表示WebView不可缩放
            allowFileAccess = true
            displayZoomControls = false      //隐藏原生的缩放控件，默认为true
            setSupportMultipleWindows(false) //禁止在新窗口中打开目标网页，默认为false
            layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
            //设置自适应屏幕，两者合用
            useWideViewPort = true           //将图片调整到合适的大小
            setGeolocationEnabled(true)
            loadWithOverviewMode = true      //缩放至屏幕的大小，默认为false
            setAppCacheEnabled(true)         //开启APP缓存功能，默认为false
            setAppCacheMaxSize(Long.MAX_VALUE)
            pluginState = WebSettings.PluginState.ON_DEMAND
            setAppCachePath(appCachePath)    //开启APP缓存功能必须设置缓存路径
            cacheMode = if (NetworkUtils.isConnected()) {
                //根据cache-control决定是否从网络上取数据。
                WebSettings.LOAD_DEFAULT
            } else {
                //无论是否有网络，无论是否过期，或者no-cache，只要本地有缓存，都使用缓存
                WebSettings.LOAD_CACHE_ELSE_NETWORK
            }
            databaseEnabled = true           //开启数据库形式存储，默认为false
            domStorageEnabled = true         //开启DOM形式存储，默认为false
            //解决在Android 5.0上 WebView 默认不允许加载http和https混合内容
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            allowUniversalAccessFromFileURLs = true
        }
    }

    override fun setOverScrollMode(overScrollMode: Int) {
        super.setOverScrollMode(overScrollMode)
        //重写该方法实现density适配
        if (context != null && context is Activity) {
            AutoSize.autoConvertDensityOfGlobal(context as Activity)
        }
    }
}