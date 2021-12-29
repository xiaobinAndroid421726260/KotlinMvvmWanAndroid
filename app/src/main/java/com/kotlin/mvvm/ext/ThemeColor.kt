package com.kotlin.mvvm.ext

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.gyf.immersionbar.ImmersionBar
import com.kotlin.mvvm.R

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/13 17:28
 */

/**
 * 设置主题 Toolbar 背景颜色
 */
@SuppressLint("UseCompatLoadingForDrawables")
fun setToolbarBackColor(
    activity: AppCompatActivity,
    toolbar: Toolbar,
    actionButton: FloatingActionButton?
) {
    val color = getAppThemeColor()
    if (getNightMode()) {
        setToolbarWhiteExceptColor(activity, toolbar, color)
    } else {
        if (activity.supportActionBar != null) {
            if (color == R.color.white) {
                setToolbarWhiteColor(activity, toolbar, color)
            } else {
                setToolbarWhiteExceptColor(activity, toolbar, color)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.supportActionBar!!
                    .setBackgroundDrawable(
                        ColorDrawable(
                            activity.resources.getColor(
                                color,
                                activity.theme
                            )
                        )
                    )
            }
        }
    }
    if (actionButton != null) {
        actionButton.setImageDrawable(
            if (color == R.color.white)
                activity.resources.getDrawable(
                    R.drawable.ic_arrow_upward_black_24dp,
                    activity.theme
                ) else activity.resources.getDrawable(
                R.drawable.ic_arrow_upward_white_24dp,
                activity.theme
            )
        )
        actionButton.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(activity, color))
    }
}

/**
 * 设置TaLayout背景颜色及文本颜色
 */
fun setTaLayoutViewTextColor(tabLayout: TabLayout) {
    tabLayout.setBackgroundColor(
        ContextCompat.getColor(
            tabLayout.context,
            R.color.colorPrimary
        )
    )
    if (getNightMode()) {
        tabLayout.setTabTextColors(
            ContextCompat.getColor(tabLayout.context, R.color.arrow_color),
            ContextCompat.getColor(tabLayout.context, R.color.color_theme_text)
        )
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(tabLayout.context, R.color.color_theme_text))
    } else {
        val color = getAppThemeColor()
        if (color == R.color.white) {
            tabLayout.setTabTextColors(
                ContextCompat.getColor(tabLayout.context, R.color.Grey700),
                ContextCompat.getColor(tabLayout.context, R.color.dark_dark)
            )
            tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(tabLayout.context, R.color.dark_dark))
        } else {
            tabLayout.setTabTextColors(
                ContextCompat.getColor(tabLayout.context, R.color.Grey300),
                ContextCompat.getColor(tabLayout.context, R.color.white)
            )
            tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(tabLayout.context, R.color.white))
        }
    }
}

/**
 * 设置白色主题
 */
private fun setToolbarWhiteColor(activity: AppCompatActivity, toolbar: Toolbar, color: Int) {
    toolbar.setTitleTextColor(Color.BLACK)
    ImmersionBar.with(activity)
        .statusBarDarkFont(true)
        .fitsSystemWindows(true) //使用该属性,必须指定状态栏颜色
        .statusBarColor(color)
        .init()
}

/**
 * 设置除白色以外的主题
 */
private fun setToolbarWhiteExceptColor(activity: AppCompatActivity, toolbar: Toolbar, color: Int) {
    toolbar.setTitleTextColor(Color.WHITE)
    ImmersionBar.with(activity)
        .statusBarDarkFont(false)
        .fitsSystemWindows(true) //使用该属性,必须指定状态栏颜色
        .statusBarColor(color)
        .init()
}