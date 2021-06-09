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

import android.view.SurfaceHolder
import com.nlab.practice.surfacetutorial.game.Renderer
import com.nlab.practice.surfacetutorial.game.State
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * @author Doohyun
 */
class RendererImpl(
    private val buffer: BlockingQueue<State>,
    private val getSurfaceHolder: () -> SurfaceHolder
) : Renderer() {
    private val drawer = Drawer()
    private var executorService: ExecutorService? = null

    override fun onCreated() {
        executorService = Executors.newSingleThreadScheduledExecutor().apply {
            scheduleWithFixedDelay(
                {
                    val state = buffer.take()
                    getSurfaceHolder().run { unlockCanvasAndPost(lockCanvas().also { drawer.onDraw(it, state) }) }
                },
                0,
                FRAME_DELAY_TIME,
                TimeUnit.MILLISECONDS
            )
        }
    }

    override fun onDestroyed() {
        executorService?.shutdownNow()
        executorService = null
    }

    companion object {
        private const val FRAME_DELAY_TIME = 10L
    }

}