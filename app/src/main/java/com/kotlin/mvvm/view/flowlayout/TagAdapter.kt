package com.kotlin.mvvm.view.flowlayout

import android.view.View

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/28 15:59
 */
abstract class TagAdapter<T> @JvmOverloads constructor(data: MutableList<T>? = null) {

    var data: MutableList<T> = data ?: arrayListOf()
    private lateinit var mOnDataChangedListener: () -> Unit

    private val mCheckedPosList = HashSet<Int>()

    fun setOnDataChangedListener(listener: () -> Unit) {
        mOnDataChangedListener = listener
    }

    open fun setList(list: Collection<T>?) {
        if (list !== this.data) {
            this.data.clear()
            if (!list.isNullOrEmpty()) {
                this.data.addAll(list)
            }
        } else {
            if (!list.isNullOrEmpty()) {
                val newList = ArrayList(list)
                this.data.clear()
                this.data.addAll(newList)
            } else {
                this.data.clear()
            }
        }
    }

    open fun setSelectedList(vararg poses: Int) {
        val set = hashSetOf<Int>()
        for (pos in poses) {
            set.add(pos)
        }
        setSelectedList(set)
    }

    open fun setSelectedList(set: Set<Int>?) {
        mCheckedPosList.clear()
        if (set != null) {
            mCheckedPosList.addAll(set)
        }
        notifyDataChanged()
    }

    open fun getItem(position: Int) = data[position]

    abstract fun getView(parent: FlowLayout?, position: Int, t: T): View

    open fun setSelected(position: Int, t: T) = false

    open fun getPreCheckedList() = mCheckedPosList

    open fun getCount() = data.size

    open fun notifyDataChanged() {
        mOnDataChangedListener.invoke()
    }

    open fun onSelected(position: Int, view: View?) {}

    open fun unSelected(position: Int, view: View?) {}
}