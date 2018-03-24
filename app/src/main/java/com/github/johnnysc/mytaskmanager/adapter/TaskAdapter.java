package com.github.johnnysc.mytaskmanager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.johnnysc.mytaskmanager.R;
import com.github.johnnysc.mytaskmanager.model.Task;

import io.realm.RealmList;

/**
 * To show tasks in list
 *
 * @author JohnnySC on 24.03.18.
 * @see Task
 */
public final class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {

    private final TaskInteractListener mTaskInteractListener;
    private RealmList<Task> mTasks;

    public TaskAdapter(TaskInteractListener taskCheckListener,
                       RealmList<Task> tasks) {
        mTaskInteractListener = taskCheckListener;
        mTasks = tasks;
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, null);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = mTasks.get(position);
        holder.setCheckBoxChecked(task.isDone());
        if (task.isDone()) {
            holder.setDataStrikeThrough();
        }
        holder.setCheckBoxChangeListener((compoundButton, checked) -> {
            if (checked) {
                holder.setDataStrikeThrough();
            } else {
                holder.setDataCleared();
            }
            mTaskInteractListener.setTaskDone(task.getId(), checked);
        });
        holder.setTitle(task.getTitle());
        holder.setBody(task.getBody());
        holder.setLayoutClickListener(view -> {
            mTaskInteractListener.viewTask(task.getId());
        });
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }
}