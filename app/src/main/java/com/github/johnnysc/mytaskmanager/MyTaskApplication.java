package com.github.johnnysc.mytaskmanager;

import android.app.Application;

import com.github.johnnysc.mytaskmanager.details.di.TaskListComponent;
import com.github.johnnysc.mytaskmanager.main.di.AppComponent;
import com.github.johnnysc.mytaskmanager.main.di.AppModule;
import com.github.johnnysc.mytaskmanager.main.di.DaggerAppComponent;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * @author JohnnySC on 19.03.18.
 */

public class MyTaskApplication extends Application {

    private static MyTaskApplication sInstance;
    private AppComponent mAppComponent;
    private TaskListComponent mTaskListComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        setUpDb();
    }

    public static MyTaskApplication getInstance() {
        return sInstance;
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    public TaskListComponent getTaskListComponent() {
        if (mTaskListComponent == null) {
            mTaskListComponent = mAppComponent.createTaskListComponent();
        }
        return mTaskListComponent;
    }

    public void clearTaskListComponent() {
        mTaskListComponent = null;
    }

    private void setUpDb() {
        RealmConfiguration configuration = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);
    }
}