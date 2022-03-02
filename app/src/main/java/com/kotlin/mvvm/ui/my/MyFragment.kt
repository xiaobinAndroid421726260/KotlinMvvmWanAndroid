package com.kotlin.mvvm.ui.my

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils
import com.kotlin.mvvm.R
import com.kotlin.mvvm.base.BaseFragment
import com.kotlin.mvvm.common.ScrollToTop
import com.kotlin.mvvm.common.handler_code_collect
import com.kotlin.mvvm.common.handler_code_un_collect
import com.kotlin.mvvm.databinding.FragmentMyBinding
import com.kotlin.mvvm.databinding.LayoutEmptyRecyclerBinding
import com.kotlin.mvvm.ext.*
import com.kotlin.mvvm.ui.MainActivity
import com.kotlin.mvvm.ui.collect.CollectActivity
import com.kotlin.mvvm.ui.integral.IntegralRankActivity
import com.kotlin.mvvm.ui.integral.MyIntegralActivity
import com.kotlin.mvvm.ui.login.bean.LoginBean
import com.kotlin.mvvm.ui.message.MessageActivity
import com.kotlin.mvvm.ui.my.bean.OptionBean
import com.kotlin.mvvm.ui.questions.QuestionsAnswersActivity
import com.kotlin.mvvm.ui.setting.SettingActivity
import com.kotlin.mvvm.ui.share.ShareListActivity
import com.kotlin.mvvm.ui.square.SquareActivity

/**
 * description:
 *
 * @author Db_z
 * @Date 2022/1/20 14:35
 */
class MyFragment : BaseFragment(), ScrollToTop {

