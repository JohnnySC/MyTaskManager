package com.github.johnnysc.mytaskmanager.dateandtime;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Fragment to pick a time
 *
 * @author Asatryan on 02.04.18.
 */

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private TimePickerCallback mTimePickerCallback;

    public static TimePickerFragment newInstance() {
        return new TimePickerFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mTimePickerCallback = (TimePickerCallback) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mTimePickerCallback = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        if (view.isShown() && mTimePickerCallback != null) {
            mTimePickerCallback.doOnTimePicked(view, hourOfDay, minute);
        }
    }
}