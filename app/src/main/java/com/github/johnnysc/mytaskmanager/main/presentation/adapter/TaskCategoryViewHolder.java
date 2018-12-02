package com.github.johnnysc.mytaskmanager.main.presentation.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.johnnysc.mytaskmanager.R;
import com.github.johnnysc.mytaskmanager.main.data.model.TaskTypeDataModel;

/**
 * @author Asatryan on 02.12.18
 */
final class TaskCategoryViewHolder extends RecyclerView.ViewHolder {

    private final TextView mTaskCategoryTitleTextView;
    private final TextView mTaskCountTextView;
    private final Button mAddNewTaskButton;
    private final TaskCategoryClickListener mTaskCategoryClickListener;
    private final AddNewTaskClickListener mAddNewTaskClickListener;

    TaskCategoryViewHolder(@NonNull View itemView,
                           TaskCategoryClickListener taskCategoryClickListener,
                           AddNewTaskClickListener addNewTaskClickListener) {
        super(itemView);
        mTaskCategoryTitleTextView = itemView.findViewById(R.id.task_category_text_view);
        mTaskCountTextView = itemView.findViewById(R.id.task_count_text_view);
        mAddNewTaskButton = itemView.findViewById(R.id.add_new_task_button);
        mTaskCategoryClickListener = taskCategoryClickListener;
        mAddNewTaskClickListener = addNewTaskClickListener;
    }

    void bind(TaskTypeDataModel taskCategoryDataModel) {
        final int layoutPosition = getLayoutPosition();
        itemView.setOnClickListener(v -> mTaskCategoryClickListener.onTaskCategoryClick(layoutPosition));
        mTaskCategoryTitleTextView.setText(taskCategoryDataModel.getTitleResId());
        mTaskCountTextView.setText(String.valueOf(taskCategoryDataModel.getTaskCount()));
        mAddNewTaskButton.setOnClickListener(v -> mAddNewTaskClickListener.onAddNewTaskClick(layoutPosition));
    }
}