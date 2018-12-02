package com.github.johnnysc.mytaskmanager.main.domain;

/**
 * @author Asatryan on 02.12.18
 */
public interface DataCallbackSetter<T> {

    void setDataCallback(T dataCallback);
}