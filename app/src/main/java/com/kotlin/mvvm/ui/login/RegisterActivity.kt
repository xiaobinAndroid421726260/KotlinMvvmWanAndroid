package com.kotlin.mvvm.ui.login

import android.os.Bundle
import androidx.activity.viewModels
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.kotlin.mvvm.R
import com.kotlin.mvvm.base.BaseActivity
import com.kotlin.mvvm.databinding.ActivityRegisterBinding
import com.kotlin.mvvm.ext.onClick
import com.kotlin.mvvm.ext.setToolbarBackColor

class RegisterActivity : BaseActivity() {

    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<LoginViewModel>()

    override fun getContentView() = binding.root

    override fun initView(bundle: Bundle?) {
        refreshUiState(mViewModel.uiState)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = StringUtils.getString(R.string.register)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
        binding.tvGoLogin.onClick { finish() }
        binding.tvRegister.onClick {
            mViewModel.register(
                binding.textInputText.editText?.text.toString(),
                binding.textInputPwd.editText?.text.toString(),
                binding.textInputRePwd.editText?.text.toString()
            )
        }
        mViewModel.mLoginRegisterBean.observe(this) {
            ToastUtils.showShort(StringUtils.getString(R.string.register_success))
            finish()
        }
    }

    override fun initData() {
        setToolbarBackColor(this, binding.toolbar, null)
    }
}