package com.github.johnnysc.mytaskmanager.model;

import io.realm.RealmObject;

/**
 * @author JohnnySC on 19.03.18.
 */

public class Task extends RealmObject {

    private String id;
    private String title;
    private String body;
    private boolean done;

    public Task() {
        //need an empty constructor
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
}
