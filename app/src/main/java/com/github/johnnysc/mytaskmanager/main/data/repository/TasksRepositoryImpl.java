package com.github.johnnysc.mytaskmanager.main.data.repository;

import android.support.annotation.Nullable;

import com.github.johnnysc.mytaskmanager.R;
import com.github.johnnysc.mytaskmanager.main.data.model.Category;
import com.github.johnnysc.mytaskmanager.main.data.model.Task;
import com.github.johnnysc.mytaskmanager.main.data.model.TaskCategoryDataModel;
import com.github.johnnysc.mytaskmanager.main.domain.TasksRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

import static java.util.Arrays.asList;

/**
 * @author Asatryan on 02.12.18
 */
public final class TasksRepositoryImpl implements TasksRepository {

    private static final List<Integer> CATEGORY_TYPES_IDS = Collections.unmodifiableList(asList(
            R.string.first_category,
            R.string.second_category,
            R.string.third_category,
            R.string.forth_category
    ));

    private final Realm mRealm;

    public TasksRepositoryImpl(Realm realm) {
        mRealm = realm;
    }

    @Override
    public List<TaskCategoryDataModel> getTasksCategoryDataModels() {
        List<TaskCategoryDataModel> dataModelList = new ArrayList<>();

        for (int i = 0; i < CATEGORY_TYPES_IDS.size(); i++) {
            Category category = getCategoryByPrimaryKey(i);
            int tasksCount;
            if (category == null) {
                tasksCount = 0;
                initCategory(i);
            } else {
                tasksCount = getNotDoneTasksCount(category.getTasks());
            }
            dataModelList.add(new TaskCategoryDataModel(i, CATEGORY_TYPES_IDS.get(i), tasksCount));
        }

        return dataModelList;
    }

    @Nullable
    private Category getCategoryByPrimaryKey(int id) {
        return mRealm.where(Category.class).equalTo("id", id).findFirst();
    }

    private void initCategory(Integer id) {
        mRealm.executeTransaction(realm -> realm.createObject(Category.class, id));
    }

    private int getNotDoneTasksCount(RealmList<Task> tasks) {
        int count = 0;
        for (int i = 0; i < tasks.size(); i++) {
            if (!tasks.get(i).isDone()) {
                count++;
            }
        }
        return count;
    }
}