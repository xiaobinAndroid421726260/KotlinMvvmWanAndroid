package com.kotlin.mvvm.common

import com.blankj.utilcode.util.GsonUtils
import com.kotlin.mvvm.ext.e
import java.io.Serializable
import kotlin.jvm.JvmField
import kotlin.jvm.JvmInline
import kotlin.jvm.JvmName

/*
 * Copyright 2010-2018 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

/**
 * description:
 *  A discriminated union that encapsulates a successful outcome with a value of type [T]
 *  or a failure with an arbitrary [Throwable] exception.
 *
 * @author Db_z
 * @Date 2021/12/10 11:14
 */

@SinceKotlin("1.3")
@JvmInline
value class BaseResult<out T> @PublishedApi internal constructor(
    @PublishedApi
    internal val value: Any?
) : Serializable {

    init {
        e("------BaseResult = " + GsonUtils.toJson(value))
    }

    /**
     * Returns `true` if this instance represents a successful outcome.
     * In this case [isFailure] returns `false`.
     */
    val isSuccess: Boolean get() = value !is Failure

    /**
     * Returns `true` if this instance represents a failed outcome.
     * In this case [isSuccess] returns `false`.
     */
    val isFailure: Boolean get() = value is Failure

    // value & exception retrieval

    /**
     * Returns the encapsulated value if this instance represents [success][Result.isSuccess] or `null`
     * if it is [failure][Result.isFailure].
     *
     * This function is a shorthand for `getOrElse { null }` (see [getOrElse]) or
     * `fold(onSuccess = { it }, onFailure = { null })` (see [fold]).
     */
    @Suppress("UNCHECKED_CAST")
    fun getOrNull(): T? =
        when {
            isFailure -> null
            else -> value as T
        }

    /**
     * Returns the encapsulated [Throwable] exception if this instance represents [failure][isFailure] or `null`
     * if it is [success][isSuccess].
     *
     * This function is a shorthand for `fold(onSuccess = { null }, onFailure = { it })` (see [fold]).
     */
    fun exceptionOrNull(): Throwable? =
        when (value) {
            is Failure -> value.exception
            else -> null
        }

    /**
     * Returns a string `Success(v)` if this instance represents [success][Result.isSuccess]
     * where `v` is a string representation of the value or a string `Failure(x)` if
     * it is [failure][isFailure] where `x` is a string representation of the exception.
     */
    override fun toString(): String =
        when (value) {
            is Failure -> value.toString() // "Failure($exception)"
            else -> "Success($value)"
        }

    // companion with constructors

    /**
     * Companion object for [Result] class that contains its constructor functions
     * [success] and [failure].
     */
    companion object {
        /**
         * Returns an instance that encapsulates the given [value] as successful value.
         */
        @Suppress("INAPPLICABLE_JVM_NAME")
        @JvmName("success")
        fun <T> success(value: T): BaseResult<T> =
            BaseResult(value)

        /**
         * Returns an instance that encapsulates the given [Throwable] [exception] as failure.
         */
        @Suppress("INAPPLICABLE_JVM_NAME")
        @JvmName("failure")
        fun <T> failure(exception: Throwable): BaseResult<T> =
            BaseResult(createFailure(exception))
    }

    internal class Failure(
        @JvmField
        val exception: Throwable
    ) : Serializable {
        override fun equals(other: Any?): Boolean = other is Failure && exception == other.exception
        override fun hashCode(): Int = exception.hashCode()
        override fun toString(): String = "Failure($exception)"
    }
}

/**
 * Creates an instance of internal marker [Result.Failure] class to
 * make sure that this class is not exposed in ABI.
 */
@PublishedApi
@SinceKotlin("1.3")
internal fun createFailure(exception: Throwable): Any =
    BaseResult.Failure(exception)

/**
 * Throws exception if the result is failure. This internal function minimizes
 * inlined bytecode for [getOrThrow] and makes sure that in the future we can
 * add some exception-augmenting logic here (if needed).
 */
@PublishedApi
@SinceKotlin("1.3")
internal fun BaseResult<*>.throwOnFailure() {
    if (value is BaseResult.Failure) throw value.exception
}

/**
 * Calls the specified function [block] and returns its encapsulated result if invocation was successful,
 * catching any [Throwable] exception that was thrown from the [block] function execution and encapsulating it as a failure.
 */
@SinceKotlin("1.3")
inline fun <R> runCatching(block: () -> R): BaseResult<R> {
    return try {
        BaseResult.success(block())
    } catch (e: Throwable) {
        BaseResult.failure(e)
    }
}

/**
 * Calls the specified function [block] with `this` value as its receiver and returns its encapsulated result if invocation was successful,
 * catching any [Throwable] exception that was thrown from the [block] function execution and encapsulating it as a failure.
 */
@SinceKotlin("1.3")
inline fun <T, R> T.runCatching(block: T.() -> R): BaseResult<R> {
    return try {
        BaseResult.success(block())
    } catch (e: Throwable) {
        BaseResult.failure(e)
    }
}

// -- extensions ---

/**
 * Returns the encapsulated value if this instance represents [success][Result.isSuccess] or throws the encapsulated [Throwable] exception
 * if it is [failure][Result.isFailure].
 *
 * This function is a shorthand for `getOrElse { throw it }` (see [getOrElse]).
 */
@Suppress("UNCHECKED_CAST")
@SinceKotlin("1.3")
fun <T> BaseResult<T>.getOrThrow(): T {
    throwOnFailure()
    return value as T
}

/**
 * Returns the encapsulated value if this instance represents [success][Result.isSuccess] or the
 * result of [onFailure] function for the encapsulated [Throwable] exception if it is [failure][Result.isFailure].
 *
 * Note, that this function rethrows any [Throwable] exception thrown by [onFailure] function.
 *
 * This function is a shorthand for `fold(onSuccess = { it }, onFailure = onFailure)` (see [fold]).
 */
