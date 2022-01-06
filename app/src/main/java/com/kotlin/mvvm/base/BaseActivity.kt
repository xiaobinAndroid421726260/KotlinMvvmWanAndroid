package com.kotlin.mvvm.base

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.blankj.utilcode.util.ToastUtils
import com.gyf.immersionbar.ImmersionBar
import com.kotlin.mvvm.R
import com.kotlin.mvvm.common.UiState
import com.kotlin.mvvm.popup.LoadingView
import com.jakewharton.rxbinding4.view.clicks
import com.kotlin.mvvm.common.UiState.*
import com.kotlin.mvvm.ext.*
import com.permissionx.guolindev.PermissionX
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.jessyan.autosize.AutoSizeCompat
import me.jessyan.autosize.internal.CustomAdapt
import java.util.concurrent.TimeUnit

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/10/7 15:38
 */
abstract class BaseActivity : AppCompatActivity(), CustomAdapt {

    protected val mHandler = Handler(Looper.getMainLooper())
    protected var mAppThemeColor = getAppThemeColor()
    private var isFirstLoad = true
    private var mDisposable: CompositeDisposable? = null
    protected var savedInstanceState: Bundle? = null

    protected abstract fun getContentView(): View

    protected abstract fun initView(bundle: Bundle?)

    protected abstract fun initData()

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.savedInstanceState = savedInstanceState
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setWindowConfigure()
        setContentView(getContentView())
        initImmersionBar()
        initView(intent.extras)
        ToastUtils.getDefaultMaker().setGravity(Gravity.CENTER, 0, 0)
    }

    /**
     * 监听加载的UI用户界面状态
     */
    fun refreshUiState(uiState: MutableStateFlow<UiState>) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                uiState.collect {
                    e("-------lifecycleScope.UiState = $it")
                    when (it) {
                        Loading -> showLoadView()
                        LoadEnd -> dismissLoadView()
                        LoadError -> dismissLoadView()
                        LoadComplete -> dismissLoadView()
                        LoadDefault -> {}
                    }
                }
            }
        }
    }

    /**
     * 请求权限
     */
    fun requestPermissions(vararg permissions: String, block: () -> Unit) {
        requestPermissions(listOf(*permissions), block)
    }

    private fun requestPermissions(permissions: List<String>, block: () -> Unit) {
        PermissionX.init(this)
            .permissions(permissions)
//            .onExplainRequestReason { scope, deniedList ->
//                val message = "PermissionX需要您同意以下权限才能正常使用"
//                scope.showRequestReasonDialog(deniedList, message, "Allow", "Deny")
//            }
            .request { allGranted, _, deniedList ->
                if (allGranted) {
                    block()
                } else {
                    ToastUtils.showShort("您拒绝了如下权限：$deniedList")
                }
            }
    }

    open fun initImmersionBar(
        @ColorInt statusBarColor: Int = ContextCompat.getColor(
            this,
            R.color.colorPrimary
        ), isDarkFont: Boolean = false
    ) {
        ImmersionBar.with(this)
            .statusBarDarkFont(isDarkFont)
            .fitsSystemWindows(true) //使用该属性,必须指定状态栏颜色
            .statusBarColorInt(statusBarColor)
            .init()
    }

    open fun initThemeColor() {
        mAppThemeColor = if (getNightMode()) {
            ContextCompat.getColor(this, R.color.colorPrimary)
        } else {
            getAppThemeColor()
        }
        supportActionBar?.setBackgroundDrawable(ColorDrawable(mAppThemeColor))
        initImmersionBar(mAppThemeColor, mAppThemeColor == Color.WHITE)
    }

    /**
     * 为布局内所有控件设置添加防抖处理
     */
    protected fun addDebouncingViews(vararg views: View) {
        if (mDisposable == null) {
            mDisposable = CompositeDisposable()
        }
        for (view in views) {
            val disposable = view.clicks()
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe({ onClickView(view) }) { e("-----onClickView = $it") }
            mDisposable?.add(disposable)
        }
    }

    /**
     * 取消所有订阅，防止内存泄漏
     */
    private fun clearDisposable() {
        if (mDisposable != null) {
            mDisposable?.clear()
            mDisposable = null
        }
    }

    override fun onResume() {
        super.onResume()
        alwaysNeedReload()
        if (isFirstLoad) {
            initData()
            isFirstLoad = false
        } else {
            if (isNeedReload()) {
                initData()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearDisposable()
    }

//    private fun setRootViewLayout() {
//        try {
//            val type = javaClass.genericSuperclass as ParameterizedType
//            val clz = type.actualTypeArguments[0] as Class<*>
//            val method = clz.getMethod("inflate", LayoutInflater::class.java)
//            binding = method.invoke(null, layoutInflater) as VB
//            setContentView(binding?.root)
//        } catch (e: Exception) {
//            e("setRootViewLayout exception-------$e")
//            finish()
//        }
//    }

    /**
     * 总是需要重新加载
     */
    protected open fun alwaysNeedReload() {}

    /**
     * 提供子类方法 在设置布局之前
     */
    protected open fun setWindowConfigure() {}

    /**
     * 失败重试,重新加载事件
     */
    protected open fun onReloadClick() {}

    protected open fun onClickView(view: View?) {}

    /**
     * 再次可见时，是否重新请求数据，默认为true
     */
    protected open fun isNeedReload() = true

    fun showLoadView() {
        LoadingView.getInstance(this).showPopupWindow()
    }

    fun dismissLoadView() {
        LoadingView.getInstance(this).delayDismiss()
    }

    fun dismissLoadView(delay: Long) {
        LoadingView.getInstance(this).delayDismiss(delay)
    }

    fun dismissDelayWith(delay: Long, runnable: Runnable?) {
        LoadingView.getInstance(this).delayDismissWith(delay, runnable)
    }

    override fun getResources(): Resources {
        AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources())
        return super.getResources()
    }

    override fun isBaseOnWidth() = false

    override fun getSizeInDp(): Float = 667F
}