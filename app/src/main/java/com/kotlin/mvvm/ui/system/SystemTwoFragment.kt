package com.kotlin.mvvm.ui.system

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kotlin.mvvm.base.BaseFragment
import com.kotlin.mvvm.common.ScrollToTop
import com.kotlin.mvvm.databinding.FragmentSystemTwoBinding
import com.kotlin.mvvm.ext.setLinearLayoutManager
import com.kotlin.mvvm.ui.system.bean.Article
import com.kotlin.mvvm.ui.system.bean.NaviBean
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/27 15:01
 */
class SystemTwoFragment : BaseFragment(), ScrollToTop {

    private val binding by lazy { FragmentSystemTwoBinding.inflate(layoutInflater) }
    private val mViewModel by viewModels<NaviViewModel>()
    private val mTitleAdapter by lazy { SystemTwoTitleAdapter() }
    private val mAdapter by lazy { SystemTwoContentAdapter() }
    private val mArticle = arrayListOf<Article>()

    override fun getContentView() = binding.root

    override fun initView(bundle: Bundle?) {
        setLoadSir(binding.rootView)
        binding.recyclerViewTitle.setLinearLayoutManager(mTitleAdapter)
        binding.recyclerView.setLinearLayoutManager(mAdapter)
        mAdapter.setAnimationWithDefault(BaseQuickAdapter.AnimationType.ScaleIn)
        mViewModel.mNaviBean.observe(this) {
            if (it.isNotEmpty()) {
                mArticle.clear()
                Observable.fromIterable(it).subscribe(object : Observer<NaviBean> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(t: NaviBean) {
                        t.check = false
                        mArticle.addAll(t.articles)
                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {
                        it[0].check = true
                        mTitleAdapter.setList(it)
                        mAdapter.setList(it)
                        showContent()
                    }
                })
            }
        }
        mTitleAdapter.setOnItemClickListener { _, _, position ->
            Observable.fromIterable(mTitleAdapter.data)
                .subscribe(object : Observer<NaviBean> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(t: NaviBean) {
                        t.check = false
                    }

                    override fun onError(e: Throwable) {

                    }

                    @SuppressLint("NotifyDataSetChanged")
                    override fun onComplete() {
                        mTitleAdapter.data[position].check = true
                        mTitleAdapter.notifyDataSetChanged()
                        // 以下方法都可以
                        val layoutManager =
                            binding.recyclerView.layoutManager as LinearLayoutManager
                        layoutManager.scrollToPositionWithOffset(position, 0)
//                        binding.recyclerView.smoothScrollToStartPosition(position)
//                        binding.recyclerView.smoothScrollToPosition(position, LinearSmoothScroller.SNAP_TO_START)
                    }
                })
        }
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            @SuppressLint("NotifyDataSetChanged")
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //获取滚动时的第一条展示的position
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisiblePosition = layoutManager.findFirstVisibleItemPosition()
                var index = 0
                for (i in 0 until firstVisiblePosition) {
                    index += mTitleAdapter.data[i].articles.size
                }
                //获取右侧数据的关联id
                val id = mArticle[index].chapterId
                //记录外部id， 更新左侧状态栏状态
                var position = 0
                for (i in 0 until mTitleAdapter.data.size){
                    mTitleAdapter.data[i].check = false
                    if (id == mTitleAdapter.data[i].cid){
                        position = i
                        mTitleAdapter.data[i].check = true
                    }
                }
                binding.recyclerViewTitle.smoothScrollToPosition(position)
                mTitleAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun initData() {
        mViewModel.getNaviJson()
    }

    override fun scrollToTop() {
        binding.recyclerView.smoothScrollToPosition(0)
    }
}