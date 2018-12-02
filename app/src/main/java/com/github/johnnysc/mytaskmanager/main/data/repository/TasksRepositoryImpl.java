package com.github.johnnysc.mytaskmanager.main.data.repository;

import android.support.annotation.Nullable;

import com.github.johnnysc.mytaskmanager.R;
import com.github.johnnysc.mytaskmanager.main.data.model.Category;
import com.github.johnnysc.mytaskmanager.main.data.model.CategoryType;
import com.github.johnnysc.mytaskmanager.main.data.model.Task;
import com.github.johnnysc.mytaskmanager.main.data.model.TaskTypeDataModel;
import com.github.johnnysc.mytaskmanager.main.domain.TasksRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

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
    public List<TaskTypeDataModel> getTasksCategoryDataModels() {
        List<TaskTypeDataModel> dataModelList = new ArrayList<>();

        for (int i = 0; i < CATEGORY_TYPES_IDS.size(); i++) {
            Category category = getCategoryByPrimaryKey(i);
            int tasksCount;
            if (category == null) {
                tasksCount = 0;
                initCategory(i);
            } else {
                tasksCount = getNotDoneTasksCount(category.getTasks());
            }
            dataModelList.add(new TaskTypeDataModel(i, CATEGORY_TYPES_IDS.get(i), tasksCount));
        }

        return dataModelList;
    }

    @Override
    public List<Task> getTasksSorted(@CategoryType.TaskType int taskType) {
        sortTaskList(taskType);
        Category category = getCategoryByPrimaryKey(taskType);
        return category.getTasks();
    }

    @Override
    public void moveTask(int taskType, int oldPosition, int newPosition) {
        mRealm.executeTransaction(realm -> getCategoryByPrimaryKey(taskType).getTasks().move(oldPosition, newPosition));
    }

    @Override
    public Task getTask(int taskType, int position) {
        return getCategoryByPrimaryKey(taskType).getTasks().get(position);
    }

    @Override
    public void setTaskDone(long id, boolean done) {
        mRealm.executeTransaction(realm -> getTaskByPrimaryKey(id).setDone(done));
    }

    @Override
    public void sortTaskList(@CategoryType.TaskType int taskType) {
        RealmList<Task> list = getCategoryByPrimaryKey(taskType).getTasks();
        mRealm.executeTransaction(realm -> Collections.sort(list, (task, that) -> {
            int taskInt = task.isDone() ? 2 : 1;
            int thatInt = that.isDone() ? 2 : 1;
            return taskInt - thatInt;
        }));
    }

    @Override
    public void removeTask(long id) {
        mRealm.executeTransaction(realm -> {
            RealmResults<Task> task = realm.where(Task.class).equalTo("id", id).findAll();
            if (task != null) {
                task.clear();
            }
        });
    }

    @Override
    public int getCategoryTitle(@CategoryType.TaskType int taskType) {
        return CATEGORY_TYPES_IDS.get(taskType);
    }

    //region private methods

    @Nullable
    private Category getCategoryByPrimaryKey(int id) {
        return mRealm.where(Category.class).equalTo("id", id).findFirst();
    }

    private void initCategory(Integer id) {
        mRealm.executeTransaction(realm -> {
            Category category = new Category();
            category.setId(id);
            realm.copyToRealmOrUpdate(category);
        });
    }

    private Task getTaskByPrimaryKey(long id) {
        return mRealm.where(Task.class).equalTo("id", id).findFirst();
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

    //endregion
}