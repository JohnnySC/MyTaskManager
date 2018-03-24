package com.github.johnnysc.mytaskmanager;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntDef;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.johnnysc.mytaskmanager.model.CategoryType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class CRUDTaskActivity extends BaseActivity {

    public static final int CREATE = 0;
    public static final int READ = 1;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CREATE, READ})
    public @interface TaskActionType {
    }

    private static final String EXTRA_TASK_TYPE = "extra_task_type";
    private static final String EXTRA_ACTION_TYPE = "extra_action_type";

    public static Intent newIntent(Context context,
                                   @CategoryType.TaskType int taskType,
                                   @TaskActionType int actionType){
        Intent intent = new Intent(context, CRUDTaskActivity.class);
        intent.putExtra(EXTRA_TASK_TYPE, taskType);
        intent.putExtra(EXTRA_ACTION_TYPE, actionType);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        int taskType = getIntent().getIntExtra(EXTRA_TASK_TYPE, 0);


    }
}
