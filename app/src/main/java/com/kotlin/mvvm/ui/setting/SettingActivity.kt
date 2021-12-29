package com.kotlin.mvvm.ui.setting

import android.os.Bundle
import com.blankj.utilcode.util.StringUtils
import com.kotlin.mvvm.R
import com.kotlin.mvvm.base.BaseActivity
import com.kotlin.mvvm.ext.setToolbarBackColor
import com.kotlin.mvvm.databinding.ActivitySettingBinding
import com.kotlin.mvvm.ext.getAppThemeColor

class SettingActivity : BaseActivity() {

    private val binding by lazy { ActivitySettingBinding.inflate(layoutInflater) }

    override fun getContentView() = binding.root

    override fun initView(bundle: Bundle?) {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = StringUtils.getString(R.string.action_setting)
        binding.toolbar.setNavigationIcon(if (getAppThemeColor() == R.color.white)
            R.drawable.ic_arrow_black else R.drawable.ic_arrow_white)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun initData() {
        setToolbarBackColor(this, binding.toolbar, null)
    }
}