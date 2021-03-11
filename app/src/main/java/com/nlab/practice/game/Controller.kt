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

package com.nlab.practice.game

import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * @author Doohyun
 */
class Controller(
    private val presenter: Presenter,
    private val uiThreadExecutor: (() -> (Unit)) -> Unit,
    private val getDefaultState: () -> State,
    private val getNodes: () -> List<State.Node>
) {
    private var executorService: ExecutorService? = null

    fun initialize() {
        presenter.setState(getDefaultState())
    }

    fun startGame() {
        val startTime = Calendar.getInstance().timeInMillis
        presenter.setNodes(getNodes())

        executorService = Executors.newSingleThreadScheduledExecutor().also { scheduler ->
            scheduler.scheduleWithFixedDelay(
                {
                    val currentTime = Calendar.getInstance().timeInMillis - startTime
                    uiThreadExecutor {
                        presenter.setProgressTimeInMillis(currentTime, 10)
                    }
                },
                0,
                10,
                TimeUnit.MILLISECONDS
            )
        }
    }

    fun endGame() {
        executorService?.shutdownNow()
        executorService = null
    }

    fun inputPitchYPercent(percent: Float) {
        presenter.setPitchYPercent(percent)
    }

    fun onCreateView() {
        presenter.onCreated()
    }

    fun onDestroyView() {
        presenter.onDestroyed()
    }
}