@Suppress("UNCHECKED_CAST")
@SinceKotlin("1.3")
inline fun <R, T : R> BaseResult<T>.getOrElse(onFailure: (exception: Throwable) -> R): R {
//    contract {
//        callsInPlace(onFailure, InvocationKind.AT_MOST_ONCE)
//    }
    return when (val exception = exceptionOrNull()) {
        null -> value as T
        else -> onFailure(exception)
    }
}

/**
 * Returns the encapsulated value if this instance represents [success][Result.isSuccess] or the
 * [defaultValue] if it is [failure][Result.isFailure].
 *
 * This function is a shorthand for `getOrElse { defaultValue }` (see [getOrElse]).
 */
@Suppress("UNCHECKED_CAST")
@SinceKotlin("1.3")
fun <R, T : R> BaseResult<T>.getOrDefault(defaultValue: R): R {
    if (isFailure) return defaultValue
    return value as T
}

/**
 * Returns the result of [onSuccess] for the encapsulated value if this instance represents [success][Result.isSuccess]
 * or the result of [onFailure] function for the encapsulated [Throwable] exception if it is [failure][Result.isFailure].
 *
 * Note, that this function rethrows any [Throwable] exception thrown by [onSuccess] or by [onFailure] function.
 */
@Suppress("UNCHECKED_CAST")
@SinceKotlin("1.3")
inline fun <R, T> BaseResult<T>.fold(
    onSuccess: (value: T) -> R,
    onFailure: (exception: Throwable) -> R
): R {
//    contract {
//        callsInPlace(onSuccess, InvocationKind.AT_MOST_ONCE)
//        callsInPlace(onFailure, InvocationKind.AT_MOST_ONCE)
//    }
    return when (val exception = exceptionOrNull()) {
        null -> onSuccess(value as T)
        else -> onFailure(exception)
    }
}


/**
 * Returns the encapsulated result of the given [transform] function applied to the encapsulated value
 * if this instance represents [success][Result.isSuccess] or the
 * original encapsulated [Throwable] exception if it is [failure][Result.isFailure].
 *
 * Note, that this function rethrows any [Throwable] exception thrown by [transform] function.
 * See [mapCatching] for an alternative that encapsulates exceptions.
 */
@Suppress("UNCHECKED_CAST")
@SinceKotlin("1.3")
inline fun <R, T> BaseResult<T>.map(transform: (value: T) -> R): BaseResult<R> {
//    contract {
//        callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
//    }
    return when {
        isSuccess -> BaseResult.success(transform(value as T))
        else -> BaseResult(value)
    }
}

/**
 * Returns the encapsulated result of the given [transform] function applied to the encapsulated value
 * if this instance represents [success][Result.isSuccess] or the
 * original encapsulated [Throwable] exception if it is [failure][Result.isFailure].
 *
 * This function catches any [Throwable] exception thrown by [transform] function and encapsulates it as a failure.
 * See [map] for an alternative that rethrows exceptions from `transform` function.
 */
@Suppress("UNCHECKED_CAST")
@SinceKotlin("1.3")
inline fun <R, T> BaseResult<T>.mapCatching(transform: (value: T) -> R): BaseResult<R> {
    return when {
        isSuccess -> runCatching { transform(value as T) }
        else -> BaseResult(value)
    }
}

/**
 * Returns the encapsulated result of the given [transform] function applied to the encapsulated [Throwable] exception
 * if this instance represents [failure][Result.isFailure] or the
 * original encapsulated value if it is [success][Result.isSuccess].
 *
 * Note, that this function rethrows any [Throwable] exception thrown by [transform] function.
 * See [recoverCatching] for an alternative that encapsulates exceptions.
 */
@SinceKotlin("1.3")
inline fun <R, T : R> BaseResult<T>.recover(transform: (exception: Throwable) -> R): BaseResult<R> {
//    contract {
//        callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
//    }
    return when (val exception = exceptionOrNull()) {
        null -> this
        else -> BaseResult.success(transform(exception))
    }
}

/**
 * Returns the encapsulated result of the given [transform] function applied to the encapsulated [Throwable] exception
 * if this instance represents [failure][Result.isFailure] or the
 * original encapsulated value if it is [success][Result.isSuccess].
 *
 * This function catches any [Throwable] exception thrown by [transform] function and encapsulates it as a failure.
 * See [recover] for an alternative that rethrows exceptions.
 */
@SinceKotlin("1.3")
inline fun <R, T : R> BaseResult<T>.recoverCatching(transform: (exception: Throwable) -> R): BaseResult<R> {
    return when (val exception = exceptionOrNull()) {
        null -> this
        else -> runCatching { transform(exception) }
    }
}

// "peek" onto value/exception and pipe

/**
 * Performs the given [action] on the encapsulated [Throwable] exception if this instance represents [failure][Result.isFailure].
 * Returns the original `Result` unchanged.
 */
@SinceKotlin("1.3")
inline fun <T> BaseResult<T>.onFailure(action: (exception: Throwable) -> Unit): BaseResult<T> {
//    contract {
//        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
//    }
    exceptionOrNull()?.let { action(it) }
    return this
}

/**
 * Performs the given [action] on the encapsulated value if this instance represents [success][Result.isSuccess].
 * Returns the original `Result` unchanged.
 */
@Suppress("UNCHECKED_CAST")
@SinceKotlin("1.3")
inline fun <T> BaseResult<T>.onSuccess(action: (value: T) -> Unit): BaseResult<T> {
//    contract {
//        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
//    }
    if (isSuccess) action(value as T)
    return this
}