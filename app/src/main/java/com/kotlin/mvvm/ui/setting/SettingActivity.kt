package com.kotlin.mvvm.ui.setting

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.color.ColorChooserDialog
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.kotlin.mvvm.R
import com.kotlin.mvvm.base.BaseActivity
import com.kotlin.mvvm.databinding.ActivitySettingBinding
import com.kotlin.mvvm.ext.*

class SettingActivity : BaseActivity(), ColorChooserDialog.ColorCallback {

    private val binding by lazy { ActivitySettingBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<SettingViewModel>()

    override fun getContentView() = binding.root

    override fun initView(bundle: Bundle?) {
        setSupportActionBar(binding.toolbar)
        setThemeColor()
        supportActionBar?.title = StringUtils.getString(R.string.action_setting)
        binding.checkbox.isChecked = getNavBar()
        binding.tvVersionText.text = getString(R.string.current_version, AppUtils.getAppVersionName())
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
        binding.tvLogout.onClick { mViewModel.logout() }
        mViewModel.handlerCode.observe(this) {
            ToastUtils.showShort(StringUtils.getString(R.string.logout))
            saveUser("")
            CookieClass.clearCookie()
            binding.tvLogout.invisible()
            finish()
        }
    }

    @SuppressLint("RestrictedApi")
    override fun initData() {
        setToolbarBackColor(this, binding.toolbar, null)
        binding.tvDarkMode.text = if (getNightMode()) {
            StringUtils.getString(R.string.standard_mode)
        } else {
            StringUtils.getString(R.string.dark_mode)
        }
        binding.checkbox.supportButtonTintList = ColorStateList.valueOf(getThemeTextColor())
        binding.tvVersion.setTextColor(getThemeTextColor())
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
        if (isLogin()) {
            binding.tvLogout.setBackgroundColor(getThemeTextColor())
            binding.tvLogout.visible()
        } else {
            binding.tvLogout.invisible()
        }
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