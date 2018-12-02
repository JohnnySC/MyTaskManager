package com.github.johnnysc.mytaskmanager.details.di;

import com.github.johnnysc.mytaskmanager.details.presentation.TaskListActivity;

import dagger.Subcomponent;

/**
 * @author Asatryan on 02.12.18
 */
@TaskListScope
@Subcomponent(modules = TaskListModule.class)
public interface TaskListComponent {

    void inject(TaskListActivity activity);
}