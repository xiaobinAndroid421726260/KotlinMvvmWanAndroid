package com.kotlin.mvvm.ext

import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import com.blankj.utilcode.util.ActivityUtils
import com.kotlin.mvvm.ui.WebViewActivity
import com.kotlin.mvvm.ui.setting.SettingActivity

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/10/8 16:30
 */

//获取主题属性id
fun TypedValue.resourceId(resId: Int, theme: Resources.Theme): Int {
    theme.resolveAttribute(resId, this, true)
    return this.resourceId
}

fun startWebViewActivity(id: Int, url: String, title: String){
    val bundle = Bundle().apply {
        putInt("id", id)
        putString("url", url)
        putString("title", title)
    }
    ActivityUtils.startActivity(bundle, WebViewActivity::class.java)
}