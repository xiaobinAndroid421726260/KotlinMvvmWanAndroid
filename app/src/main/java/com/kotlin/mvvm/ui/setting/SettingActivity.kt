package com.kotlin.mvvm.ui.setting

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.color.ColorChooserDialog
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.StringUtils
import com.kotlin.mvvm.R
import com.kotlin.mvvm.base.BaseActivity
import com.kotlin.mvvm.databinding.ActivitySettingBinding
import com.kotlin.mvvm.ext.*

class SettingActivity : BaseActivity(), ColorChooserDialog.ColorCallback {

    private val binding by lazy { ActivitySettingBinding.inflate(layoutInflater) }

    override fun getContentView() = binding.root

    @SuppressLint("SetTextI18n")
    override fun initView(bundle: Bundle?) {
        setSupportActionBar(binding.toolbar)
        setThemeColor()
        binding.toolbar.title = StringUtils.getString(R.string.action_setting)
        binding.toolbar.setNavigationIcon(
            if (getAppThemeColor() == Color.WHITE)
                R.drawable.ic_arrow_black else R.drawable.ic_arrow_white
        )
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
        binding.checkbox.isChecked = getNavBar()
        binding.tvVersion.setTextColor(mAppThemeColor)
        binding.tvVersionText.text = StringUtils.getString(R.string.current_version) + "  " + AppUtils.getAppVersionName()
        binding.llTheme.onClick {
            ColorChooserDialog.Builder(this, R.string.choose_theme_color)
                .backButton(R.string.back)
                .cancelButton(R.string.cancel)
                .doneButton(R.string.done)
                .customButton(R.string.custom)
                .presetsButton(R.string.back)
                .allowUserColorInputAlpha(false)
                .show(this)
        }
        binding.llDarkMode.onClick {
            if (getNightMode()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                setNightMode(false)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                setNightMode(true)
            }
            initThemeColor()
            initData()
        }
        binding.checkbox.onClick {
            setNavBar(binding.checkbox.isChecked)
            setNavBarColor(true)
        }
    }

    override fun initData() {
        setToolbarBackColor(this, binding.toolbar, null)
        binding.toolbar.setNavigationIcon(
            if (getAppThemeColor() == Color.WHITE)
                R.drawable.ic_arrow_black else R.drawable.ic_arrow_white
        )
        binding.tvDarkMode.text = if (getNightMode()) {
            "深夜模式"
        } else {
            "标准模式"
        }
    }

    private fun setThemeColor(appThemeColor: Int = mAppThemeColor) {
        val imageColor = if (getNightMode()) {
            appThemeColor
        } else {
            if (mAppThemeColor == Color.WHITE) {
                ContextCompat.getColor(this, R.color.Grey200)
            } else {
                mAppThemeColor
            }
        }
        binding.viewTheme.setImageDrawable(ColorDrawable(imageColor))
    }

    override fun onColorSelection(dialog: ColorChooserDialog, selectedColor: Int) {
        if (!dialog.isAccentMode) {
            setAppThemeColor(selectedColor)
            setNavBarColor(true)
        }
        initThemeColor()
        setThemeColor(selectedColor)
        initData()
    }

    override fun onColorChooserDismissed(dialog: ColorChooserDialog) {

    }
}