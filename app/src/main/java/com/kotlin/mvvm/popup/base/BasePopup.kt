package com.kotlin.mvvm.popup.base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.kotlin.mvvm.R
import razerdp.basepopup.BasePopupWindow

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/10/11 10:57
 */
open class BasePopup : BasePopupWindow {

    private val mHandler = Handler(Looper.getMainLooper())
    private var dismissWithRunnable: Runnable? = null
    private var mMap: Map<String, Any>? = null

    constructor(context: Context) : super(context) {
    }

    constructor(fragment: Fragment) : super(fragment) {
    }

    constructor(dialog: Dialog) : super(dialog) {
    }

    init {
        setBackgroundColor(Color.TRANSPARENT)
    }

    open fun setAnimationBottom(): BasePopupWindow? {
        showAnimation = AnimationUtils.loadAnimation(context, R.anim.dialog_bottom_in)
        dismissAnimation = AnimationUtils.loadAnimation(context, R.anim.dialog_bottom_out)
        return this
    }

    open fun setAnimationScale(): BasePopupWindow? {
        showAnimation = AnimationUtils.loadAnimation(context, R.anim.popup_scale_in)
        dismissAnimation = AnimationUtils.loadAnimation(context, R.anim.popup_scale_out)
        return this
    }

    open fun setCustomDataMap(map: Map<String, Any>?): BasePopupWindow? {
        mMap = map
        return this
    }

    /**
     * 延迟关闭100毫秒
     */
    open fun delayDismiss() {
        delayDismiss(100)
    }

    /**
     * 延迟关闭
     */
    open fun delayDismiss(delay: Long) {
        var d = delay
        if (d < 0) d = 0
        mHandler.postDelayed({
            dismiss()
            if (dismissWithRunnable != null) {
                dismissWithRunnable?.run()
                dismissWithRunnable = null
            }
        }, d)
    }

    /**
     * 延迟关闭 关闭之后的操作
     */
    open fun delayDismissWith(delay: Long, dismissWithRunnable: Runnable?) {
        this.dismissWithRunnable = dismissWithRunnable
        delayDismiss(delay)
    }
}