package com.github.johnnysc.mytaskmanager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.johnnysc.mytaskmanager.model.Category;
import com.github.johnnysc.mytaskmanager.model.Task;

import java.util.Arrays;
import java.util.List;

import io.realm.Realm;

/**
 * @author JohnnySC on 24.03.18.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected static final List<Integer> CATEGORY_STRINGS = Arrays.asList(
            R.string.first_category,
            R.string.second_category,
            R.string.third_category,
            R.string.forth_category
    );

    protected Realm mRealm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();
    }

    protected final Category getCategoryByPrimaryKey(int id) {
        return mRealm.where(Category.class).equalTo("id", id).findFirst();
    }

    protected final Task getTaskByPrimaryKey(int id) {
        return mRealm.where(Task.class).equalTo("id", id).findFirst();
    }
}