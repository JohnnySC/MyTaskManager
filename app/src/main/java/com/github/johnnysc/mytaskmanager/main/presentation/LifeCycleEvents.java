package com.github.johnnysc.mytaskmanager.main.presentation;

/**
 * @author Asatryan on 02.12.18
 */
public interface LifeCycleEvents<V> {

    void onResume(V view);

    void onPause();

    void clear();
}