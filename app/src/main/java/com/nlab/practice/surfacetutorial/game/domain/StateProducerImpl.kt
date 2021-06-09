/*
 * Copyright (C) 2018 The N's lab Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nlab.practice.surfacetutorial.game.domain

import com.nlab.practice.surfacetutorial.game.StateProducer
import com.nlab.practice.surfacetutorial.game.State
import java.util.concurrent.*

/**
 * @author Doohyun
 */
class StateProducerImpl(
    private val buffer: BlockingQueue<State>
) : StateProducer() {

    private var executorService: ScheduledExecutorService? = null
    private var beforeTask: Future<*>? = null

    override fun onCreated() {
        executorService = Executors.newSingleThreadScheduledExecutor()
    }

    override fun onDestroy() {
        executorService?.shutdownNow()
        executorService = null
        beforeTask = null
    }

    override fun updateTo(state: State) {
        stopBeforeJob()
        beforeTask = executorService?.submit { buffer.put(state) }
    }

    private fun stopBeforeJob() {
        beforeTask?.takeUnless { it.isDone }?.cancel(false)
        beforeTask = null
    }

}