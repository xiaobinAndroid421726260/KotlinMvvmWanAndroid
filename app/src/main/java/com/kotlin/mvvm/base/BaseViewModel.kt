package com.kotlin.mvvm.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.mvvm.common.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/10/7 15:40
 */
open class BaseViewModel : ViewModel(), LifecycleObserver {

    val uiState = MutableStateFlow(UiState.LoadDefault)
    val handlerCode = MutableLiveData<Int>()

    /**
     * 运行在UI线程的协程 viewModelScope 已经实现了在onCleared取消协程
     */
    fun launchUI(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch { block() }

//    /**
//     * 在主线程中执行一个协程
//     */
//    fun launchOnMain(block: suspend CoroutineScope.() -> Unit) =
//        viewModelScope.launch(Dispatchers.Main) { block() }
}