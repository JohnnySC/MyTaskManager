package com.github.johnnysc.mytaskmanager;

import android.widget.TimePicker;

/**
 * @author Asatryan on 02.04.18.
 */

public interface TimePickerCallback {

    void doOnTimePicked(TimePicker view, int hourOfDay, int minute);
}