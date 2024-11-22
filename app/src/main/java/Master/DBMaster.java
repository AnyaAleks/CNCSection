package Master;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class DBMaster extends SQLiteOpenHelper {

    private static final String TAG = "DBHELPER";
    public static final String DBNAME = "CNCSectionDB.db";
    public static final int DBVERSION = 1;

    SQLiteDatabase mDB;

    public DBMaster(Context context) {
        super(context,DBNAME,null,DBVERSION);
        if (!ifDBExists(context)) {
            if (!copyDBFromAssets(context)) {
                throw new RuntimeException("Failed to Copy Database From Assets Folder");
            }
        }
        mDB = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Do NOTHING in here as the database has been copied from the assets
        // if it did not already exist
        Log.d(TAG, "METHOD onCreate called");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d(TAG,"METHOD onUpgrade called");
    }

    public Cursor getAll(String table_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + table_name, null);
        Log.e("LogTag", table_name + ".getCount(): " + res.getCount());
        return res;
    }

    public Cursor getAllOrder() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from 'Order'", null);
        return res;
    }

    public Cursor sortInOrder(String table_name, String sort_field) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + table_name
                + " ORDER BY " + sort_field, null);
        Log.e("LogTag", table_name + ".getCount(): " + res.getCount());
        return res;
    }

    public Cursor searchItem(String table_name, String compare_field,String search_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + table_name
                + " WHERE " + compare_field + " = " + search_id, null);
        Log.e("LogTag", table_name + ".getCount(): " + res.getCount());
        return res;
    }

    public long addToStaff(int id_access, String fio, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Создание объекта для данных (добавления новых строк в таблицу)
        ContentValues cv = new ContentValues();
        //cv.put("id_staff", 1); - autoincrement
        cv.put("id_access", id_access);
        cv.put("fio", fio);
        cv.put("password", password);

        long rowID = db.insert("Staff", null, cv);
        Log.e("LogTag", "addToEmployees" + " rowID " +rowID);

        return rowID;
    }

    public long addToOrder(int id_staff_master, String production_time, int id_request, String date_start) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Создание объекта для данных (добавления новых строк в таблицу)
        ContentValues cv = new ContentValues();
        cv.put("id_staff_master", id_staff_master);
        cv.put("production_time", production_time);
        cv.put("id_request", id_request);
        cv.put("date_start", date_start);

        long rowID = db.insert("'Order'", null, cv);
        Log.e("LogTag", "addToOrder" + " rowID " +rowID);

        return rowID;
    }

    public long addToOrder_and_Machine(int id_order, int id_machine) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Создание объекта для данных (добавления новых строк в таблицу)
        ContentValues cv = new ContentValues();
        cv.put("id_order", id_order);
        cv.put("id_machine", id_machine);

        long rowID = db.insert("Order_and_Machine", null, cv);
        Log.e("LogTag", "addToOrder_and_Machine" + " rowID " +rowID);

        return rowID;
    }

    public long addToOrder_and_Operator(int id_order, int id_staff) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Создание объекта для данных (добавления новых строк в таблицу)
        ContentValues cv = new ContentValues();
        cv.put("id_order", id_order);
        cv.put("id_staff", id_staff);

        long rowID = db.insert("Order_and_Operator", null, cv);
        Log.e("LogTag", "addToOrder_and_Operator" + " rowID " +rowID);

        return rowID;
    }

    public long addToOrder_and_Osnaska(int id_order, int id_osnaska) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Создание объекта для данных (добавления новых строк в таблицу)
        ContentValues cv = new ContentValues();
        cv.put("id_order", id_order);
        cv.put("id_osnaska", id_osnaska);

        long rowID = db.insert("Order_and_Osnaska", null, cv);
        Log.e("LogTag", "addToOrder_and_Osnaska" + " rowID " +rowID);

        return rowID;
    }

    public long addToOrder_and_Tool(int id_order, int id_tool) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Создание объекта для данных (добавления новых строк в таблицу)
        ContentValues cv = new ContentValues();
        cv.put("id_order", id_order);
        cv.put("id_tool", id_tool);

        long rowID = db.insert("Order_and_Tool", null, cv);
        Log.e("LogTag", "addToOrder_and_Tool" + " rowID " +rowID);

        return rowID;
    }

    public Cursor updateValueById(String table_name, String update_idField, int update_id, String update_field, int update_value){
        //"update "+Table_name+" set availability='0' where product_name like 'bar'"
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("update " + table_name
                + " set " + update_field + " = " + update_value + " where " + update_idField + " like " + update_id, null);
        Log.e("LogTag", table_name + ".getCount(): " + res.getCount());
        return res;
    }

    /*
        Copies the database from the assets folder to the apps database folder (with logging)
        note databases folder is typically data/data/the_package_name/database
             however using getDatabasePath method gets the actual path (should it not be as above)
        This method can be significantly reduced one happy that it works.
     */
    private boolean copyDBFromAssets(Context context) {
        Log.d("CPYDBINFO","Starting attemtpt to cop database from the assets file.");
        String DBPATH = context.getDatabasePath(DBNAME).getPath();
        InputStream is;
        OutputStream os;
        int buffer_size = 8192;
        int length = buffer_size;
        long bytes_read = 0;
        long bytes_written = 0;
        byte[] buffer = new byte[length];

        try {

            is = context.getAssets().open(DBNAME);
        } catch (IOException e) {
            Log.e("CPYDB FAIL - NO ASSET","Failed to open the Asset file " + DBNAME);
            e.printStackTrace();
            return false;
        }

        try {
            os = new FileOutputStream(DBPATH);
        } catch (IOException e) {
            Log.e("CPYDB FAIL - OPENDB","Failed to open the Database File at " + DBPATH);
            e.printStackTrace();
            return false;
        }
        Log.d("CPYDBINFO","Initiating copy from asset file" + DBNAME + " to " + DBPATH);
        while (length >= buffer_size) {
            try {
                length = is.read(buffer,0,buffer_size);
            } catch (IOException e) {
                Log.e("CPYDB FAIL - RD ASSET",
                        "Failed while reading in data from the Asset. " +
                                String.valueOf(bytes_read) +
                                " bytes read successfully."
                );
                e.printStackTrace();
                return false;
            }
            bytes_read = bytes_read + length;
            try {
                os.write(buffer,0,buffer_size);
            } catch (IOException e) {
                Log.e("CPYDB FAIL - WR ASSET","failed while writing Database File " +
                        DBPATH +
                        ". " +
                        String.valueOf(bytes_written) +
                        " bytes written successfully.");
                e.printStackTrace();
                return false;

            }
            bytes_written = bytes_written + length;
        }
        Log.d("CPYDBINFO",
                "Read " + String.valueOf(bytes_read) + " bytes. " +
                        "Wrote " + String.valueOf(bytes_written) + " bytes."
        );
        try {
            os.flush();
            is.close();
            os.close();
        } catch (IOException e ) {
            Log.e("CPYDB FAIL - FINALISING","Failed Finalising Database Copy. " +
                    String.valueOf(bytes_read) +
                    " bytes read." +
                    String.valueOf(bytes_written) +
                    " bytes written."
            );
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /*
    Checks to see if the database exists if not will create the respective directory (database)
    Creating the directory overcomes the NOT FOUND error
    */

    private boolean ifDBExists(Context context) {
        String dbparent = context.getDatabasePath(DBNAME).getParent();
        File f = context.getDatabasePath(DBNAME);
        if (!f.exists()) {
            Log.d("NODB MKDIRS","Database file not found, making directories."); //<<<< remove before the App goes live.
            File d = new File(dbparent);
            d.mkdirs();
            //return false;
        }
        return f.exists();
    }
}