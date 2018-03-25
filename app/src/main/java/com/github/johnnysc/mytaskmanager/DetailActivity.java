package com.github.johnnysc.mytaskmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;

import com.github.johnnysc.mytaskmanager.adapter.TaskAdapter;
import com.github.johnnysc.mytaskmanager.adapter.TaskInteractListener;
import com.github.johnnysc.mytaskmanager.model.CategoryType;
import com.github.johnnysc.mytaskmanager.model.Task;

import java.util.Collections;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Here is the list of all the task of chosen category
 * it is possible to make the task done immediately from list
 * view details and add a new one
 */
public class DetailActivity extends BaseActivity implements TaskInteractListener {

    private static final int REQUEST_CODE = 2;
    private TaskAdapter mAdapter;
    private boolean mDataChanged;

    public static Intent newIntent(Context context, @CategoryType.TaskType int taskType) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_TASK_TYPE, taskType);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initAdapter();
        initUi();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE == requestCode && resultCode == RESULT_OK) {
            updateData();
        }
    }

    @Override
    public void onBackPressed() {
        if (mDataChanged) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
        }
        super.onBackPressed();
    }

    @Override
    public void setTaskDone(long id, boolean done) {
        mRealm.executeTransaction(realm -> getTaskByPrimaryKey(id).setDone(done));
        updateData();
    }

    @Override
    public void viewTask(long id) {
        startActivityForResult(CRUDTaskActivity.newIntent(this, mTaskType, CRUDTaskActivity.READ, id), REQUEST_CODE);
    }

    @Override
    protected void initFab() {
        super.initFab();
        mActionButton.setImageResource(android.R.drawable.ic_input_add);
        mActionButton.setOnClickListener(view ->
                startActivityForResult(CRUDTaskActivity.newIntent(this, mTaskType, CRUDTaskActivity.CREATE), REQUEST_CODE));
    }

    private void updateData() {
        sortData();
        mAdapter.notifyDataSetChanged();
        mDataChanged = true;
    }

    private void initAdapter() {
        mAdapter = new TaskAdapter(this, getCategoryByPrimaryKey(mTaskType).getTasks());
    }

    private void sortData() {
        RealmList<Task> list = getCategoryByPrimaryKey(mTaskType).getTasks();
        mRealm.executeTransaction(realm -> Collections.sort(list, (task, that) -> {
            int taskInt = task.isDone() ? 2 : 1;
            int thatInt = that.isDone() ? 2 : 1;
            return taskInt - thatInt;
        }));
    }

    private void initUi() {
        initTitle();
        initToolbar();
        initDataList();
        initFab();
    }

    private void initTitle() {
        setTitle(CATEGORY_STRINGS.get(mTaskType));
    }

    private void initDataList() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(mAdapter);
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int old = viewHolder.getAdapterPosition();
                int tgt = target.getAdapterPosition();
                mRealm.executeTransaction(realm -> getCategoryByPrimaryKey(mTaskType).getTasks().move(old, tgt));
                mAdapter.notifyItemMoved(old, tgt);
                return true;
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                return makeMovementFlags(dragFlags, ItemTouchHelper.LEFT);
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction != ItemTouchHelper.LEFT) {
                    return;
                }
                boolean needUpdate;
                long id = getCategoryByPrimaryKey(mTaskType).getTasks().get(viewHolder.getLayoutPosition()).getId();
                needUpdate = getTaskByPrimaryKey(id).isDone();
                if (!needUpdate) {
                    mAdapter.notifyDataSetChanged();
                    return;
                }
                mRealm.executeTransaction(realm -> {
                    RealmResults<Task> rows = realm.where(Task.class).equalTo("id", id).findAll();
                    rows.clear();
                });
                mAdapter.notifyDataSetChanged();
                mDataChanged = true;
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }
}