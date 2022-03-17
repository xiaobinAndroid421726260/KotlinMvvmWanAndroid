package com.kotlin.mvvm.ext

import android.util.Log


/**
 * description:
 *
 * @author Db_z
 * @Date 2021/10/8 10:57
 */

private var allow: Boolean = true

private const val TAG = "---LogUtils: "

fun d(msg: String?) {
    d(TAG, msg)
}

fun w(msg: String?) {
    w(TAG, msg)
}

fun i(msg: String?) {
    i(TAG, msg)
}

fun e(msg: String?) {
    e(TAG, msg)
}

fun e(tag: String?, msg: String?) {
    if (allow) {
        Log.e(tag, msg!!)
    }
}

fun d(tag: String?, msg: String?) {
    if (allow) {
        Log.d(tag, msg!!)
    }
}

fun i(tag: String?, msg: String?) {
    if (allow) {
        Log.i(tag, msg!!)
    }
}

fun w(tag: String?, msg: String?) {
    if (allow) {
        Log.w(tag, msg!!)
    }
}