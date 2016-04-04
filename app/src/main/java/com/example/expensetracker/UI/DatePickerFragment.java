package com.example.expensetracker.UI;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public DatePickerDialogListener mListener;
    public static DatePickerFragment newInstance() {
        DatePickerFragment fragment = new DatePickerFragment();
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Use the current time as the default values for the time picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DATE);
        mListener = getActivity() instanceof DatePickerDialogListener ? (DatePickerFragment.DatePickerDialogListener) getActivity() : null;

        //Create and return a new instance of TimePickerDialog
        return new DatePickerDialog(getActivity(), this, year, month,
                day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (mListener != null) mListener.onDateSet(view, year, month, day);
    }

    public static interface DatePickerDialogListener {
        public void onDateSet(DatePicker view, int year, int month, int day);
    }
}
