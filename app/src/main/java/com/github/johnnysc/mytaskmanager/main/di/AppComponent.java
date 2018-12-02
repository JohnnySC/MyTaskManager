package com.github.johnnysc.mytaskmanager.main.di;

import com.github.johnnysc.mytaskmanager.main.presentation.TasksMainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Asatryan on 02.12.18
 */
@Singleton
@Component(modules = {AppModule.class, MainModule.class})
public interface AppComponent {

    void inject(TasksMainActivity activity);
}