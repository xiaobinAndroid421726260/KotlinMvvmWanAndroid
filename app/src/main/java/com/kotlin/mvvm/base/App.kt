package com.kotlin.mvvm.base

import android.app.Application
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.LogUtils
import com.kingja.loadsir.core.LoadSir
import com.kotlin.mvvm.R
import com.kotlin.mvvm.common.loadsir.EmptyCallback
import com.kotlin.mvvm.common.loadsir.ErrorCallback
import com.kotlin.mvvm.common.loadsir.LoadingCallback
import com.kotlin.mvvm.ext.getAppThemeColor
import com.kotlin.mvvm.ext.getNightMode
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.tencent.mmkv.MMKV
import kotlin.properties.Delegates

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/14 15:34
 */
class App : Application() {

    init {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, refreshLayout ->
            if (getNightMode()) {
                refreshLayout.setPrimaryColors(ContextCompat.getColor(this, R.color.colorAccent), ContextCompat.getColor(this, R.color.white))
            } else {
                val color = getAppThemeColor()
                if (color == Color.WHITE) {
                    refreshLayout.setPrimaryColors(color, ContextCompat.getColor(this, R.color.black))
                } else {
                    refreshLayout.setPrimaryColors(color, ContextCompat.getColor(this, R.color.white))
                }
            }
            ClassicsHeader(context)
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ -> ClassicsFooter(context) }
    }

    companion object {
        var CONTEXT: Context by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXT = applicationContext
        val rootDir: String = MMKV.initialize(this)
        LogUtils.e("---MMKV rootDir = $rootDir")
        // 获取当前的主题
        AppCompatDelegate.setDefaultNightMode(if (getNightMode()) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
        LoadSir.beginBuilder()
            .addCallback(LoadingCallback())
            .addCallback(EmptyCallback())
            .addCallback(ErrorCallback())
            .setDefaultCallback(LoadingCallback::class.java)
            .commit()
    }
}