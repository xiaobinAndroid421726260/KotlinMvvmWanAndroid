package com.kotlin.mvvm.ext

import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.kotlin.mvvm.ui.WebViewActivity
import com.kotlin.mvvm.ui.login.LoginActivity

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/10/8 16:30
 */

fun startLoginActivity(){
    ActivityUtils.startActivity(LoginActivity::class.java)
}

fun startWebViewActivity(id: Int, url: String, title: String){
    val bundle = Bundle().apply {
        putInt("id", id)
        putString("url", url)
        putString("title", title)
    }
    ActivityUtils.startActivity(bundle, WebViewActivity::class.java)
}