package com.kotlin.mvvm.ext

import androidx.appcompat.app.AppCompatDelegate
import com.kotlin.mvvm.R
import com.tencent.mmkv.MMKV

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/10/8 16:43
 */
private const val THEME = "theme"
private const val KEY_NIGHT_MODE = "key_night_mode"
private const val SAVE_NIGHT_MODE = "save_night_mode"

fun setAppThemeColor(theme: Int) {
    MMKV.defaultMMKV().putInt(THEME, theme)
}

fun getAppThemeColor() = MMKV.defaultMMKV().getInt(THEME, R.color.colorPrimary)

fun setNightMode(nightMode: Boolean) {
    MMKV.defaultMMKV().putBoolean(KEY_NIGHT_MODE, nightMode)
}

fun getNightMode() = MMKV.defaultMMKV().getBoolean(KEY_NIGHT_MODE, false)

fun saveLastNightMode(theme: Int) {
    MMKV.defaultMMKV().putInt(SAVE_NIGHT_MODE, theme)
}

fun getLastNightMode() = MMKV.defaultMMKV().getInt(SAVE_NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_NO)
