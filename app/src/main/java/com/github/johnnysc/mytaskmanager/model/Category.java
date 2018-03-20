package com.github.johnnysc.mytaskmanager.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author JohnnySC on 19.03.18.
 */

public class Category extends RealmObject {

    @PrimaryKey
    private int id;
    private String name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(RealmList<Task> tasks) {
        this.tasks = tasks;
    }
}
