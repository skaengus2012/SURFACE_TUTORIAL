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

import androidx.annotation.FloatRange

/**
 * @author Doohyun
 */
class Presenter(
    private val stateProducer: StateProducer,
    private val renderer: Renderer,
    private val getViewHeight: () -> Int,
    private var latestState: State
) {

    fun onCreated() {
        stateProducer.onCreated()
        stateProducer.updateTo(latestState)

        renderer.onCreated()
    }

    fun onDestroyed() {
        stateProducer.onDestroy()
        renderer.onDestroyed()
    }

    fun setPitchYPercent(@FloatRange(from = 0.0, to = 1.0) percent: Float) {
        setState(latestState.copy(pitchYPercent = percent))
    }

    fun setProgressTimeInMillis(time: Long, range: Long) {
        setState(latestState
            .copy(progressTimeInMillis = time)
            .withAnswers(getViewHeight(), range)
        )
    }

    fun setNodes(nodes: List<State.Node>) {
        setState(latestState.copy(nodes = nodes))
    }

    fun setState(newState: State) {
        latestState = newState
        stateProducer.updateTo(latestState)
    }

}