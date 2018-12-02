package com.github.johnnysc.mytaskmanager.main.di;

import com.github.johnnysc.mytaskmanager.main.data.repository.TasksRepositoryImpl;
import com.github.johnnysc.mytaskmanager.main.domain.TasksMainInteractor;
import com.github.johnnysc.mytaskmanager.main.domain.TasksMainInteractorImpl;
import com.github.johnnysc.mytaskmanager.main.domain.TasksRepository;
import com.github.johnnysc.mytaskmanager.main.presentation.TasksMainPresenter;
import com.github.johnnysc.mytaskmanager.main.presentation.TasksMainPresenterImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

/**
 * @author Asatryan on 02.12.18
 */
@Module
public class MainModule {

    @Provides
    @Singleton
    TasksMainPresenter provideTasksMainPresenter(TasksMainInteractor tasksMainInteractor) {
        return new TasksMainPresenterImpl(tasksMainInteractor);
    }

    @Provides
    @Singleton
    TasksMainInteractor provideTasksMainInteractor(TasksRepository tasksRepository) {
        return new TasksMainInteractorImpl(tasksRepository);
    }

    @Provides
    @Singleton
    TasksRepository provideTasksRepository(Realm realm) {
        return new TasksRepositoryImpl(realm);
    }

    @Provides
    @Singleton
    Realm provideRealm() {
        return Realm.getDefaultInstance();
    }
}