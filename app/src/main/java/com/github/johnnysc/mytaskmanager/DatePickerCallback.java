package com.github.johnnysc.mytaskmanager;

import android.widget.DatePicker;

/**
 * @author Asatryan on 02.04.18.
 */

public interface DatePickerCallback {

    void doOnDatePicked(DatePicker view, int year, int month, int day);
}