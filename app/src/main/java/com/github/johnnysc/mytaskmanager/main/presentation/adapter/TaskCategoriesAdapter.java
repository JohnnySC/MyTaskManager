package com.github.johnnysc.mytaskmanager.main.presentation.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.johnnysc.mytaskmanager.R;
import com.github.johnnysc.mytaskmanager.main.data.model.TaskTypeDataModel;

import java.util.List;

/**
 * @author Asatryan on 02.12.18
 */
public final class TaskCategoriesAdapter extends RecyclerView.Adapter<TaskCategoryViewHolder> {

    private final List<TaskTypeDataModel> mTaskCategoryDataModels;
    private final TaskCategoryClickListener mTaskCategoryClickListener;
    private final AddNewTaskClickListener mAddNewTaskClickListener;

    public TaskCategoriesAdapter(List<TaskTypeDataModel> taskCategoryDataModels,
                                 TaskCategoryClickListener taskCategoryClickListener,
                                 AddNewTaskClickListener addNewTaskClickListener) {
        mTaskCategoryDataModels = taskCategoryDataModels;
        mTaskCategoryClickListener = taskCategoryClickListener;
        mAddNewTaskClickListener = addNewTaskClickListener;
    }

    @NonNull
    @Override
    public TaskCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_category_item, viewGroup, false);
        int height = viewGroup.getMeasuredHeight() / 2;
        int width = viewGroup.getMeasuredWidth() / 2;
        view.setLayoutParams(new RecyclerView.LayoutParams(width, height));
        return new TaskCategoryViewHolder(view, mTaskCategoryClickListener, mAddNewTaskClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskCategoryViewHolder taskCategoryViewHolder, int position) {
        taskCategoryViewHolder.bind(mTaskCategoryDataModels.get(position));
    }

    @Override
    public int getItemCount() {
        return mTaskCategoryDataModels.size();
    }
}