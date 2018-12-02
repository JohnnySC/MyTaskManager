package com.github.johnnysc.mytaskmanager.main.data.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author JohnnySC on 19.03.18.
 */

public class Category extends RealmObject {

    @PrimaryKey
    private int id;
    private RealmList<Task> tasks;

    public Category() {
        //need an empty constructor
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RealmList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(RealmList<Task> tasks) {
        this.tasks = tasks;
    }
}
