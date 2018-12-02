package com.github.johnnysc.mytaskmanager.details.presentation;

import android.support.annotation.StringRes;

import com.github.johnnysc.mytaskmanager.main.data.model.Task;

import java.util.List;

/**
 * @author Asatryan on 02.12.18
 */
public interface TaskListView {

    void showTitle(@StringRes int titleResId);

    void showTasks(List<Task> taskList);

    void notifyDataSetChanged();

    void notifyItemRemoved(int position);

    void notifyItemMoved(int oldPosition, int newPosition);
}