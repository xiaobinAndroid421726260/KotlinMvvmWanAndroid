package com.kotlin.mvvm.ext

import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.kotlin.mvvm.base.App

/**
 * description:
 *
 * @author Db_z
 * @Date 2022/01/22 16:28
 */
object CookieClass {

    /**Cookie*/
    private val cookiePersists = SharedPrefsCookiePersistor(App.CONTEXT)
    val cookieJar = PersistentCookieJar(SetCookieCache(), cookiePersists)

    /**清除Cookie*/
    fun clearCookie() = cookieJar.clear()

    /**是否有Cookie*/
    fun hasCookie() = cookiePersists.loadAll().isNotEmpty()
}