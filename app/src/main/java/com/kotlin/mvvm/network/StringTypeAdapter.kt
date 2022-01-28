package com.kotlin.mvvm.network

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

/**
 * description:
 *
 * @author Db_z
 * @Date 2021/10/14 15:44
 */
class StringTypeAdapter : TypeAdapter<String>(){

    override fun write(writer: JsonWriter?, value: String?) {
        try {
            if (value == null) {
                writer?.nullValue()
                return
            }
            writer?.value(value)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun read(reader: JsonReader?): String {
        try {
            if (reader!!.peek() == JsonToken.NULL) {
                reader.nextNull()
                return "" // 原先是返回null，这里改为返回空字符串
            }
            return reader.nextString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}