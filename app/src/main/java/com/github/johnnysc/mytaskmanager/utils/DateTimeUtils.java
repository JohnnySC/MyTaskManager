package com.github.johnnysc.mytaskmanager.utils;

import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Simple class to generate full date
 *
 * @author Asatryan on 02.04.18.
 */

public final class DateTimeUtils {

    private DateTimeUtils() {
        throw new IllegalStateException("not supposed to instantiate an object of a class " + DateTimeUtils.class.getCanonicalName());
    }

    /**
     * @param datePicker to get date
     * @param timePicker to get time
     * @return date composed by date and time pickers
     */
    public static java.util.Date getDateFromDatePicker(DatePicker datePicker, TimePicker timePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);

        return calendar.getTime();
    }
}