package com.kotlin.mvvm.ui.search

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.StringUtils
import com.kotlin.mvvm.R
import com.kotlin.mvvm.base.BaseActivity
import com.kotlin.mvvm.common.ScrollToTop
import com.kotlin.mvvm.databinding.ActivitySearchBinding
import com.kotlin.mvvm.ext.*

class SearchActivity : BaseActivity() {

    companion object {
        private const val FRAGMENT_HISTORY = 0
        private const val FRAGMENT_RESULT = 1
    }

    private val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<SearchViewModel>()
    private var page = 0
    private val mFragments = mutableListOf<Fragment>().apply {
        add(SearchHistoryFragment())
        add(SearchResultFragment())
    }

    override fun getContentView() = binding.root

    override fun initView(bundle: Bundle?) {
        setSupportActionBar(binding.toolbar)
        binding.ivClear.onClick { binding.etSearch.setText("") }
        binding.actionButton.onClick {
            val fragment = mFragments[FRAGMENT_RESULT]
            if (mFragments[FRAGMENT_RESULT].isVisible && fragment is ScrollToTop) {
                fragment.scrollToTop()
            }
        }
        loadFragments(R.id.fragment, FRAGMENT_HISTORY, mFragments)
        showHideFragment(mFragments[FRAGMENT_HISTORY])
        binding.etSearch.run {
            addTextChangedListener(afterTextChanged = {
                binding.ivClear.visibility = if (it.isNullOrEmpty()) View.GONE else View.VISIBLE
            })
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val k = binding.etSearch.text?.toString()
                    if (StringUtils.isEmpty(k)) return@setOnEditorActionListener false
                    k?.let {
                        saveSearchHistoryData(it)
                        search(it)
                    }
                    true
                } else {
                    false
                }
            }
        }
        mViewModel.mSearch.observe(this){
            binding.etSearch.setText(it)
            binding.etSearch.setSelection(it.length)
            saveSearchHistoryData(it)
            search(it)
        }
    }

    override fun initData() {
        setToolbarBackColor(this, binding.toolbar, binding.actionButton, navigationListener = {
            binding.toolbar.setNavigationOnClickListener {
                if (mFragments[FRAGMENT_RESULT].isVisible) {
                    showHideFragment(mFragments[FRAGMENT_HISTORY])
                } else {
                    finish()
                }
            }
        })
    }

    private fun search(k: String) {
        KeyboardUtils.hideSoftInput(this)
        showHideFragment(mFragments[FRAGMENT_RESULT])
        mViewModel.getArticleQuery(page, k)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_search, menu)
        if (!getNightMode()) {
            // 如果是白色主题  那么右侧的图标就会变成黑色
            if (getAppThemeColor() == Color.WHITE) {
                val search = menu?.findItem(R.id.action_search)
                val colorStateList =
                    ColorStateList.valueOf(ContextCompat.getColor(this, R.color.color_black))
                MenuItemCompat.setIconTintList(search, colorStateList)
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search) {
            val k = binding.etSearch.text?.toString()
            if (StringUtils.isEmpty(k)) return super.onOptionsItemSelected(item)
            k?.let {
                saveSearchHistoryData(it)
                search(it)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}