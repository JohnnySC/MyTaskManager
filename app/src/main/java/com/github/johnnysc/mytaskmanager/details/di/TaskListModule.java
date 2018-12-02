package com.github.johnnysc.mytaskmanager.details.di;

import com.github.johnnysc.mytaskmanager.details.domain.TaskListInteractor;
import com.github.johnnysc.mytaskmanager.details.domain.TaskListInteractorImpl;
import com.github.johnnysc.mytaskmanager.details.presentation.TaskListPresenter;
import com.github.johnnysc.mytaskmanager.details.presentation.TaskListPresenterImpl;
import com.github.johnnysc.mytaskmanager.main.domain.TasksRepository;

import dagger.Module;
import dagger.Provides;

/**
 * @author Asatryan on 02.12.18
 */
@TaskListScope
@Module
public class TaskListModule {

    @TaskListScope
    @Provides
    TaskListPresenter provideTaskListPresenter(TaskListInteractor interactor) {
        return new TaskListPresenterImpl(interactor);
    }

    @TaskListScope
    @Provides
    TaskListInteractor provideTaskListInteractor(TasksRepository repository) {
        return new TaskListInteractorImpl(repository);
    }
}