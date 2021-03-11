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