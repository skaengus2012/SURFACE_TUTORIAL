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

package com.nlab.practice.game.domain

import android.graphics.Color
import com.nlab.practice.game.State
import org.junit.Assert.*
import org.junit.Test

/**
 * @author Doohyun
 */
class StateTest {

    @Test
    fun testWithAnswers() {
        assertTrue(State(
            pitchColor = Color.RED,
            pitchSize = 10f,
            pitchYPercent = 0.34f,
            progressTimeInMillis = 4050L,
            horizontalCalculateTimeInMillis = 2000L,
            nodeSizePercentByPitchSize = 0.4f,
            nodeColor = Color.GRAY,
            activatedNodeColor = Color.GREEN,
            nodes = listOf(
                State.Node(0.3f, 4000L to 4070L)
            ),
            answerColor = Color.BLUE
        ).withAnswers(100, 10).answers.isNotEmpty())
    }

}