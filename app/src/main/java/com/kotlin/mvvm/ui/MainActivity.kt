package com.kotlin.mvvm.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.kotlin.mvvm.R
import com.kotlin.mvvm.base.BaseActivity
import com.kotlin.mvvm.common.ScrollToTop
import com.kotlin.mvvm.ext.setToolbarBackColor
import com.kotlin.mvvm.databinding.ActivityMainBinding
import com.kotlin.mvvm.ext.loadFragments
import com.kotlin.mvvm.ext.onClick
import com.kotlin.mvvm.ext.showHideFragment
import com.kotlin.mvvm.ui.home.HomeFragment
import com.kotlin.mvvm.ui.project.ProjectFragment
import com.kotlin.mvvm.ui.setting.SettingActivity
import com.kotlin.mvvm.ui.square.SquareFragment
import com.kotlin.mvvm.ui.system.SystemFragment
import com.kotlin.mvvm.ui.wechat.WechatFragment

class MainActivity : BaseActivity() {

    companion object {
        private const val BOTTOM_INDEX = "bottom_index"

        private const val FRAGMENT_HOME = 0
        private const val FRAGMENT_SQUARE = 1
        private const val FRAGMENT_WECHAT = 2
        private const val FRAGMENT_SYSTEM = 3
        private const val FRAGMENT_PROJECT = 4
    }

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mFragments = mutableListOf<Fragment>().apply {
        add(HomeFragment())
        add(SquareFragment())
        add(WechatFragment())
        add(SystemFragment())
        add(ProjectFragment())
    }
    private var index = FRAGMENT_HOME

    override fun getContentView() = binding.root

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(BOTTOM_INDEX, index)
    }

    override fun initView(bundle: Bundle?) {
        setSupportActionBar(binding.toolbar)
        loadFragments(R.id.fl_container, FRAGMENT_HOME, mFragments)
        showFragment(FRAGMENT_HOME)
        savedInstanceState?.apply {
            index = getInt(BOTTOM_INDEX)
            showFragment(index)
        }
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> showFragment(FRAGMENT_HOME)
                R.id.action_square -> showFragment(FRAGMENT_SQUARE)
                R.id.action_wechat -> showFragment(FRAGMENT_WECHAT)
                R.id.action_system -> showFragment(FRAGMENT_SYSTEM)
                R.id.action_project -> showFragment(FRAGMENT_PROJECT)
            }
            return@setOnItemSelectedListener true
        }
        binding.actionButton.onClick {
            val fragment = mFragments[index]
            if (fragment is ScrollToTop) {
                fragment.scrollToTop()
            }
        }
    }

    override fun initData() {
        setToolbarBackColor(this, binding.toolbar, binding.actionButton)
    }

    private fun showFragment(index: Int) {
        this.index = index
        showHideFragment(mFragments[index])
        when (index) {
            FRAGMENT_HOME -> supportActionBar?.title = StringUtils.getString(R.string.home)
            FRAGMENT_SQUARE -> supportActionBar?.title = StringUtils.getString(R.string.square)
            FRAGMENT_WECHAT -> supportActionBar?.title = StringUtils.getString(R.string.wechat)
            FRAGMENT_SYSTEM -> supportActionBar?.title = StringUtils.getString(R.string.system)
            FRAGMENT_PROJECT -> supportActionBar?.title = StringUtils.getString(R.string.project)
        }
    }

    /**
     * 重新创建Activity
     */
    private fun recreateActivity() {
        window.setWindowAnimations(R.style.WindowAnimationFadeInOut)
        recreate()
    }

    override fun recreate() {
        super.recreate()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search) {
            ToastUtils.showShort("跳转搜索")
            return true
        } else if (item.itemId == R.id.action_setting) {
            ActivityUtils.startActivity(SettingActivity::class.java)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}