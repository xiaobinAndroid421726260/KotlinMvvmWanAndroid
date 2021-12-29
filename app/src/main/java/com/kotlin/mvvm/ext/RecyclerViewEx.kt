@file:Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")

package com.kotlin.mvvm.ext

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

/**
 * description:
 * RecyclerView 列表相关扩展
 * @author Db_z
 * @Date 2021/08/26 16:43
 */

/**
 * 设置线性 item 布局管理器
 *
 * @param adapter RecyclerView 的适配器
 * @param orientation 布局方向，默认为 1，即 VERTICAL 方向
 * @param reverseLayout 是否反转，默认 false 不反转
 */
@kotlin.internal.InlineOnly
inline fun RecyclerView.setLinearLayoutManager(
    adapter: RecyclerView.Adapter<*>,
    @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false
) {
    this.layoutManager = LinearLayoutManager(context, orientation, reverseLayout)
    this.adapter = adapter
    setHasFixedSize(true)
}

@kotlin.internal.InlineOnly
inline fun RecyclerView.setGridLayoutManager(
    adapter: RecyclerView.Adapter<*>,
    spanCount: Int,
    @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false
) {
    this.layoutManager = GridLayoutManager(context, spanCount, orientation, reverseLayout)
    this.adapter = adapter
    setHasFixedSize(true)
}

/**
 * 刷新列表适配器
 */
@SuppressLint("NotifyDataSetChanged")
@kotlin.internal.InlineOnly
inline fun RecyclerView.Adapter<out RecyclerView.ViewHolder>.notifyAllDataSetChanged() {
    this.notifyDataSetChanged()
}

/**
 * 顺滑地滚动到指定 position 的起始位置
 *
 * @param position 目标 position
 */
@kotlin.internal.InlineOnly
inline fun RecyclerView.smoothScrollToStartPosition(position: Int) =
    smoothScrollToPosition(position, LinearSmoothScroller.SNAP_TO_START)

/**
 * 顺滑地滚动到指定 position 的末端位置
 *
 * @param position 目标 position
 */
@kotlin.internal.InlineOnly
inline fun RecyclerView.smoothScrollToEndPosition(position: Int) =
    smoothScrollToPosition(position, LinearSmoothScroller.SNAP_TO_END)

/**
 * 顺滑地滚动到指定 position 的指定位置
 *
 * @param position 目标 position
 * @param snapPreference 与指定位置的对齐方式
 */
@kotlin.internal.InlineOnly
inline fun RecyclerView.smoothScrollToPosition(position: Int, snapPreference: Int) =
    layoutManager?.let {
        val smoothScroller = LinearSmoothScroller(context, snapPreference)
        smoothScroller.targetPosition = position
        it.startSmoothScroll(smoothScroller)
    }

/**
 * 顺滑滚动并线性对齐
 *
 * @param snapPreference 线性对齐的方式
 */
@kotlin.internal.InlineOnly
inline fun LinearSmoothScroller(context: Context, snapPreference: Int) =
    object : LinearSmoothScroller(context) {
        override fun getVerticalSnapPreference() = snapPreference
        override fun getHorizontalSnapPreference() = snapPreference
    }

/**
 * 设置空布局
 */
@kotlin.internal.InlineOnly
inline fun RecyclerView.setEmptyView(owner: LifecycleOwner, emptyView: View) =
    observeDataEmpty(owner) { emptyView.isVisible = it }

/**
 * 观察数据是否为空
 */
@kotlin.internal.InlineOnly
inline fun RecyclerView.observeDataEmpty(owner: LifecycleOwner, noinline block: (Boolean) -> Unit) {
    owner.lifecycle.addObserver(object : LifecycleObserver {
        private var observer: RecyclerView.AdapterDataObserver? = null

        @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
        fun onCreate() {
            if (observer == null) {
                val adapter = checkNotNull(adapter) {
                    "RecyclerView needs to set up the adapter before setting up an empty view."
                }
                observer = AdapterDataEmptyObserver(adapter, block)
                adapter.registerAdapterDataObserver(observer!!)
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            observer?.let {
                adapter?.unregisterAdapterDataObserver(it)
                observer = null
            }
        }
    })
}

class AdapterDataEmptyObserver(
    private val adapter: RecyclerView.Adapter<*>,
    private val checkEmpty: (Boolean) -> Unit
) : RecyclerView.AdapterDataObserver() {

    override fun onChanged() {
        super.onChanged()
        checkEmpty(isDataEmpty)
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        super.onItemRangeInserted(positionStart, itemCount)
        checkEmpty(isDataEmpty)
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        super.onItemRangeRemoved(positionStart, itemCount)
        checkEmpty(isDataEmpty)
    }

    private val isDataEmpty get() = adapter.itemCount == 0
}

