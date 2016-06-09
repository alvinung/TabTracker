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
            TableData.TableInfo.TYPE + " TEXT," + TableData.TableInfo.LOAN + " TEXT);";

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

    // adding to tabs_table
    public void addTab (DatabaseOperations dbop, String name, String date, String amount, String reason, String type, String loan) {

        SQLiteDatabase sq = dbop.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TableData.TableInfo.NAME, name);
        cv.put(TableData.TableInfo.DATE, date);
        cv.put(TableData.TableInfo.AMOUNT, amount);
        cv.put(TableData.TableInfo.REASON, reason);
        cv.put(TableData.TableInfo.POST_NUM, postCount);
        cv.put(TableData.TableInfo.TYPE, type);
        cv.put(TableData.TableInfo.LOAN, loan);
        postCount++;

        sq.insert(TableData.TableInfo.TABS_TABLE, null, cv);
        Log.d("Database Operations", "One row inserted into tabs_table");
    }

    // pulling all data from tabs_table for history
    public Cursor getTab (DatabaseOperations dbop) {

        SQLiteDatabase sq = dbop.getReadableDatabase();
        String[] columns = {TableData.TableInfo.NAME, TableData.TableInfo.DATE, TableData.TableInfo.AMOUNT,
                TableData.TableInfo.REASON, TableData.TableInfo.POST_NUM, TableData.TableInfo.TYPE, TableData.TableInfo.LOAN};
        String orderBy = TableData.TableInfo.POST_NUM + " DESC";

        Cursor cr = sq.query(TableData.TableInfo.TABS_TABLE, columns, null, null, null, null, orderBy);
        return cr;
    }

    // pulling from tabs_table for specific person
    public Cursor getSpecTab (DatabaseOperations dbop, String name) {

        SQLiteDatabase sq = dbop.getReadableDatabase();
        String[] columns = {TableData.TableInfo.NAME, TableData.TableInfo.DATE, TableData.TableInfo.AMOUNT,
                TableData.TableInfo.REASON, TableData.TableInfo.POST_NUM, TableData.TableInfo.TYPE, TableData.TableInfo.LOAN};
        String where = TableData.TableInfo.NAME + " = ?";
        String[] whereArgs = new String[] {name};
        String orderBy = TableData.TableInfo.POST_NUM + " DESC";

        Cursor cr = sq.query(TableData.TableInfo.TABS_TABLE, columns, where, whereArgs, null, null, orderBy);
        return cr;
    }

}
