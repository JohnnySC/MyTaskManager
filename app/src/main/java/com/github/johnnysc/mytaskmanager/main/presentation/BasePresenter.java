package com.github.johnnysc.mytaskmanager.main.presentation;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Asatryan on 02.12.18
 */
public abstract class BasePresenter<V> implements LifeCycleEvents<V> {

    private V mView;
    private List<Runnable> mViewCommandList;

    public BasePresenter() {
        mViewCommandList = new CopyOnWriteArrayList<>();
    }

    @Override
    public void onResume(V view) {
        mView = view;
        runAllViewCommands();
    }

    @Override
    public void onPause() {
        mView = null;
        mViewCommandList.clear();
        mViewCommandList = new CopyOnWriteArrayList<>();
    }

    @Override
    public void clear() {
        onPause();
    }

    protected final V getView() {
        return mView;
    }

    protected final void doSafely(Runnable... commands) {
        if (mView == null) {
            mViewCommandList.addAll(Arrays.asList(commands));
        } else {
            runAllViewCommands();
        }
    }

    private void runAllViewCommands() {
        for (Runnable command : mViewCommandList) {
            if (mView != null) {
                command.run();
            }
        }
    }
}