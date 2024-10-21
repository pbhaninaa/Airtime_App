package com.example.testingmyskills.Dao;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class HelperClass {
//public class DatabaseHelper extends SQLiteOpenHelper {

    /*private static final int DATABASE_ID = 1;
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
*/

    public static JSONObject sendPostRequest(String urlString, String jsonInputString) throws Exception {
// Parse jsonInputString to extract TransactionType
        JSONObject jsonObject = new JSONObject(jsonInputString);
        String transactionType = jsonObject.getString("TransactionType");
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");  // Set content type to application/json
        conn.setRequestProperty("Accept", "application/json");        // Ensure the server returns JSON
        conn.setDoOutput(true);

        // Send JSON data
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Get the response code
        int responseCode = conn.getResponseCode();
        System.out.println("Response Code: " + responseCode);
//
//        if (responseCode == HttpURLConnection.HTTP_OK) {
//            // Success, read the response
//            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
//                StringBuilder response = new StringBuilder();
//                System.out.println(br);
//                String responseLine;
//                while ((responseLine = br.readLine()) != null) {
//                    response.append(responseLine.trim());
//                }
//                System.out.println("Transaction Type:" + transactionType);
//                System.out.println("Request Response: " + response.toString());
//
//                return response.length() > 0 ? response.toString() : "Empty response from server.";
//            }
//        } else {
//            // Handle non-200 responses, read error stream if available
//            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
//                StringBuilder errorResponse = new StringBuilder();
//                String errorLine;
//                while ((errorLine = br.readLine()) != null) {
//                    errorResponse.append(errorLine.trim());
//                }
//                System.out.println("Error Response: " + errorResponse.toString());
//                return "Error: " + errorResponse.toString();
//            } catch (Exception e) {
//                return "Error: Unable to read error response. Response Code " + responseCode;
//            }
//        }

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Success, read the response
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }


                // Construct JSON response
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("responseCode", responseCode);
                jsonResponse.put("response", response.length() > 0 ? response.toString() : "Empty response from server.");

                System.out.println("Transaction Type: " + transactionType);
                System.out.println("Request Response: " + jsonResponse);
                return jsonResponse;  // Return JSON as a string
            }
        } else {
            // Handle non-200 responses, read error stream if available
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                StringBuilder errorResponse = new StringBuilder();
                String errorLine;
                while ((errorLine = br.readLine()) != null) {
                    errorResponse.append(errorLine.trim());
                }
                System.out.println("Error Response: " + errorResponse.toString());

                // Construct JSON error response
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("responseCode", responseCode);
                jsonResponse.put("error", errorResponse.toString());

                return jsonResponse;  // Return JSON as a string
            } catch (Exception e) {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("responseCode", responseCode);
                jsonResponse.put("error", "Unable to read error response.");

                return jsonResponse;  // Return JSON as a string
            }
        }


    }

}
 