    private val binding by lazy { FragmentMyBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<MyViewModel>()
    private val mRvAdapter by lazy { MyOptionAdapter() }
    private val mAdapter by lazy { MyAdapter() }
    private var position = 0

    override fun getContentView() = binding.root

    override fun initView(bundle: Bundle?) {
        setLoadSir(binding.refreshLayout)
        binding.refreshLayout.setEnableLoadMore(false)
        binding.recyclerView.setLinearLayoutManager(mAdapter)
        val emptyBinding =
            LayoutEmptyRecyclerBinding.inflate(layoutInflater, binding.recyclerView, false)
        emptyBinding.tvEmpty.text = StringUtils.getString(R.string.not_questions_and_answers)
        mAdapter.setEmptyView(emptyBinding.root)
        binding.refreshLayout.setOnRefreshListener { mViewModel.getWendListJson(0, 5) }
        binding.rvOption.setGridLayoutManager(mRvAdapter, 4)
        mAdapter.setCollectionListener { collect, id, position ->
            this.position = position
            if (collect) {
                mViewModel.unCollectList(id)
            } else {
                mViewModel.collect(id)
            }
        }
        observeEvent()
        addDebouncingViews(
            binding.tvLogin,
            binding.tvMyIntegral,
            binding.tvMyIntegralRanking,
            binding.clQuestionsAndAnswers
        )
    }

    private fun observeEvent() {
        mViewModel.handlerCode.observe(this) { initRvOption(it) }
        mViewModel.mWendBean.observe(this) {
            if (it.datas.isNotEmpty()) {
                mAdapter.setList(it.datas)
            }
            binding.refreshLayout.finishRefresh(true)
            showContent()
        }
        mViewModel.handlerCode.observe(this) {
            when (it) {
                handler_code_collect -> {
                    mAdapter.data[position].collect = true
                    ToastUtils.showShort(StringUtils.getString(R.string.collect_success))
                }
                handler_code_un_collect -> {
                    mAdapter.data[position].collect = false
                    ToastUtils.showShort(StringUtils.getString(R.string.cancel_collect))
                }
            }
            mAdapter.notifyItemChanged(position)
        }
    }

    override fun initData() {
        activity?.let {
            val color = it.getThemeColor()
            binding.clHead.setBackgroundColor(color)
        }
        if (isLogin()) {
            binding.tvLogin.invisible()
            binding.tvName.visible()
            binding.tvUserId.visible()
            val user = GsonUtils.fromJson(getUser(), LoginBean::class.java)
            binding.tvName.text = user.publicName
            binding.tvUserId.text = String.format(StringUtils.getString(R.string.user_id), user.id)
        } else {
            binding.tvName.invisible()
            binding.tvUserId.invisible()
            binding.tvLogin.visible()
        }
        // 如果不是深色模式  而且主题是白色
        if (!getNightMode() && getAppThemeColor() == Color.WHITE) {
            binding.tvLogin.setTextColor(Color.BLACK)
            binding.tvName.setTextColor(Color.BLACK)
            binding.tvUserId.setTextColor(Color.BLACK)
        } else {
            activity?.let {
                val color = ContextCompat.getColor(it, R.color.white_title)
                binding.tvLogin.setTextColor(color)
                binding.tvName.setTextColor(color)
                binding.tvUserId.setTextColor(color)
            }
        }
        showContent()
        if (isLogin()) {
            mViewModel.getMessageCountUnread()
        } else {
            initRvOption(0)
        }
        mViewModel.getWendListJson(0, 5)
    }

    override fun onClickView(view: View?) {
        when (view?.id) {
            R.id.tv_login -> startLoginActivity()
            R.id.tv_my_integral -> checkLogin {
                ActivityUtils.startActivity(MyIntegralActivity::class.java)
            }
            R.id.tv_my_integral_ranking -> checkLogin {
                ActivityUtils.startActivity(IntegralRankActivity::class.java)
            }
            R.id.cl_questions_and_answers -> ActivityUtils.startActivity(QuestionsAnswersActivity::class.java)
        }
    }

    private fun initRvOption(count: Int) {
        val mList = mutableListOf<OptionBean>().apply {
            add(OptionBean(R.drawable.ic_share, StringUtils.getString(R.string.my_share)))
            add(OptionBean(R.drawable.ic_collection, StringUtils.getString(R.string.my_collect)))
            add(
                OptionBean(
                    R.drawable.ic_message_black_24dp,
                    StringUtils.getString(R.string.message),
                    count
                )
            )
            add(
                OptionBean(
                    R.drawable.ic_square_black_24dp,
                    StringUtils.getString(R.string.square_list)
                )
            )
            add(
                OptionBean(
                    if (getNightMode()) R.drawable.standard_mode else R.drawable.dark_mode,
                    if (getNightMode()) StringUtils.getString(R.string.standard_mode) else StringUtils.getString(
                        R.string.dark_mode
                    )
                )
            )
            add(OptionBean(R.drawable.ic_my_tool, StringUtils.getString(R.string.my_tool)))
            add(
                OptionBean(
                    R.drawable.ic_project_home_page,
                    StringUtils.getString(R.string.project_home)
                )
            )
            add(
                OptionBean(
                    R.drawable.ic_setting_white_reverse_24dp,
                    StringUtils.getString(R.string.action_setting)
                )
            )
        }
        mRvAdapter.setList(mList)
        mRvAdapter.setOnItemClickListener { _, _, position ->
            when (position) {
                0 -> checkLogin { ActivityUtils.startActivity(ShareListActivity::class.java) }
                1 -> checkLogin { ActivityUtils.startActivity(CollectActivity::class.java) }
                2 -> checkLogin { ActivityUtils.startActivity(MessageActivity::class.java) }
                3 -> ActivityUtils.startActivity(SquareActivity::class.java)
                4 -> {
                    if (getNightMode()) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        setNightMode(false)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        setNightMode(true)
                    }
                    activity?.let {
                        if (it is MainActivity) {
                            it.initThemeColor()
                        }
                    }
                    initData()
                }
                5 -> startWebViewActivity(
                    0,
                    StringUtils.getString(R.string.wan_android_tools),
                    StringUtils.getString(R.string.my_tool)

                )
                6 -> startWebViewActivity(
                    0,
                    StringUtils.getString(R.string.git_url_wan_android),
                    StringUtils.getString(R.string.project_home)
                )
                7 -> ActivityUtils.startActivity(SettingActivity::class.java)
            }
        }
    }

    override fun scrollToTop() {
        binding.scrollView.smoothScrollTo(0, 0)
    }
}