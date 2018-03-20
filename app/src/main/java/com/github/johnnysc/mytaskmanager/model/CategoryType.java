package com.github.johnnysc.mytaskmanager.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author JohnnySC on 19.03.18.
 */

public final class CategoryType {

    public static final int NOT_IMPORTANT_NOT_URGENT = 0;
    public static final int IMPORTANT_NOT_URGENT = 1;
    public static final int URGENT_NOT_IMPORTANT = 2;
    public static final int URGENT_IMPORTANT = 3;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NOT_IMPORTANT_NOT_URGENT,
            IMPORTANT_NOT_URGENT,
            URGENT_NOT_IMPORTANT,
            URGENT_IMPORTANT
    })
    public @interface TaskType {
    }
}
