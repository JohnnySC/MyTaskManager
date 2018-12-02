package com.github.johnnysc.mytaskmanager.main.presentation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.johnnysc.mytaskmanager.details.presentation.TaskListActivity;
import com.github.johnnysc.mytaskmanager.MyTaskApplication;
import com.github.johnnysc.mytaskmanager.R;
import com.github.johnnysc.mytaskmanager.crud.CRUDModelImpl;
import com.github.johnnysc.mytaskmanager.crud.CRUDTaskActivity;
import com.github.johnnysc.mytaskmanager.main.data.model.TaskTypeDataModel;
import com.github.johnnysc.mytaskmanager.main.presentation.adapter.AddNewTaskClickListener;
import com.github.johnnysc.mytaskmanager.main.presentation.adapter.TaskCategoriesAdapter;
import com.github.johnnysc.mytaskmanager.main.presentation.adapter.TaskCategoryClickListener;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Asatryan on 02.12.18
 */
public class TasksMainActivity extends AppCompatActivity implements TasksMainView, TaskCategoryClickListener, AddNewTaskClickListener {

    @Inject
    TasksMainPresenter mTasksMainPresenter;

    private RecyclerView mTasksRecyclerView;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, TasksMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        MyTaskApplication.getInstance().getAppComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasks_main_activity);

        initUi();
        mTasksMainPresenter.getTasksData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTasksMainPresenter.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTasksMainPresenter.onPause();
    }

    @Override
    public void showTasksInfo(List<TaskTypeDataModel> taskCategoryDataModels) {
        mTasksRecyclerView.setAdapter(new TaskCategoriesAdapter(taskCategoryDataModels, this, this));
    }

    @Override
    public void onAddNewTaskClick(int categoryPosition) {
        mTasksMainPresenter.clear();
        startActivity(CRUDTaskActivity.newIntent(this, categoryPosition, CRUDModelImpl.CREATE));
    }

    @Override
    public void onTaskCategoryClick(int categoryPosition) {
        mTasksMainPresenter.clear();
        startActivity(TaskListActivity.newIntent(this, categoryPosition));
    }

    private void initUi() {
        mTasksRecyclerView = findViewById(R.id.tasks_recycler_view);
        mTasksRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mTasksRecyclerView.setHasFixedSize(true);
        mTasksRecyclerView.setNestedScrollingEnabled(false);
    }
}