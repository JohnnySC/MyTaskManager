package com.github.johnnysc.mytaskmanager.details.presentation.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.johnnysc.mytaskmanager.R;
import com.github.johnnysc.mytaskmanager.main.data.model.Task;

import java.util.List;

/**
 * To show tasks in list
 *
 * @author JohnnySC on 24.03.18.
 * @see Task
 */
public final class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {

    private final TaskInteractListener mTaskInteractListener;
    private List<Task> mTasks;
    private boolean mOnBind;

    public TaskAdapter(TaskInteractListener taskCheckListener,
                       List<Task> tasks) {
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
        mOnBind = true;
        holder.setCheckBoxChecked(task.isDone());
        mOnBind = false;
        if (task.isDone()) {
            holder.setDataStrikeThrough();
        }
        holder.setCheckBoxChangeListener((compoundButton, checked) -> {
            if (checked) {
                holder.setDataStrikeThrough();
            } else {
                holder.setDataCleared();
            }
            if (!mOnBind) {
                mTaskInteractListener.setTaskDone(task.getId(), checked);
            }
        });
        holder.setTitle(task.getTitle());
        holder.setBody(task.getBody());
        holder.setLayoutClickListener(view -> mTaskInteractListener.viewTask(task.getId()));
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }
}