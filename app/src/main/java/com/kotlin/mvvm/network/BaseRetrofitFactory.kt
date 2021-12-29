package com.kotlin.mvvm.network

import androidx.viewbinding.BuildConfig
import com.kotlin.mvvm.api.ANDROID_URL
import com.kotlin.mvvm.api.getHost
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.reflect.ParameterizedType
import java.util.concurrent.TimeUnit

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/12/10 16:42
 */
abstract class BaseRetrofitFactory {

    protected abstract fun handleBuilder(builder: OkHttpClient.Builder)
    protected abstract fun retrofitBuilder(builder: Retrofit.Builder)

    companion object {
        private const val DEFAULT_TIMEOUT = 20
    }

    private val okHttpClient: OkHttpClient
        get() {
            val mOkHttpClientBuilder = OkHttpClient.Builder()
            // 设置超时时间
            mOkHttpClientBuilder.connectTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            mOkHttpClientBuilder.readTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            mOkHttpClientBuilder.writeTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
            mOkHttpClientBuilder.retryOnConnectionFailure(true) // 连接失败时重试
            // 添加头部拦截器
            mOkHttpClientBuilder.addInterceptor(NetworkStatusInterceptor())
            // 日志拦截器
            mOkHttpClientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
//            level = if (isShowLog) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
                level =
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.BASIC
            })
            mOkHttpClientBuilder.addInterceptor(HeadersInterceptor())
            handleBuilder(mOkHttpClientBuilder)
//            mOkHttpClient = mOkHttpClientBuilder.build()
            return mOkHttpClientBuilder.build()
        }

//    init {
//        val mOkHttpClientBuilder = OkHttpClient.Builder()
//        // 设置超时时间
//        mOkHttpClientBuilder.connectTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
//        mOkHttpClientBuilder.readTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
//        mOkHttpClientBuilder.writeTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
//        mOkHttpClientBuilder.retryOnConnectionFailure(true) // 连接失败时重试
//        // 添加头部拦截器
//        mOkHttpClientBuilder.addInterceptor(NetworkStatusInterceptor())
//        // 日志拦截器
//        mOkHttpClientBuilder.addInterceptor(HttpLoggingInterceptor().apply {
////            level = if (isShowLog) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
//            level =
//                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.BASIC
//        })
//        mOkHttpClientBuilder.addInterceptor(HeadersInterceptor())
//        handleBuilder(mOkHttpClientBuilder)
//        mOkHttpClient = mOkHttpClientBuilder.build()
//        getService(Api::class.java)
//    }

    fun <T> getService(
        serviceClass: Class<T>,
        hostType: Int = ANDROID_URL
    ): T {
        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
//            .addConverterFactory(GsonConverterFactory.create(getGson()))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .baseUrl(getHost(hostType))
        retrofitBuilder(retrofit)
        return create(serviceClass, retrofit.build())
    }

    /**
     * APi接口服务类
     */
    private fun <T> create(service: Class<T>, retrofit: Retrofit): T {
        return retrofit.create(service)
    }

    /**
     * 解析把null转换成""
     */
    private fun getGson(): Gson {
        return GsonBuilder()
            .setLenient() //设置时间格式
            .setDateFormat("yyyy-MM-dd hh:mm:ss") //设置解析的时候把null转换成""
            .registerTypeHierarchyAdapter(String::class.java, StringTypeAdapter())
            .registerTypeHierarchyAdapter(List::class.java, jsonDeserializer)
            .create()
    }

    private var jsonDeserializer = JsonDeserializer { json, typeOfT, context ->
        if (json.isJsonArray) {
            val array = json.asJsonArray
            val itemType = (typeOfT as ParameterizedType).actualTypeArguments[0]
            val list = arrayListOf<Any>()
            var i = 0
            while (i < array.size()) {
                val element = array[i]
                val item = context.deserialize<Any>(element, itemType)
                list.add(item)
                i++
            }
            return@JsonDeserializer list
        } else {
            //和接口类型不符，返回空List
            return@JsonDeserializer listOf()
        }
    }
}