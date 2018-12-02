package com.github.johnnysc.mytaskmanager.details.presentation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;

import com.github.johnnysc.mytaskmanager.MyTaskApplication;
import com.github.johnnysc.mytaskmanager.R;
import com.github.johnnysc.mytaskmanager.details.presentation.adapter.CustomTouchHelperCallback;
import com.github.johnnysc.mytaskmanager.details.presentation.adapter.TaskAdapter;
import com.github.johnnysc.mytaskmanager.details.presentation.adapter.TaskInteractListener;
import com.github.johnnysc.mytaskmanager.main.data.model.CategoryType;
import com.github.johnnysc.mytaskmanager.main.data.model.Task;
import com.github.johnnysc.mytaskmanager.crud.CRUDModelImpl;
import com.github.johnnysc.mytaskmanager.crud.CRUDTaskActivity;
import com.github.johnnysc.mytaskmanager.main.presentation.TasksMainActivity;

import java.util.List;

import javax.inject.Inject;

/**
 * Here is the list of all the task of chosen category
 * it is possible to make the task done immediately from list
 * view details and add a new one
 *
 * @author Asatryan on 02.12.18
 */
public class TaskListActivity extends AppCompatActivity implements TaskListView,
        TaskInteractListener, CustomTouchHelperCallback.TouchHelperInteractionListener {

    private static final String EXTRA_TASK_TYPE = "extra_task_type";
    private static final int REQUEST_CODE = 2;

    @Inject
    TaskListPresenter mTaskListPresenter;

    @CategoryType.TaskType
    private int mTaskType;

    private RecyclerView mTaskListRecyclerView;
    private TaskAdapter mTaskAdapter;

    public static Intent newIntent(Context context, @CategoryType.TaskType int taskType) {
        Intent intent = new Intent(context, TaskListActivity.class);
        intent.putExtra(EXTRA_TASK_TYPE, taskType);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyTaskApplication.getInstance().getTaskListComponent().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getExtraData();
        initUi();
        mTaskListPresenter.getTaskList(mTaskType);
        mTaskListPresenter.getCategoryType(mTaskType);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTaskListPresenter.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTaskListPresenter.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE == requestCode && resultCode == RESULT_OK) {
            mTaskListPresenter.getTaskList(mTaskType);
        }
    }

    @Override
    public void onBackPressed() {
        mTaskListPresenter.clear();
        MyTaskApplication.getInstance().clearTaskListComponent();
        finish();
        startActivity(TasksMainActivity.newIntent(this));
    }

    @Override
    public void setTaskDone(long id, boolean done) {
        mTaskListPresenter.setTaskDone(mTaskType, id, done);
    }

    @Override
    public void viewTask(long id) {
        startActivityForResult(CRUDTaskActivity.newIntent(this, mTaskType, CRUDModelImpl.READ, id), REQUEST_CODE);
    }

    @Override
    public void onMove(int oldPosition, int newPosition) {
        mTaskListPresenter.moveTask(mTaskType, oldPosition, newPosition);
    }

    @Override
    public void onSwiped(int position) {
        mTaskListPresenter.removeTask(mTaskType, position);
    }

    @Override
    public void showTitle(int titleResId) {
        setTitle(titleResId);
    }

    @Override
    public void showTasks(List<Task> taskList) {
        mTaskAdapter = new TaskAdapter(this, taskList);
        mTaskListRecyclerView.setAdapter(mTaskAdapter);
    }

    @Override
    public void notifyDataSetChanged() {
        mTaskAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyItemRemoved(int position) {
        mTaskAdapter.notifyItemRemoved(position);
    }

    @Override
    public void notifyItemMoved(int oldPosition, int newPosition) {
        mTaskAdapter.notifyItemMoved(oldPosition, newPosition);
    }

    //region private methods

    private void getExtraData() {
        mTaskType = getIntent().getIntExtra(EXTRA_TASK_TYPE, 0);
    }

    private void initUi() {
        initToolbar();
        initRecyclerView();
        initAddNewTaskButton();
    }

    protected void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initRecyclerView() {
        mTaskListRecyclerView = findViewById(R.id.recycler_view);
        mTaskListRecyclerView.setHasFixedSize(false);
        CustomTouchHelperCallback itemTouchCallback = new CustomTouchHelperCallback(0, ItemTouchHelper.LEFT, this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mTaskListRecyclerView);
    }

    private void initAddNewTaskButton() {
        FloatingActionButton addTaskButton = findViewById(R.id.add_new_task_floating_action_button);
        addTaskButton.setOnClickListener(view ->
                startActivityForResult(CRUDTaskActivity.newIntent(this, mTaskType, CRUDModelImpl.CREATE), REQUEST_CODE));
    }

    //endregion
}