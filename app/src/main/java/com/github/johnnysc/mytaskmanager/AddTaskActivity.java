package com.github.johnnysc.mytaskmanager;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.johnnysc.mytaskmanager.model.CategoryType;

public class AddTaskActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_TYPE = "extra_task_type";

    public static Intent newIntent(Context context, @CategoryType.TaskType int taskType){
        Intent intent = new Intent(context, AddTaskActivity.class);
        intent.putExtra(EXTRA_TASK_TYPE, taskType);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        int taskType = getIntent().getIntExtra(EXTRA_TASK_TYPE, 0);


    }
}
