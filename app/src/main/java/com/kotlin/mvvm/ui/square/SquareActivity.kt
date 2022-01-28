package com.kotlin.mvvm.ui.square

import android.os.Bundle
import com.blankj.utilcode.util.StringUtils
import com.kotlin.mvvm.R
import com.kotlin.mvvm.base.BaseActivity
import com.kotlin.mvvm.databinding.ActivitySquareBinding
import com.kotlin.mvvm.ext.onClick
import com.kotlin.mvvm.ext.setToolbarBackColor

class SquareActivity : BaseActivity() {

    private val binding by lazy { ActivitySquareBinding.inflate(layoutInflater) }

    override fun getContentView() = binding.root

    override fun initView(bundle: Bundle?) {
        setSupportActionBar(binding.layoutTitle.toolbar)
        supportActionBar?.title = StringUtils.getString(R.string.square)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment, SquareFragment())
            .commitAllowingStateLoss()
        binding.actionButton.onClick {
            val fragment = supportFragmentManager.findFragmentById(R.id.fragment)
            if (fragment is SquareFragment){
                fragment.scrollToTop()
            }
        }
    }

    override fun initData() {
        setToolbarBackColor(this, binding.layoutTitle.toolbar, binding.actionButton)
    }
}