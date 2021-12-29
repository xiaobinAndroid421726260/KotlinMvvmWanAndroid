@file:Suppress("INVISIBLE_REFERENCE", "INVISIBLE_MEMBER")

package com.kotlin.mvvm.ext

import android.view.View
import android.view.ViewGroup

/**
 * description: View 操作相关扩展
 *
 * @author Db_z
 * @Date 2021/09/26 17:28
 */

/**
 * 设置view显示
 */
@kotlin.internal.InlineOnly
inline fun View?.visible() {
    this ?: return
    visibility = View.VISIBLE
}

/**
 * 设置view占位隐藏
 */
@kotlin.internal.InlineOnly
inline fun View?.invisible() {
    this ?: return
    visibility = View.INVISIBLE
}

/**
 * 设置view隐藏
 */
@kotlin.internal.InlineOnly
inline fun View?.gone() {
    this ?: return
    visibility = View.GONE
}

/**
 * 根据条件设置view显示隐藏
 * @param flag 为true 显示，为false 隐藏
 */
@kotlin.internal.InlineOnly
inline fun View?.visibleOrGone(flag: Boolean) {
    this ?: return
    visibility = if (flag) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

/**
 * 根据条件设置view显示隐藏
 * @param flag 为true 显示，为false 隐藏
 */
@kotlin.internal.InlineOnly
inline fun View?.visibleOrInvisible(flag: Boolean) {
    this ?: return
    visibility = if (flag) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

var lastClickTime = 0L
/**
 * 防抖处理，防止重复点击事件，默认0.5秒内不可重复点击
 * @param interval 时间间隔，默认0.5秒
 * @param action 执行方法
 */
@kotlin.internal.InlineOnly
inline fun View.onClick(interval: Long = 500, crossinline action: (view: View) -> Unit) {
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        val flag: Boolean = !(lastClickTime != 0L && (currentTime - lastClickTime < interval))
        lastClickTime = currentTime
        if (flag) action(it)
    }
}

/**
 * View长按点击事件
 */
@kotlin.internal.InlineOnly
inline fun View.onLongClick(crossinline block: (View) -> Unit) {
    setOnLongClickListener {
        block(it)
        true
    }
}

/**
 * 更新View
 */
@kotlin.internal.InlineOnly
inline fun View.resize(block: ViewGroup.LayoutParams.() -> Boolean) {
    if (layoutParams.block()) requestLayout()
}

/**
 * 批量调整View尺寸
 */
fun View.resizeByDp(
    w: Float = -1F,
    h: Float = -1F,
    lm: Float = -1F,
    tm: Float = -1F,
    rm: Float = -1F,
    bm: Float = -1F
) {
    fun toInt(p: Float = -1F): Int {

        return if (p > 0) p.dp2px() else p.toInt()
    }
    resize(toInt(w), toInt(h), toInt(lm), toInt(tm), toInt(rm), toInt(bm))
}

/**
 * 调整View尺寸
 */
fun View.resize(
    w: Int = -1,
    h: Int = -1,
    lm: Int = -1,
    tm: Int = -1,
    rm: Int = -1,
    bm: Int = -1
) {
    var change = false
    if (w >= 0 && layoutParams.width != w) {
        layoutParams.width = w
        change = true
    }
    if (h >= 0 && layoutParams.height != w) {
        layoutParams.height = h
        change = true
    }
    (layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
        if (lm >= 0 && leftMargin != lm) {
            leftMargin = lm
            change = true
        }
        if (tm >= 0 && topMargin != tm) {
            topMargin = tm
            change = true
        }
        if (rm >= 0 && rightMargin != rm) {
            rightMargin = rm
            change = true
        }
        if (bm >= 0 && bottomMargin != bm) {
            bottomMargin = bm
            change = true
        }
    }
    if (change) requestLayout()
}