package com.kotlin.mvvm.network

import com.kotlin.mvvm.api.ANDROID_URL
import com.kotlin.mvvm.api.Api
import okhttp3.*
import retrofit2.Retrofit

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/10/14 15:09
 */
class RetrofitFactory private constructor(hostType: Int = ANDROID_URL) : BaseRetrofitFactory() {

    val service by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        getService(Api::class.java, hostType)
    }

    companion object {
        val instance: RetrofitFactory by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RetrofitFactory()
        }
//        private var instance: RetrofitFactory? = null
//        fun getInStance(hostType: Int = ANDROID_URL) = instance ?: synchronized(this) {
//            instance ?: RetrofitFactory(hostType).also { instance = it }
//        }
    }

    override fun handleBuilder(builder: OkHttpClient.Builder) {

    }

    override fun retrofitBuilder(builder: Retrofit.Builder) {

    }
}