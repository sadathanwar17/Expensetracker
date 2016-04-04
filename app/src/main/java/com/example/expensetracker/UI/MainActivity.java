package com.example.expensetracker.UI;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.expensetracker.Database.ExpenseTracker;
import com.example.expensetracker.R;

import java.util.Calendar;

public class MainActivity extends ListActivity implements DatePickerFragment.DatePickerDialogListener {

    /*Variable declaration for button and texviews*/
    Button addButton;
    TextView dateTextView,totalTextView;

    //String declaration for date,field,value and total
    String date,alertDialogField,alertDialogValue;
    int total = 0;

    // Declaration for Database

    ExpenseTracker db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dateTextView = (TextView) findViewById(R.id.DateTextView);
        addButton = (Button) findViewById(R.id.AddButton);
        totalTextView = (TextView) findViewById(R.id.TotalTextView);
        db = new ExpenseTracker(this);
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);
        if (month < 10 && day < 10) {
            date = "0" + day + "/" + "0" + month + "/" + year;
        } else if (month < 10) {
            date = day + "/" + "0" + month + "/" + year;
        } else if (day < 10) {
            date = "0" + day + "/" + month + "/" + year;
        } else {
            date = day + "/" + month + "/" + year;
        }
        dateTextView.setText(date);
        LoadData(date);
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker(v);
                LoadData(date);
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = getLayoutInflater();
                View promptsView = li.inflate(R.layout.activity_alertdialog, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.AppCompatAlertDialogStyle);
                alertDialogBuilder.setTitle("Add Field");
                alertDialogBuilder.setView(promptsView);
                final EditText alertDialogFields = (EditText) promptsView.findViewById(R.id.alertDialogFields);
                final EditText alertDialogValues = (EditText) promptsView.findViewById(R.id.alertDialogValues);
                alertDialogBuilder
                        .setCancelable(true)
                        .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialogField = alertDialogFields.getText().toString();
                                alertDialogValue = alertDialogValues.getText().toString();
                                total += Integer.parseInt(alertDialogValue);
                                db.write(date, alertDialogField, Integer.parseInt(alertDialogValue), total);
                                LoadData(date);
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        month = month + 1;
        if (month < 10 && day < 10) {
            date = "0" + day + "/" + "0" + month + "/" + year;
        } else if (month < 10) {
            date = day + "/" + "0" + month + "/" + year;
        } else if (day < 10) {
            date = "0" + day + "/" + month + "/" + year;
        } else {
            date = day + "/" + month + "/" + year;
        }
        dateTextView.setText(date);
        LoadData(date);
    }

    public void datePicker(View v) {
        DialogFragment newFragment = DatePickerFragment.newInstance();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void LoadData(String date)
    {
        Cursor cursor = db.readAll(date);
        String[] from = new String[] {db.COLUMN_FIELDS,db.COLUMN_VALUES};
        int[] to = new int[] {R.id.fieldTextView,R.id.valuesTextView};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,R.layout.activity_textviews,cursor,from,to,0);
        setListAdapter(adapter);
        if(db.check()>0)
            totalTextView.setText("Total is " + db.readTotal(date));
        else
            totalTextView.setText("Total is 0");
    }
}
