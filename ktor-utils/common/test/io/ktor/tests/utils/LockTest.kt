/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.tests.utils

import io.ktor.util.*
import kotlinx.io.core.*
import kotlin.test.*

class LockTest {
    @Test
    fun testLockUnlock() {
        Lock().use { lock ->
            lock.lock()
            lock.unlock()
        }
    }

    @Test
    fun testReadWriteLockUnlock() {
        ReadWriteLock().use { lock ->
            lock.readLock().close()
            lock.writeLock().close()
        }

    }

    @Test
    fun testReentrantLock() {
        Lock().use { lock ->
            lock.lock()
            lock.lock()

            lock.unlock()
            lock.unlock()
        }
    }

    @Test
    fun testReadWriteReentrantLock() {
        ReadWriteLock().use {  lock ->
            lock.readLock().use {
                lock.readLock().use {

                }
            }

            lock.writeLock().use {
                lock.writeLock().use {
                }
            }
        }
    }
}
