/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.client.utils

import io.ktor.client.*
import io.ktor.util.*
import kotlinx.coroutines.*
import kotlinx.io.pool.*
import java.nio.*
import kotlin.coroutines.*

@Suppress("KDocMissingDocumentation", "unused")
@Deprecated(
    "Binary compatibility",
    level = DeprecationLevel.HIDDEN
)
val HTTP_CLIENT_THREAD_COUNT: Int = 2

@Suppress("KDocMissingDocumentation", "unused")
@Deprecated(
    "Binary compatibility",
    level = DeprecationLevel.HIDDEN
)
val HTTP_CLIENT_DEFAULT_DISPATCHER: CoroutineDispatcher
    get() = Dispatchers.IO

/**
 * Singleton pool of [ByteBuffer] objects used for [HttpClient].
 */

val HttpClientDefaultPool = ByteBufferPool()

@InternalAPI
class ByteBufferPool : DefaultPool<ByteBuffer>(DEFAULT_HTTP_POOL_SIZE) {
    override fun produceInstance(): ByteBuffer = ByteBuffer.allocate(DEFAULT_HTTP_BUFFER_SIZE)!!

    override fun clearInstance(instance: ByteBuffer): ByteBuffer = instance.apply { clear() }
}

/**
 * Run request blocking in [HttpClient] dispatcher.
 */
@KtorExperimentalAPI
actual fun <T> HttpClient.runBlocking(block: suspend CoroutineScope.() -> T): T =
    runBlocking(EmptyCoroutineContext, block)
