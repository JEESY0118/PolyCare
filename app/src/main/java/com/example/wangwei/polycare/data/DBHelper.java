package com.example.wangwei.polycare.data;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "mydatabase.sqlite";
    private static final String ACTIVITY_TAG = "LogDemo";

    private SQLiteDatabase myDataBase;
    private final Context myContext;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    public void openDataBase() throws SQLException, IOException {
        //Open the database
        String myPath = myContext.getDatabasePath(DB_NAME).getAbsolutePath();
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            try {
                // Copy the database in assets to the application database.
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database", e);
            }
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = myContext.getDatabasePath(DB_NAME).getAbsolutePath();
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            //database doesn't exist yet.
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null;
    }

    public void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = myContext.getDatabasePath(DB_NAME).getAbsolutePath();
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    public List<Event> getAllEvent() {
        Event event = null;
        List<Event> events = new ArrayList<>();
        try {
            createDataBase();
            openDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Cursor cursor = myDataBase.rawQuery("\n" +
                "                SELECT e.event_title, c.category_name, e.event_description,\n" +
                "                u.user_firstname, g.urgence_name, s.state_name,e.event_report_date, u.user_phone\n" +
                "                FROM events e, categories c, users u, states s, urgences g\n" +
                "                where e.event_reporter = u.user_id\n" +
                "                and e.event_category = c.category_id\n" +
                "\t\t\t\tand e.event_importance = g.urgence_id\n" +
                "\t\t\t\tand e.event_state = s.state_id", null);
        cursor.moveToNext();
        while (!cursor.isAfterLast()) {
            event = new Event(cursor.getString(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5), cursor.getString(6),
                    cursor.getString(7));
            events.add(event);
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return events;
    }

    public static String getDbName() {
        return DB_NAME;
    }
}