package com.kotlin.mvvm.network

import com.blankj.utilcode.util.LogUtils
import com.kotlin.mvvm.ext.e
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.Response
import okio.Buffer

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/10/14 15:23
 */
class HeadersInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()
        val urlBuilder = request.url.newBuilder()
        if ("GET" == request.method) {
            e("---请求头url：" + urlBuilder.build().toUrl())
            val httpUrl = urlBuilder.build()
            // 打印所有get参数
            val paramKeys = httpUrl.queryParameterNames
            for (key in paramKeys) {
                val value = httpUrl.queryParameter(key)
                e("---paramKeys = $key == $value")
            }
            // 将最终的url填充到request中
            requestBuilder.url(httpUrl)
        } else if ("POST" == request.method) {
            e("---请求头url：" + urlBuilder.build().toUrl())
            // 把已有的post参数添加到新的构造器
            if (request.body is FormBody) {
                e("---请求头参数：FormBody")
                // FormBody和url不太一样，若需添加公共参数，需要新建一个构造器
                val formBody = request.body as FormBody?
                val bodyBuilder = FormBody.Builder()
                for (i in 0 until formBody!!.size) {
                    bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i))
                }

                val newBody = bodyBuilder.build()
                // 打印所有post参数
                for (i in 0 until newBody.size) {
                    e("---" + newBody.name(i) + " = " + newBody.value(i))
                }
                requestBuilder
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Accept", "*/*")
                    .addHeader("Accept-Encoding", "Vary")
                // 将最终的表单body填充到request中
                requestBuilder.post(newBody)
            }

            if (request.body is MultipartBody) {
                LogUtils.e("---请求头参数：MultipartBody")
                val builder = MultipartBody.Builder()
                builder.setType(MultipartBody.FORM)
                val multipartBody = request.body as MultipartBody?
                for (i in 0 until multipartBody!!.size) {
                    val part = multipartBody.part(i)
                    builder.addPart(part)
                }
                val newBody = builder.build()
                // 打印所有post参数
                val buffer1 = Buffer()
                newBody.writeTo(buffer1)
                val postParams = buffer1.readUtf8()
                val split = postParams.split("\n".toRegex()).toTypedArray()
                val names: MutableList<String> = ArrayList()
                for (str in split) {
                    if (str.contains("Content-Disposition")) {
                        names.add(
                            str.replace("Content-Disposition: form-data; name=", "")
                                .replace("\"", "")
                        )
                    }
                }
                val parts = newBody.parts
                for (i in parts.indices) {
                    val part = parts[i]
                    val body1 = part.body
                    if (body1.contentLength() < 100) {
                        val buffer = Buffer()
                        body1.writeTo(buffer)
                        val value = buffer.readUtf8()
                        //打印 name和value
                        if (names.size > i) {
                            e("---params = " + names[i] + " = " + value)
                        }
                    } else {
                        if (names.size > i) {
                            e("---params = " + names[i])
                        }
                    }
                }
                requestBuilder.post(newBody)
            }
        }
        return chain.proceed(requestBuilder.build())
    }
}