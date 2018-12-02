package com.github.johnnysc.mytaskmanager.details.presentation.adapter;

import com.github.johnnysc.mytaskmanager.main.data.model.Task;

/**
 * Additional actions with task when in the list
 *
 * @author JohnnySC on 24.03.18.
 */
public interface TaskInteractListener {

    /**
     * Changing task's done property
     *
     * @param id   of the task {@link Task#id}
     * @param done property of the task  {@link Task#done}
     */
    void setTaskDone(long id, boolean done);

    /**
     * Go to view the task, find by id
     *
     * @param id property of the task {@link Task#id}
     */
    void viewTask(long id);
}