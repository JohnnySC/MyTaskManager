package com.github.johnnysc.mytaskmanager.main.data.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.List;

/**
 * @author JohnnySC on 19.03.18.
 */

public final class CategoryType {

    public static final int NOT_IMPORTANT_NOT_URGENT = 0;
    public static final int IMPORTANT_NOT_URGENT = 1;
    public static final int URGENT_NOT_IMPORTANT = 2;
    public static final int URGENT_IMPORTANT = 3;

    public static final List<Integer> CATEGORY_TYPES = Arrays.asList(
            NOT_IMPORTANT_NOT_URGENT,
            IMPORTANT_NOT_URGENT,
            URGENT_NOT_IMPORTANT,
            URGENT_IMPORTANT
    );

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NOT_IMPORTANT_NOT_URGENT,
            IMPORTANT_NOT_URGENT,
            URGENT_NOT_IMPORTANT,
            URGENT_IMPORTANT
    })
    public @interface TaskType {
    }
}