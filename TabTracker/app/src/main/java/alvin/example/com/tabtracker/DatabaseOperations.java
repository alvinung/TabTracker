package alvin.example.com.tabtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Alvin on 6/3/2016.
 */
public class DatabaseOperations extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static int postCount = 1;

    // string to create tabs_table
    public String CREATE_TABS_DATA_QUERY = "CREATE TABLE " + TableData.TableInfo.TABS_TABLE + "(" +
            TableData.TableInfo.NAME + " TEXT," + TableData.TableInfo.DATE + " TEXT," + TableData.TableInfo.AMOUNT +
            " TEXT," + TableData.TableInfo.REASON + " TEXT," + TableData.TableInfo.POST_NUM + " TEXT," +
            TableData.TableInfo.TYPE + " TEXT," + TableData.TableInfo.LOAN + " TEXT," +
            TableData.TableInfo.TIME + " TEXT);";

    // string to create totals_table
    public String CREATE_TOTALS_TABLE_QUERY = "CREATE TABLE " + TableData.TableInfo.TOTALS_TABLE + "(" +
            TableData.TableInfo.OWED + " TEXT," + TableData.TableInfo.OWE + " TEXT," + TableData.TableInfo.TIME + " TEXT);";

    // create database
    public DatabaseOperations(Context context) {
        super(context, TableData.TableInfo.DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("Database Operations", "Database created");
    }

    @Override
    public void onCreate(SQLiteDatabase sdb) {

        // create tabs_table
        Log.d("Database Operations", "creating tabs_table");
        sdb.execSQL(CREATE_TABS_DATA_QUERY);
        Log.d("Database Operations", "tabs_table created");

        // create totals_table
        Log.d("Database Operations", "creating totals_table");
        sdb.execSQL(CREATE_TOTALS_TABLE_QUERY);
        Log.d("Database Operations", "totals_table created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sdb, int oldVersion, int newVersion) {

    }

    // getting current date
    public static String getDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String result = sdf.format(date).toString();
        return result;
    }

    // get current time
    public static String getTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss.SSS a");
        String time = sdf.format(date).toString();
        return time;
    }

    // adding to totals_table
    public void addTotal (DatabaseOperations dbop, double owed, double owe) {

        SQLiteDatabase sq = dbop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String time = getTime();

        cv.put(TableData.TableInfo.OWED, owed);
        cv.put(TableData.TableInfo.OWE, owe);
        cv.put(TableData.TableInfo.TIME, time);

        sq.insert(TableData.TableInfo.TOTALS_TABLE, null, cv);
        Log.d("Database Operations", "One row inserted into totals_table");

    }

    // adding to tabs_table
    public void addTab (DatabaseOperations dbop, String name, String date, String amount, String reason, String type, String loan) {

        SQLiteDatabase sq = dbop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String time = getTime();

        cv.put(TableData.TableInfo.NAME, name);
        cv.put(TableData.TableInfo.DATE, date);
        cv.put(TableData.TableInfo.AMOUNT, amount);
        cv.put(TableData.TableInfo.REASON, reason);
        cv.put(TableData.TableInfo.POST_NUM, postCount);
        cv.put(TableData.TableInfo.TYPE, type);
        cv.put(TableData.TableInfo.LOAN, loan);
        cv.put(TableData.TableInfo.TIME, time);
        postCount++;

        sq.insert(TableData.TableInfo.TABS_TABLE, null, cv);
        Log.d("Database Operations", "One row inserted into tabs_table");
    }

    // pulling most recent data from totals_table
    public Cursor getTotal (DatabaseOperations dbop) {
        SQLiteDatabase sq = dbop.getReadableDatabase();
        String[] columns = {TableData.TableInfo.OWED, TableData.TableInfo.OWE, TableData.TableInfo.TIME};
        String orderBy = TableData.TableInfo.TIME + " DESC LIMIT 1";
        Cursor cr = sq.query(TableData.TableInfo.TOTALS_TABLE, columns, null, null, null, null, orderBy);
        return cr;
    }

    // pulling all data from tabs_table for history
    public Cursor getTab (DatabaseOperations dbop) {

        SQLiteDatabase sq = dbop.getReadableDatabase();
        String[] columns = {TableData.TableInfo.NAME, TableData.TableInfo.DATE, TableData.TableInfo.AMOUNT,
                TableData.TableInfo.REASON, TableData.TableInfo.POST_NUM, TableData.TableInfo.TYPE, TableData.TableInfo.LOAN,
                TableData.TableInfo.TIME};
        String orderBy = TableData.TableInfo.TIME + " DESC";

        Cursor cr = sq.query(TableData.TableInfo.TABS_TABLE, columns, null, null, null, null, orderBy);
        return cr;
    }

    // pulling from tabs_table for specific person
    public Cursor getSpecTab (DatabaseOperations dbop, String name) {

        SQLiteDatabase sq = dbop.getReadableDatabase();
        String[] columns = {TableData.TableInfo.NAME, TableData.TableInfo.DATE, TableData.TableInfo.AMOUNT,
                TableData.TableInfo.REASON, TableData.TableInfo.POST_NUM, TableData.TableInfo.TYPE, TableData.TableInfo.LOAN,
                TableData.TableInfo.TIME};
        String where = TableData.TableInfo.NAME + " = ?";
        String[] whereArgs = new String[] {name};
        String orderBy = TableData.TableInfo.TIME + " DESC";

        Cursor cr = sq.query(TableData.TableInfo.TABS_TABLE, columns, where, whereArgs, null, null, orderBy);
        return cr;
    }

}
