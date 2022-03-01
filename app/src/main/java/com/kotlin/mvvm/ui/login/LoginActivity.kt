package com.kotlin.mvvm.ui.login

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.kotlin.mvvm.R
import com.kotlin.mvvm.base.BaseActivity
import com.kotlin.mvvm.databinding.ActivityLoginBinding
import android.graphics.drawable.GradientDrawable
import androidx.core.content.ContextCompat
import com.kotlin.mvvm.ext.*

class LoginActivity : BaseActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<LoginViewModel>()

    override fun getContentView() = binding.root

    override fun initView(bundle: Bundle?) {
        refreshUiState(mViewModel.uiState)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = StringUtils.getString(R.string.login)
        binding.toolbar.setNavigationOnClickListener { finish() }
        mViewModel.mLoginRegisterBean.observe(this) {
            saveUser(GsonUtils.toJson(it))
            ToastUtils.showShort(StringUtils.getString(R.string.login_success))
            finish()
        }
        binding.tvLogin.onClick {
            mViewModel.login(
                binding.textInputText.editText?.text.toString(),
                binding.textInputPwd.editText?.text.toString()
            )
        }
        binding.tvRegister.onClick { ActivityUtils.startActivity(RegisterActivity::class.java) }
        binding.tvRegister.setTextColor(getThemeTextColor())
        val drawable = binding.tvLogin.background as GradientDrawable
        drawable.setColor(getThemeTextColor())
    }

    override fun initData() {
        setToolbarBackColor(this, binding.toolbar, null)
    }
}