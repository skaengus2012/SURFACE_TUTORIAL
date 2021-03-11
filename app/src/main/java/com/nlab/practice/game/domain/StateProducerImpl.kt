package com.nlab.practice.game.domain

import com.nlab.practice.game.StateProducer
import com.nlab.practice.game.State
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