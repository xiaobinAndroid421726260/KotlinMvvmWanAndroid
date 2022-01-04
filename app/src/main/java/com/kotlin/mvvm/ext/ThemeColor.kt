package com.kotlin.mvvm.ext

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
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
    val color = if (getNightMode()) {
        ContextCompat.getColor(activity, R.color.colorPrimary)
    } else {
        getAppThemeColor()
    }
    if (getNightMode()) {
        setToolbarWhiteExceptColor(activity, toolbar, color)
    } else {
        if (activity.supportActionBar != null) {
            if (color == Color.WHITE) {
                setToolbarWhiteColor(activity, toolbar, color)
            } else {
                setToolbarWhiteExceptColor(activity, toolbar, color)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.supportActionBar!!.setBackgroundDrawable(ColorDrawable(color))
            }
        }
    }
    if (actionButton != null) {
        actionButton.setImageDrawable(
            if (color == Color.WHITE) activity.resources.getDrawable(
                R.drawable.ic_arrow_upward_black_24dp, activity.theme
            ) else activity.resources.getDrawable(
                R.drawable.ic_arrow_upward_white_24dp,
                activity.theme
            )
        )
        actionButton.backgroundTintList = ColorStateList.valueOf(color)
    }
}

/**
 * 设置TaLayout背景颜色及文本颜色
 */
fun setTaLayoutViewTextColor(tabLayout: TabLayout) {
    if (getNightMode()) {
        tabLayout.setBackgroundColor(
            ContextCompat.getColor(
                tabLayout.context,
                R.color.colorPrimary
            )
        )
        tabLayout.setTabTextColors(
            ContextCompat.getColor(tabLayout.context, R.color.arrow_color),
            ContextCompat.getColor(tabLayout.context, R.color.color_theme_text)
        )
        tabLayout.setSelectedTabIndicatorColor(
            ContextCompat.getColor(
                tabLayout.context,
                R.color.white
            )
        )
    } else {
        val color = getAppThemeColor()
        tabLayout.setBackgroundColor(color)
        if (color == Color.WHITE) {
            tabLayout.setTabTextColors(
                ContextCompat.getColor(tabLayout.context, R.color.Grey700),
                ContextCompat.getColor(tabLayout.context, R.color.dark_dark)
            )
            tabLayout.setSelectedTabIndicatorColor(
                ContextCompat.getColor(
                    tabLayout.context,
                    R.color.dark_dark
                )
            )
        } else {
            tabLayout.setTabTextColors(
                ContextCompat.getColor(tabLayout.context, R.color.Grey300),
                ContextCompat.getColor(tabLayout.context, R.color.white)
            )
            tabLayout.setSelectedTabIndicatorColor(
                ContextCompat.getColor(
                    tabLayout.context,
                    R.color.white
                )
            )
        }
    }
}

fun setBottomNavigationItemColor(
    activity: AppCompatActivity,
    bottomNavigationView: BottomNavigationView
) {
    val color = if (getNightMode()) {
        ContextCompat.getColor(activity, R.color.colorAccent)
    } else {
        if (getNavBar()) {
            getAppThemeColor()
        } else {
            ContextCompat.getColor(activity, R.color.colorAccent)
        }
    }
    val navColor = if (color == Color.WHITE) {
        ContextCompat.getColor(activity, R.color.colorAccent)
    } else {
        color
    }
    val states = arrayOf(
        intArrayOf(android.R.attr.state_checked),
        intArrayOf(-android.R.attr.state_checked),
    )
    val stateList = ColorStateList.valueOf(navColor)
    val defaultColor =
        ColorStateList.valueOf(ContextCompat.getColor(activity, R.color.nav_theme_color))
    val colors = intArrayOf(
        stateList.getColorForState(
            intArrayOf(android.R.attr.state_enabled), color
        ),
        defaultColor.getColorForState(
            intArrayOf(-android.R.attr.state_enabled),
            R.color.nav_theme_color
        )
    )
    val colorStateList = ColorStateList(states, colors)
    bottomNavigationView.itemTextColor = colorStateList
    bottomNavigationView.itemIconTintList = colorStateList
}

/**
 * 设置白色主题
 */
private fun setToolbarWhiteColor(activity: AppCompatActivity, toolbar: Toolbar, color: Int) {
    toolbar.setTitleTextColor(Color.BLACK)
    ImmersionBar.with(activity)
        .statusBarDarkFont(true)
        .fitsSystemWindows(true) //使用该属性,必须指定状态栏颜色
        .statusBarColorInt(color)
        .init()
}

/**
 * 设置除白色以外的主题
 */
@SuppressLint("ResourceType")
private fun setToolbarWhiteExceptColor(activity: AppCompatActivity, toolbar: Toolbar, color: Int) {
    toolbar.setTitleTextColor(Color.WHITE)
    ImmersionBar.with(activity)
        .statusBarDarkFont(false)
        .fitsSystemWindows(true) //使用该属性,必须指定状态栏颜色
        .statusBarColorInt(color)
        .init()
}