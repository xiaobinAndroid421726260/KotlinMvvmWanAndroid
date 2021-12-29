package com.kotlin.mvvm.popup

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.kotlin.mvvm.popup.base.BasePopup
import razerdp.basepopup.BasePopupWindow

fun LifecycleOwner.showBottomPopup(
    otp: String? = null,
    onClickConfirm: (dialog: BasePopupWindow) -> Unit
): BasePopup? {
    val context = (if (this is Activity) this else if (this is Fragment) this.context else null)
        ?: return null
    val dialog = BasePopup(context)
//    dialog.apply {
//        val binding = PopupLoadingBinding.inflate(LayoutInflater.from(context))
//        contentView = binding.root
//        isOutSideTouchable = false
//        setOutSideDismiss(false)
//        popupGravity = Gravity.BOTTOM
//        dialog.contentView.apply {
////            binding.
////            tv_otp.text = otp
////            btn_confirm.onClick {
////                onClickConfirm(dialog)
////                dialog.dismiss()
////            }
//        }
//        showPopupWindow()
//    }
    return dialog
}