package com.nlab.practice.game.domain

import android.view.SurfaceHolder
import com.nlab.practice.game.Renderer
import com.nlab.practice.game.State
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