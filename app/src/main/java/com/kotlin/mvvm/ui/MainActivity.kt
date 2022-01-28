package com.kotlin.mvvm.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.StringUtils
import com.kotlin.mvvm.R
import com.kotlin.mvvm.base.BaseActivity
import com.kotlin.mvvm.common.ScrollToTop
import com.kotlin.mvvm.databinding.ActivityMainBinding
import com.kotlin.mvvm.ext.*
import com.kotlin.mvvm.ui.home.HomeFragment
import com.kotlin.mvvm.ui.my.MyFragment
import com.kotlin.mvvm.ui.project.ProjectFragment
import com.kotlin.mvvm.ui.search.SearchActivity
import com.kotlin.mvvm.ui.setting.SettingActivity
import com.kotlin.mvvm.ui.system.SystemFragment
import com.kotlin.mvvm.ui.wechat.WechatFragment

class MainActivity : BaseActivity() {

    companion object {
        private const val BOTTOM_INDEX = "bottom_index"
        private const val FRAGMENT_HOME = 0
        private const val FRAGMENT_WECHAT = 1
        private const val FRAGMENT_SYSTEM = 2
        private const val FRAGMENT_PROJECT = 3
        private const val FRAGMENT_MY = 4
    }

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mFragments = mutableListOf<Fragment>().apply {
        add(HomeFragment())
        add(WechatFragment())
        add(SystemFragment())
        add(ProjectFragment())
        add(MyFragment())
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
                R.id.action_wechat -> showFragment(FRAGMENT_WECHAT)
                R.id.action_system -> showFragment(FRAGMENT_SYSTEM)
                R.id.action_project -> showFragment(FRAGMENT_PROJECT)
                R.id.action_my -> showFragment(FRAGMENT_MY)
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
        setToolbarBackColor(this, binding.toolbar, binding.actionButton, false)
        setBottomNavigationItemColor()
        // 重新可见时判断BottomNavigation颜色是否根据主题变化，然后重新创建
        if (getNavBar() && getNavBarColor()) {
            setNavBarColor(false)
            recreateActivity()
        }
    }

    private fun setBottomNavigationItemColor() {
        setBottomNavigationItemColor(this, binding.bottomNavigation)
    }

    private fun showFragment(index: Int) {
        this.index = index
        showHideFragment(mFragments[index])
        when (index) {
            FRAGMENT_HOME -> supportActionBar?.title = StringUtils.getString(R.string.home)
            FRAGMENT_WECHAT -> supportActionBar?.title = StringUtils.getString(R.string.wechat)
            FRAGMENT_SYSTEM -> supportActionBar?.title = StringUtils.getString(R.string.system)
            FRAGMENT_PROJECT -> supportActionBar?.title = StringUtils.getString(R.string.project)
            FRAGMENT_MY -> supportActionBar?.title = StringUtils.getString(R.string.my)
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
        try {
            val transaction = supportFragmentManager.beginTransaction()
            val fragments = supportFragmentManager.fragments
            for (fragment in fragments) {
                transaction.remove(fragment)
            }
            transaction.commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.recreate()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        if (!getNightMode()){
            // 如果是白色主题  那么右侧的图标就会变成黑色
            if (getAppThemeColor() == Color.WHITE) {
                val search = menu?.findItem(R.id.action_search)
                val setting = menu?.findItem(R.id.action_setting)
                val colorStateList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.color_black))
                MenuItemCompat.setIconTintList(search, colorStateList)
                MenuItemCompat.setIconTintList(setting, colorStateList)
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search) {
            ActivityUtils.startActivity(SearchActivity::class.java)
        } else if (item.itemId == R.id.action_setting) {
            ActivityUtils.startActivity(SettingActivity::class.java)
        }
        return super.onOptionsItemSelected(item)
    }
}