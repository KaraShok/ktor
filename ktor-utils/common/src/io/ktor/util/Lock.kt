/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

@file:Suppress("KDocMissingDocumentation")

package io.ktor.util

import kotlinx.io.core.*

@InternalAPI
expect class Lock() : Closeable {
    fun lock()
    fun unlock()
}

@InternalAPI
expect class ReadWriteLock() : Closeable {
    fun readLock(): LockTicket
    fun writeLock(): LockTicket
}

@InternalAPI
inline fun <R> Lock.useLocked(block: () -> R): R {
    try {
        lock()
        return block()
    } finally {
        unlock()
    }
}

interface LockTicket : Closeable
