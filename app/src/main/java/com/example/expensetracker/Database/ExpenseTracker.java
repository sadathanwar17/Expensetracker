package com.example.expensetracker.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Anzy on 16-02-2016.
 * This is a simple database helper class used to store the dates and relevant fields of expenses.
 * It also has all the basic CRUD operations.
 */
public class ExpenseTracker {

    /**
     * Required database variables specifying the structure of the database table
     *
     * @param _id is used as a unique column no and is automatically incremented
     * @param date is used to store the date
     * @param fields is used to store the item names
     * @param values is used to store the cost
     * @param total is used to store the total costs
     */
    public static final String TABLE_NAME = "ExpenseTracker";
    public static final String COLUMN_NO = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_FIELDS = "fields";
    public static final String COLUMN_VALUES = "valuez";
    public static final String COLUMN_TOTAL = "total";

    /**
     * Here
     *
     * @param expenseTracker.db is the database name which is used to store the datas
     * @param DATABASE_VERSION is the version of the database and is incremented when its updated in the app
     */
    static final String DATABASE_NAME = "expenseTracker.db";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;
    private DatabaseHelper mDbHelper;

    public class DatabaseHelper extends SQLiteOpenHelper {

        // Compulsory constructor
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            // DATABASE TABLE CREATION COMMAND STORED AS A STRING WITH AL THE REQUIRED PARAMETERS AND DATA TYPES
            final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( " + COLUMN_NO + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_DATE + " TEXT NOT NULL, " + COLUMN_FIELDS + " TEXT NOT NULL, " + COLUMN_VALUES + " INTEGER, "
                    + COLUMN_TOTAL + " INTEGER " + ");";

            // COMMAND TO CREATE A TABLE
            db.execSQL(SQL_CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // DROPPING THE TABLE IF IT EXISTS DURING UPDATION AND THEN CREATING THE TABLE USING onCreate()
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    public ExpenseTracker(Context context) {
        this.mCtx = context;
    }

    public void write(String date, String fields, int values, int total) {
        mDbHelper = new DatabaseHelper(mCtx);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_FIELDS, fields);
        cv.put(COLUMN_VALUES, values);
        cv.put(COLUMN_TOTAL, total);
        db.insert(TABLE_NAME, null, cv);
        db.close();
    }

    public Cursor readAll(String date) {
        mDbHelper = new DatabaseHelper(mCtx);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_DATE + "=?", new String[] {date});
    }

    public int check() {
        mDbHelper = new DatabaseHelper(mCtx);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String count = "SELECT count(*) FROM " + TABLE_NAME;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        mDbHelper.close();
        return icount;
    }

    public int readTotal(String date)
    {
        mDbHelper = new DatabaseHelper(mCtx);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_DATE + "=?",new String[]{date});
        int data = 0;
        if(cursor.moveToFirst())
        {
            do {
                data += cursor.getInt(3);
            }while(cursor.moveToNext());
        }
        return data;
    }

}
