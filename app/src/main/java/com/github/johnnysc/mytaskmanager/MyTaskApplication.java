package com.github.johnnysc.mytaskmanager;

import android.app.Application;

import com.github.johnnysc.mytaskmanager.main.di.AppComponent;
import com.github.johnnysc.mytaskmanager.main.di.AppModule;
import com.github.johnnysc.mytaskmanager.main.di.DaggerAppComponent;

import io.realm.Realm;

/**
 * @author JohnnySC on 19.03.18.
 */

public class MyTaskApplication extends Application {

    private static MyTaskApplication sInstance;
    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

       Realm.init(this);
    }

    public static MyTaskApplication getInstance() {
        return sInstance;
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}