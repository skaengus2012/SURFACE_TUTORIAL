package com.nlab.practice.game.domain

import android.graphics.*
import androidx.annotation.ColorInt
import com.nlab.practice.game.State

/**
 * @author Doohyun
 */
class Drawer {
    private val rectF = RectF()
    private val paint = Paint().apply {
        isAntiAlias = true
    }

    fun onDraw(canvas: Canvas, state: State) {
        clearCanvas(canvas)
        drawNodes(canvas, state)
        drawPitch(canvas, state)
    }

    private fun clearCanvas(canvas: Canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
    }

    private fun drawNodes(canvas: Canvas, state: State) {
        val halfHorizontalTime = state.horizontalCalculateTimeInMillis / 2f
        val drawNodeRange =
            ((state.progressTimeInMillis - halfHorizontalTime) .. (state.progressTimeInMillis + halfHorizontalTime))

        state.nodes
            .filter { node -> isDrawWithRange(node.startTimeInMillis, node.endTimeInMillis, drawNodeRange) }
            .forEach { node ->
                drawNode(canvas, state, node, drawNodeRange)
                drawAnswer(canvas, state, node, drawNodeRange)
            }
    }

    private fun drawNode(
        canvas: Canvas,
        state: State,
        node: State.Node,
        drawNodeRange: ClosedFloatingPointRange<Float>
    ) {
        drawNode(
            canvas,
            state,
            drawNodeRange,
            state.nodeColor,
            node.heightPercent,
            node.startTimeInMillis,
            node.endTimeInMillis
        )
    }

    private fun drawAnswer(
        canvas: Canvas,
        state: State,
        node: State.Node,
        drawNodeRange: ClosedFloatingPointRange<Float>
    ) {
        state.answers
            .filter { answer ->
                isDrawWithRange(
                    answer.startTimeInMillis,
                    answer.endTimeInMillis,
                    node.startTimeInMillis.toFloat() .. node.endTimeInMillis.toFloat()
                )
            }
            .fold(mutableListOf<State.Answer>()) { acc, answer ->
                val lastAnswer = acc.lastOrNull()
                when {
                    lastAnswer != null && isDrawWithRange(
                        answer.startTimeInMillis,
                        answer.endTimeInMillis,
                        lastAnswer.startTimeInMillis.toFloat() .. lastAnswer.endTimeInMillis.toFloat()
                    ) -> {
                        acc.removeLast()
                        acc += State.Answer(
                            lastAnswer.startTimeInMillis,
                            answer.endTimeInMillis
                        )
                    }

                    else -> {
                        acc += answer
                    }
                }

                acc
            }
            .forEach { answer ->
                drawNode(
                    canvas,
                    state,
                    drawNodeRange,
                    state.answerColor,
                    node.heightPercent,
                    maxOf(answer.startTimeInMillis, node.startTimeInMillis),
                    minOf(answer.endTimeInMillis, node.endTimeInMillis)
                )
            }
    }

    private fun drawNode(
        canvas: Canvas,
        state: State,
        drawNodeRange: ClosedFloatingPointRange<Float>,
        @ColorInt nodeColor: Int,
        heightPercent: Float,
        startTimeInMillis: Long,
        endTimeInMillis: Long
    ) {
        canvas.drawRect(
            rectF.apply {
                val y = (canvas.height - state.nodeSize) * heightPercent
                set(maxOf((startTimeInMillis - drawNodeRange.start) / state.horizontalCalculateTimeInMillis, 0f) * canvas.width,
                    y,
                    minOf((endTimeInMillis - drawNodeRange.start) / state.horizontalCalculateTimeInMillis, 1f) * canvas.width,
                    y + state.nodeSize
                )
            },
            paint.apply {
                style = Paint.Style.FILL
                color = nodeColor
            }
        )
    }

    private fun drawPitch(canvas: Canvas, state: State) {
        canvas.drawArc(
            rectF.apply {
                val halfPitchSize = state.pitchSize / 2
                val y = (canvas.height - state.pitchSize) * state.pitchYPercent

                set(
                    canvas.width / 2 - halfPitchSize,
                    y,
                    canvas.width / 2 + halfPitchSize,
                    y + state.pitchSize
                )
            },
            0f,
            360f,
            false,
            paint.apply {
                style = Paint.Style.FILL
                color = state.pitchColor
            }
        )
    }

    companion object {

        fun isDrawWithRange(
            startTime: Long,
            endTime: Long,
            drawNodeRange: ClosedFloatingPointRange<Float>
        ): Boolean {
            return startTime.toFloat() in drawNodeRange
                    || endTime.toFloat() in drawNodeRange
                    || ( startTime <= drawNodeRange.start && endTime >= drawNodeRange.endInclusive)
        }

    }

}