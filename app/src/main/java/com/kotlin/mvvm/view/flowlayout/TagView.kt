package com.kotlin.mvvm.view.flowlayout

import android.content.Context
import android.view.View
import android.widget.Checkable
import android.widget.FrameLayout

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/28 16:14
 */
class TagView(context: Context) : FrameLayout(context), Checkable {

    private var isChecked = false
    private val checkState = intArrayOf(android.R.attr.state_checked)

    fun getTagView(): View? = getChildAt(0)

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val states = super.onCreateDrawableState(extraSpace + 1)
        if (isChecked()) {
            mergeDrawableStates(states, checkState)
        }
        return states
    }

    override fun setChecked(checked: Boolean) {
        if (this.isChecked != checked) {
            this.isChecked = checked
            refreshDrawableState()
        }
    }

    override fun isChecked() = isChecked

    override fun toggle() {
        setChecked(!isChecked)
    }

}