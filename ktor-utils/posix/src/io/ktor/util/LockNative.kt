/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.util

import kotlinx.cinterop.*
import kotlinx.io.core.*
import platform.posix.*
import utils.*
import kotlin.native.concurrent.*

@InternalAPI
actual class Lock : Closeable {
    private val mutex = nativeHeap.alloc<ktor_mutex_t>()

    init {
        freeze()
        ktor_mutex_create(mutex.ptr).checkResult { "Failed to create mutex." }
    }

    actual fun lock() {
        ktor_mutex_lock(mutex.ptr).checkResult { "Failed to lock mutex." }
    }

    actual fun unlock() {
        ktor_mutex_unlock(mutex.ptr).checkResult { "Failed to unlock mutex." }
    }

    override fun close() {
        ktor_mutex_destroy(mutex.ptr)
        nativeHeap.free(mutex)
    }
}

@InternalAPI
actual class ReadWriteLock actual constructor() : Closeable {
    private val lock = nativeHeap.alloc<ktor_rwlock>()

    private val ticket = object : LockTicket {
        override fun close() {
            ktor_rwlock_unlock(lock.ptr).checkResult { "Failed to release lock ticket." }
        }
    }

    init {
        freeze()
        ktor_rwlock_create(lock.ptr).checkResult { "Failed to create mutex." }
    }

    actual fun readLock(): LockTicket {
        ktor_rwlock_read(lock.ptr).checkResult { "Failed to readLock mutex." }
        return ticket
    }

    actual fun writeLock(): LockTicket {
        ktor_rwlock_read(lock.ptr).checkResult { "Failed to writeLock mutex." }
        return ticket
    }

    override fun close() {
        ktor_rwlock_destroy(lock.ptr)
        nativeHeap.free(lock)
    }
}

@InternalAPI
inline fun <R> ReadWriteLock.read(block: () -> R): R = readLock().use { block() }

@InternalAPI
inline fun <R> ReadWriteLock.write(block: () -> R): R = writeLock().use { block() }

private inline fun Int.checkResult(block: () -> String) {
    check(this == 0, block)
}
