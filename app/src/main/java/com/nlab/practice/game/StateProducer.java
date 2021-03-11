package com.nlab.practice.game;

import androidx.annotation.NonNull;

/**
 * @author Doohyun
 */
public abstract class StateProducer {
    protected abstract void onCreated();
    protected abstract void onDestroy();
    protected abstract void updateTo(@NonNull State state);
}
