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

package com.nlab.practice.surfacetutorial.game

import androidx.annotation.ColorInt
import androidx.annotation.FloatRange

/**
 * @author Doohyun
 */
data class State(
    @ColorInt val pitchColor: Int,
    val pitchSize: Float,
    @FloatRange(from = 0.0, to = 1.0) val pitchYPercent: Float,
    val progressTimeInMillis: Long,
    val horizontalCalculateTimeInMillis: Long,
    @FloatRange(from = 0.3, to = 1.0) private val nodeSizePercentByPitchSize: Float,
    @ColorInt val nodeColor: Int,
    @ColorInt val activatedNodeColor: Int,
    val nodes: List<Node> = emptyList(),
    @ColorInt val answerColor: Int,
    val answers: List<Answer> = emptyList()
) {
    init {
        require(pitchYPercent in 0.0f .. 1.0f)
        require(nodeSizePercentByPitchSize in 0.3f .. 1.0f)
    }

    val nodeSize: Float = pitchSize * nodeSizePercentByPitchSize

    fun withAnswers(height: Int, range: Long): State {
        return if (hasAnswer(height)) {
            copy(answers = answers + listOf(
                Answer(progressTimeInMillis - range, progressTimeInMillis + range))
            )
        } else {
            this
        }
    }

    private fun hasAnswer(height: Int): Boolean {
        return nodes
            .find { node -> progressTimeInMillis in node.startTimeInMillis .. node.endTimeInMillis }
            ?.let { node ->
                node.heightPercent in computeAnswerRangePercent(height)
                    .let { range ->
                        maxOf(pitchYPercent - range, 0.0f) .. minOf(pitchYPercent + range, 1.0f)
                    }
            }
            ?: false
    }

    @FloatRange(from = 0.0, to = 1.0)
    private fun computeAnswerRangePercent(height: Int): Float {
        return ((pitchSize * (1 + nodeSizePercentByPitchSize)) / 2f) / height
    }

    class Node private constructor(
        val heightPercent: Float,
        val startTimeInMillis: Long,
        val endTimeInMillis: Long
    ) {
        override fun toString(): String {
            return "Node(heightPercent=$heightPercent, startTimeInMillis=$startTimeInMillis, endTimeInMillis=$endTimeInMillis)"
        }

        companion object {
            operator fun invoke(
                @FloatRange(from = 0.0, to = 1.0) heightPercent: Float,
                duration: Pair<Long, Long>
            ): Node = Node(
                heightPercent,
                startTimeInMillis = duration.first,
                endTimeInMillis = duration.second
            )
        }

    }

    data class Answer(
        val startTimeInMillis: Long,
        val endTimeInMillis: Long
    )
}