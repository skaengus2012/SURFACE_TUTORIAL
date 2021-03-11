package com.nlab.practice.game

import android.view.SurfaceHolder

/**
 * @author Doohyun
 */
class LifecycleSurfaceHolderCallback(
    private val controller: Controller
) : SurfaceHolder.Callback {

    override fun surfaceCreated(holder: SurfaceHolder) {
        controller.onCreateView()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        controller.onDestroyView()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) = Unit
}