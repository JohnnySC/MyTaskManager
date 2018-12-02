package com.github.johnnysc.mytaskmanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.github.johnnysc.mytaskmanager.main.data.model.Category;
import com.github.johnnysc.mytaskmanager.main.data.model.CategoryType;
import com.github.johnnysc.mytaskmanager.main.data.model.Task;

import java.util.Arrays;
import java.util.List;

import io.realm.Realm;

/**
 * @author JohnnySC on 24.03.18.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected static final String EXTRA_TASK_TYPE = "extra_task_type";
    protected static final List<Integer> CATEGORY_STRINGS = Arrays.asList(
            R.string.first_category,
            R.string.second_category,
            R.string.third_category,
            R.string.forth_category
    );

    protected Realm mRealm;
    @CategoryType.TaskType
    protected int mTaskType;
    protected Toolbar mToolbar;
    protected FloatingActionButton mActionButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();
        initExtraData();
    }

    protected void initExtraData() {
        mTaskType = getIntent().getIntExtra(EXTRA_TASK_TYPE, 0);
    }

    protected void initToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void initFab() {
        mActionButton = findViewById(R.id.action_fab);
    }

    protected final Category getCategoryByPrimaryKey(int id) {
        return mRealm.where(Category.class).equalTo("id", id).findFirst();
    }

    protected final Task getTaskByPrimaryKey(long id) {
        return mRealm.where(Task.class).equalTo("id", id).findFirst();
    }
}