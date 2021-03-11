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

package com.nlab.practice

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PixelFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceView
import android.widget.Button
import com.nlab.practice.game.*
import com.nlab.practice.game.domain.StateProducerImpl
import com.nlab.practice.game.domain.RendererImpl
import java.util.concurrent.*

class MainActivity : AppCompatActivity() {

    private lateinit var controller: Controller

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gameView = findViewById<SurfaceView>(R.id.game_view)
        val buffer = ArrayBlockingQueue<State>(1)

        controller = Controller(
            Presenter(
                StateProducerImpl(buffer),
                RendererImpl(buffer, gameView::getHolder),
                gameView::getHeight,
                createDefaultState(),
            ),
            uiThreadExecutor = { runnable -> runOnUiThread(runnable) },
            getDefaultState = this::createDefaultState,
            getNodes = MainActivity::createNodes
        )

        with(gameView) {
            setZOrderOnTop(true)
            setOnTouchListener { v, event ->
                val height = v.height
                if (event.y in 0.0f .. height.toFloat()) {
                    controller.inputPitchYPercent(event.y / height.toFloat())
                }
                true
            }

            holder.setFormat(PixelFormat.TRANSPARENT)
            holder.addCallback(LifecycleSurfaceHolderCallback(controller))
        }

        findViewById<Button>(R.id.btn_start).setOnClickListener {
            controller.startGame()
        }

        findViewById<Button>(R.id.btn_end).setOnClickListener {
            controller.endGame()
            controller.initialize()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        controller.endGame()
    }

    private fun createDefaultState(): State = State(
        pitchColor = Color.RED,
        pitchSize = resources.getDimension(R.dimen.size_pitch),
        pitchYPercent = 1.0f,
        progressTimeInMillis = 0L,
        horizontalCalculateTimeInMillis = 2000L,
        nodeSizePercentByPitchSize = 0.4f,
        nodeColor = Color.GRAY,
        activatedNodeColor = Color.GREEN,
        answerColor = Color.BLUE
    )

    companion object {

        private fun createNodes(): List<State.Node> = listOf(
            State.Node(0.3f, 4000L to 4070L),
            State.Node(0.35f, 4071L to 4100L),
            State.Node(0.7f, 4250L to 4290L),
            State.Node(0.8f, 5000L to 5260L),
            State.Node(1.0f, 5500L to 5560L),
            State.Node(0.7f, 5570L to 5900L),
            State.Node(0.4f, 6400L to 6500L),
            State.Node(0.5f, 7701L to 7900L),
            State.Node(0.1f, 8550L to 8900L),
            State.Node(0.2f, 9400L to 9500L),
            State.Node(0.5f, 10701L to 10900L),
            State.Node(0.6f, 11000L to 11070L),
            State.Node(0.75f, 12071L to 12100L),
            State.Node(0.8f, 13250L to 13290L),
            State.Node(0.9f, 14000L to 14260L),
            State.Node(1.0f, 15500L to 15560L),
            State.Node(0.6f, 15560L to 15900L),
            State.Node(0.6f, 16400L to 16500L),
            State.Node(0.4f, 17701L to 17900L),
        )

    }

}