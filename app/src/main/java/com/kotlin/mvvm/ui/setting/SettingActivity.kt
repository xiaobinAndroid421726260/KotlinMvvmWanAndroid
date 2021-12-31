package com.kotlin.mvvm.ui.setting

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.color.ColorChooserDialog
import com.blankj.utilcode.util.StringUtils
import com.kotlin.mvvm.R
import com.kotlin.mvvm.base.BaseActivity
import com.kotlin.mvvm.databinding.ActivitySettingBinding
import com.kotlin.mvvm.ext.*

class SettingActivity : BaseActivity(), ColorChooserDialog.ColorCallback {

    private val binding by lazy { ActivitySettingBinding.inflate(layoutInflater) }

    override fun getContentView() = binding.root

    override fun initView(bundle: Bundle?) {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = StringUtils.getString(R.string.action_setting)
        binding.toolbar.setNavigationIcon(
            if (getAppThemeColor() == Color.WHITE)
                R.drawable.ic_arrow_black else R.drawable.ic_arrow_white
        )
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
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
    }

    override fun initData() {
        setToolbarBackColor(this, binding.toolbar, null)
        binding.toolbar.setNavigationIcon(
            if (getAppThemeColor() == Color.WHITE)
                R.drawable.ic_arrow_black else R.drawable.ic_arrow_white
        )
        setThemeColor()
    }

    private fun setThemeColor() {
        val imageColor = if (mAppThemeColor == Color.WHITE) {
            ContextCompat.getColor(this, R.color.Grey200)
        } else {
            mAppThemeColor
        }
        binding.viewTheme.setImageDrawable(ColorDrawable(imageColor))
    }

    override fun onColorSelection(dialog: ColorChooserDialog, selectedColor: Int) {
        if (!dialog.isAccentMode) {
            setAppThemeColor(selectedColor)
            if (getNavBar()) {
                setNavBarColor(true)
            }
        }
        initThemeColor()
        initData()
    }

    override fun onColorChooserDismissed(dialog: ColorChooserDialog) {

    }
}