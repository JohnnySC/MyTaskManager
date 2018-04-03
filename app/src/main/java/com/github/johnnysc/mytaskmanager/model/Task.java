package com.github.johnnysc.mytaskmanager.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author JohnnySC on 19.03.18.
 */

public class Task extends RealmObject {

    @PrimaryKey
    private long id;
    private String title;
    private String body;
    private boolean done;
    private long deadline;
    private boolean notify;

    public Task() {
        //need an empty constructor
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }
}
