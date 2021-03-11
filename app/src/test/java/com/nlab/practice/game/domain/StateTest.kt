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