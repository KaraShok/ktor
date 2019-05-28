/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.util

import kotlin.native.ThreadLocal
import kotlin.native.concurrent.*

internal class ThreadId {
    init {
        freeze()
    }

    companion object {
        val current: ThreadId get() = currentThreadId
    }
}

@ThreadLocal
private val currentThreadId = ThreadId()
