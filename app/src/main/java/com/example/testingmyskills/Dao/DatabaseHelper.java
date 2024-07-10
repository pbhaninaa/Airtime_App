package com.example.testingmyskills.Dao;

import static com.example.testingmyskills.UI.MainActivity.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.testingmyskills.JavaClasses.Bundles;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_ID = 1;
    private final Context context;
    private final int databaseId;
    private final SharedPreferences preferences;
    private static final String DATABASE_NAME = "Database";
    private static final int DATABASE_VERSION = 80;
    private static final String ASSETS_PATH = "databases";
    public static final String TABLE_NAME = "Data";
    public static final String COLUMN_CATEGORY = "Category";
    public static final String COLUMN_MAP_NAME = "MapName";
    public static final String COLUMN_PRICE_PLAN_CODE = "PricePlanCode";
    public static final String COLUMN_BUNDLE = "Bundle";
    public static final String COLUMN_VALIDITY = "Validity";
    public static final String COLUMN_VOLUME_MB = "VolumeMB";
    public static final String COLUMN_CURRENT_USD_CHARGE = "CurrentUSDCharge";



    public DatabaseHelper(Context context, int databaseId) {
        super(context, DATABASE_NAME + databaseId, null, DATABASE_VERSION);
        this.context = context;
        this.databaseId = databaseId;
        this.preferences = context.getSharedPreferences(
                context.getPackageName() + ".database_versions",
                Context.MODE_PRIVATE
        );
    }

    private boolean installedDatabaseIsOutdated() {
        return preferences.getInt(DATABASE_NAME + databaseId, 0) < DATABASE_VERSION;
    }

    private void writeDatabaseVersionInPreferences() {
        preferences.edit().putInt(DATABASE_NAME + databaseId, DATABASE_VERSION).apply();
    }

    private void installOrUpdateIfNecessary() {
        context.deleteDatabase(DATABASE_NAME);
        Log.d("Database name", DATABASE_NAME);
        if (installedDatabaseIsOutdated()) {
            context.deleteDatabase(DATABASE_NAME + databaseId);
            installDatabaseFromAssets();
            writeDatabaseVersionInPreferences();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Nothing to do
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Nothing to do
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        installOrUpdateIfNecessary();
        return super.getWritableDatabase();
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        installOrUpdateIfNecessary();
        return super.getReadableDatabase();
    }

    private void installDatabaseFromAssets() {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = context.getAssets().open(ASSETS_PATH + "/" + DATABASE_NAME + ".sqlite3");
            File outputFile = context.getDatabasePath(DATABASE_NAME + databaseId);

            if (!outputFile.exists()) {
                File dirCreate = new File(outputFile.getParent());
                if (!dirCreate.exists() && !dirCreate.mkdirs()) {
                    throw new IOException("Failed to create directory: " + dirCreate.getAbsolutePath());
                }
            }

            outputStream = new FileOutputStream(outputFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
            Log.e("DatabaseHelper", "Error installing database", exception);
            throw new RuntimeException("The " + DATABASE_NAME + " database couldn't be installed.", exception);
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            } catch (IOException e) {
                Log.e("DatabaseHelper", "Error closing streams", e);
            }
        }
    }


    //=========================================== User Manipulation ======================================================


    public static boolean saveUser(String Name, String Surname,
                                   String Email, String Password, String Phone) {

        ContentValues values = new ContentValues();
        values.put("Name", Name);
        values.put("Surname", Surname);
        values.put("Email", Email);
        values.put("Password", Password);
        values.put("PhoneNumber", Phone);

        long newRowId = -1;
        try {
            newRowId = db.insertOrThrow("Users", null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return newRowId != -1;
    }

    public static List<String[]> getAllUsers(SQLiteDatabase db, String specific) {
        List<String[]> allData = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Cashier", null);
        try {
            if (cursor.moveToFirst()) {
                int transDateIndex = cursor.getColumnIndex("tran_time");
                while (!cursor.isAfterLast()) {
                    int numColumns = cursor.getColumnCount();
                    String[] rowData = new String[numColumns];
                    for (int i = 0; i < numColumns; i++) {
                        if (cursor.getType(i) == Cursor.FIELD_TYPE_BLOB) {
                            // Handle BLOB data appropriately
                            byte[] blobData = cursor.getBlob(i);
                            // Convert blobData to string or handle it as needed
                            // Example: rowData[i] = new String(blobData);
                        } else {
                            // Handle other data types
                            rowData[i] = cursor.getString(i);
                        }
                    }
                    allData.add(rowData);

                    // Print the trans_date if the column exists
                    if (transDateIndex != -1) {
                        String transDate = cursor.getString(transDateIndex);
                        System.out.println("Transaction Time: " + transDate);
                    }

                    cursor.moveToNext();
                }
            }
        } finally {
            cursor.close();
        }
        return allData;
    }

    public static List<String[]> getSpecificUsers(SQLiteDatabase db, String specific) {
        List<String[]> allData = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Cashier WHERE EmpPermission=" + specific, null);
        try {
            if (cursor.moveToFirst()) {
                int transDateIndex = cursor.getColumnIndex("tran_time");
                while (!cursor.isAfterLast()) {
                    int numColumns = cursor.getColumnCount();
                    String[] rowData = new String[numColumns];
                    for (int i = 0; i < numColumns; i++) {
                        if (cursor.getType(i) == Cursor.FIELD_TYPE_BLOB) {
                            // Handle BLOB data appropriately
                            byte[] blobData = cursor.getBlob(i);
                            // Convert blobData to string or handle it as needed
                            // Example: rowData[i] = new String(blobData);
                        } else {
                            // Handle other data types
                            rowData[i] = cursor.getString(i);
                        }
                    }
                    allData.add(rowData);

                    // Print the trans_date if the column exists
                    if (transDateIndex != -1) {
                        String transDate = cursor.getString(transDateIndex);
                        System.out.println("Transaction Time: " + transDate);
                    }

                    cursor.moveToNext();
                }
            }
        } finally {
            cursor.close();
        }
        return allData;
    }

    public static List<String[]> getSpecificUser(SQLiteDatabase db, int id) {
        List<String[]> allData = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Cashier WHERE EmpId=" + id, null);
        try {
            if (cursor.moveToFirst()) {
                int transDateIndex = cursor.getColumnIndex("tran_time");
                while (!cursor.isAfterLast()) {
                    int numColumns = cursor.getColumnCount();
                    String[] rowData = new String[numColumns];
                    for (int i = 0; i < numColumns; i++) {
                        if (cursor.getType(i) == Cursor.FIELD_TYPE_BLOB) {
                            // Handle BLOB data appropriately
                            byte[] blobData = cursor.getBlob(i);
                            // Convert blobData to string or handle it as needed
                            // Example: rowData[i] = new String(blobData);
                        } else {
                            // Handle other data types
                            rowData[i] = cursor.getString(i);
                        }
                    }
                    allData.add(rowData);

                    // Print the trans_date if the column exists
                    if (transDateIndex != -1) {
                        String transDate = cursor.getString(transDateIndex);
                        System.out.println("Transaction Time: " + transDate);
                    }

                    cursor.moveToNext();
                }
            }
        } finally {
            cursor.close();
        }
        return allData;
    }

    //============================================Data Manipulation =========================================================




    public static void insertDataFromArray(Bundles[] dataItems) {
        for (Bundles item : dataItems) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CATEGORY, item.getCategory());
            values.put(COLUMN_MAP_NAME, item.getMapName());
            values.put(COLUMN_PRICE_PLAN_CODE, item.getPricePlanCode());
            values.put(COLUMN_BUNDLE, item.getBundle());
            values.put(COLUMN_VALIDITY, item.getValidity());
            values.put(COLUMN_VOLUME_MB, String.valueOf(item.getVolumeMB())); // Converting to String
            values.put(COLUMN_CURRENT_USD_CHARGE, item.getCurrentUSDCharge());

            // Log values before inserting into the database
            Log.d("DatabaseHelper", "Inserting data: ");
            Log.d("DatabaseHelper", "Category: " + item.getCategory());
            Log.d("DatabaseHelper", "MapName: " + item.getMapName());
            Log.d("DatabaseHelper", "PricePlanCode: " + item.getPricePlanCode());
            Log.d("DatabaseHelper", "Bundle: " + item.getBundle());
            Log.d("DatabaseHelper", "Validity: " + item.getValidity());
            Log.d("DatabaseHelper", "VolumeMB: " + item.getVolumeMB());
            Log.d("DatabaseHelper", "CurrentUSDCharge: " + item.getCurrentUSDCharge());

            db.insert(TABLE_NAME, null, values);
        }
    }


    public List<String[]> getAllData() {
        List<String[]> allData = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int numColumns = cursor.getColumnCount();
                    String[] rowData = new String[numColumns];
                    for (int i = 0; i < numColumns; i++) {
                        rowData[i] = cursor.getString(i);
                    }
                    allData.add(rowData);
                } while (cursor.moveToNext());
            } else {
                Log.d("DatabaseHelper", "No data found in the table");
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error retrieving data from database", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return allData;
    }




}
 