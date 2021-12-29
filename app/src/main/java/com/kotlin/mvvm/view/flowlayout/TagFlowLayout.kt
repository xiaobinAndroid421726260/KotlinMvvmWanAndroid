package com.kotlin.mvvm.view.flowlayout

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import com.kotlin.mvvm.R
import com.kotlin.mvvm.ext.dp2px
import kotlin.collections.HashSet

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/28 15:49
 */
@SuppressLint("CustomViewStyleable")
class TagFlowLayout<T> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FlowLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val KEY_CHOOSE_POS = "key_choose_pos"
        private const val KEY_DEFAULT = "key_default"
    }

    private var mTagAdapter: TagAdapter<T>? = null
    private var mSelectedMax = -1 //-1为不限制数量
    private val mSelectedView = hashSetOf<Int>()
//    private lateinit var mOnSelectListener: (selectPosSet: HashSet<Int>?) -> Unit
    private lateinit var mOnTagClickListener: (
        view: View?,
        position: Int,
        parent: FlowLayout?
    ) -> Unit

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout)
        mSelectedMax = ta.getInt(R.styleable.FlowLayout_max_select, -1)
        ta.recycle()
    }

//    fun setOnSelectListener(listener: (selectPosSet: HashSet<Int>?) -> Unit) {
//        mOnSelectListener = listener
//    }

    fun setOnTagClickListener(listener: (view: View?, position: Int, parent: FlowLayout?) -> Unit) {
        mOnTagClickListener = listener
    }

    fun setAdapter(adapter: TagAdapter<T>?) {
        mTagAdapter = adapter
        mTagAdapter?.setOnDataChangedListener {
            mSelectedView.clear()
            changeAdapter()
        }
        mSelectedView.clear()
        changeAdapter()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        for (i in 0 until childCount) {
            val tagView = getChildAt(i) as TagView
            if (tagView.visibility == GONE) {
                continue
            }
            if (tagView.getTagView()?.visibility == GONE) {
                tagView.visibility = GONE
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    private fun changeAdapter() {
        removeAllViews()
        val adapter = mTagAdapter!!
        var tagViewContainer: TagView?
        val preCheckedList = mTagAdapter?.getPreCheckedList()
        for (i in 0 until adapter.getCount()) {
            val tagView = adapter.getView(this, i, adapter.getItem(i))
            tagViewContainer = TagView(context)
            tagView.isDuplicateParentStateEnabled = true
            if (tagView.layoutParams != null) {
                tagViewContainer.layoutParams = tagView.layoutParams
            } else {
                val lp = MarginLayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                )
                lp.setMargins(5f.dp2px(), 5f.dp2px(), 5f.dp2px(), 5f.dp2px())
                tagViewContainer.layoutParams = lp
            }
            val lp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            tagView.layoutParams = lp
            tagViewContainer.addView(tagView)
            addView(tagViewContainer)
            if (preCheckedList != null) {
                if (preCheckedList.contains(i)) {
                    setChildChecked(i, tagViewContainer)
                }
            }
            if (mTagAdapter!!.setSelected(i, adapter.getItem(i))) {
                setChildChecked(i, tagViewContainer)
            }
            tagView.isClickable = false
            val finalTagViewContainer: TagView = tagViewContainer
            tagViewContainer.setOnClickListener(OnClickListener {
                doSelect(finalTagViewContainer, i)
                mOnTagClickListener.invoke(finalTagViewContainer, i, this@TagFlowLayout)
            })
        }
        preCheckedList?.let { mSelectedView.addAll(it) }
    }

    fun setMaxSelectCount(count: Int) {
        if (mSelectedView.size > count) {
            mSelectedView.clear()
        }
        mSelectedMax = count
    }

    fun getSelectedList() = mSelectedView

    fun getAdapter() = mTagAdapter

    private fun setChildChecked(position: Int, view: TagView) {
        view.isChecked = true
        mTagAdapter?.onSelected(position, view.getTagView())
    }

    private fun setChildUnChecked(position: Int, view: TagView) {
        view.isChecked = false
        mTagAdapter?.unSelected(position, view.getTagView())
    }

    private fun doSelect(child: TagView, position: Int) {
        if (!child.isChecked) {
            //处理max_select=1的情况
            if (mSelectedMax == 1 && mSelectedView.size == 1) {
                val iterator: Iterator<Int> = mSelectedView.iterator()
                val preIndex = iterator.next()
                val pre = getChildAt(preIndex) as TagView
                setChildUnChecked(preIndex, pre)
                setChildChecked(position, child)
                mSelectedView.remove(preIndex)
                mSelectedView.add(position)
            } else {
                if (mSelectedMax > 0 && mSelectedView.size >= mSelectedMax) {
                    return
                }
                setChildChecked(position, child)
                mSelectedView.add(position)
            }
        } else {
            setChildUnChecked(position, child)
            mSelectedView.remove(position)
        }
//        mOnSelectListener.invoke(mSelectedView)
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable(KEY_DEFAULT, super.onSaveInstanceState())
        var selectPos = ""
        if (mSelectedView.size > 0) {
            for (key in mSelectedView) {
                selectPos += "$key|"
            }
            selectPos = selectPos.substring(0, selectPos.length - 1)
        }
        bundle.putString(KEY_CHOOSE_POS, selectPos)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            val mSelectPos = state.getString(KEY_CHOOSE_POS)
            if (!TextUtils.isEmpty(mSelectPos)) {
                val split = mSelectPos!!.split("\\|".toRegex()).toTypedArray()
                for (pos in split) {
                    val index = pos.toInt()
                    mSelectedView.add(index)
                    val tagView = getChildAt(index) as TagView
                    setChildChecked(index, tagView)
                }
            }
            super.onRestoreInstanceState(state.getParcelable(KEY_DEFAULT))
            return
        }
        super.onRestoreInstanceState(state)
    }
}