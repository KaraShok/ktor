/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

@file:Suppress("KDocMissingDocumentation")

package io.ktor.util

import kotlinx.io.core.*
import java.util.concurrent.locks.*
import java.util.concurrent.locks.Lock

@InternalAPI
actual class Lock : Closeable {
    private val lock = ReentrantLock()

    actual fun lock() {
        lock.lock()
    }

    actual fun unlock() {
        lock.unlock()
    }

    override fun close() {
    }
}

@InternalAPI
actual class ReadWriteLock : Closeable {

    private val lock = ReentrantReadWriteLock()

    actual fun readLock(): LockTicket = object : LockTicket {
        val current = lock.readLock().apply {
            lock()

        }
        override fun close() {
            current.unlock()
        }
    }

    actual fun writeLock(): LockTicket = object : LockTicket {
        val current = lock.writeLock().apply {
            lock()
        }

        override fun close() {
            current.unlock()
        }
    }

    override fun close() {}
}


