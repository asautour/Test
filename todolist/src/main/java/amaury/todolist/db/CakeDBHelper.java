package amaury.todolist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import amaury.todolist.data.Cake;
import amaury.todolist.data.Recipe;

public class CakeDBHelper extends SQLiteOpenHelper {
    public static final String TABLE_CAKE_NAMES     = "cakes";
    public static final String KEY_ID               = BaseColumns._ID;
    public static final String KEY_NAME             = "cake_name";

    private static CakeDBHelper sInstance;

    public static synchronized CakeDBHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null)
            sInstance = new CakeDBHelper(context.getApplicationContext());
        return sInstance;
    }

    public CakeDBHelper(Context context) {
        super(context, DBUtils.DATABASE_NAME, null, DBUtils.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String sqlQuery = String.format(
                "CREATE TABLE IF NOT EXISTS %s (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "%s TEXT)",
                TABLE_CAKE_NAMES,
                KEY_NAME);

        Log.d("CakeDBHelper", "Query to form table: " + sqlQuery);
        sqlDB.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int oldVersion, int newVersion) {
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TABLE_CAKE_NAMES);
        onCreate(sqlDB);
    }

    /* ---------------------------------------------------------------------------------------------

    --------------------------------------------------------------------------------------------- */
    public void addCakeToDb(String cakeName) {
        Log.d("CakesActivity", cakeName);

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.clear();
        values.put(CakeDBHelper.KEY_NAME, cakeName);

        long result = db.insertWithOnConflict(CakeDBHelper.TABLE_CAKE_NAMES, null, values,
                SQLiteDatabase.CONFLICT_IGNORE);
        Log.d("addCakeToDb ", String.valueOf(result));
    }

    /* ---------------------------------------------------------------------------------------------

    --------------------------------------------------------------------------------------------- */
    public Cake getCake(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_CAKE_NAMES,
                new String[]{KEY_ID, KEY_NAME},
                KEY_NAME + "=?",
                new String[]{String.valueOf(name)},
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return new Cake(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
        }
        else
            return null;
    }

    /* ---------------------------------------------------------------------------------------------

    --------------------------------------------------------------------------------------------- */
    public List<Cake> getAllCakes() {
        List<Cake> cakeList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CAKE_NAMES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Cake cake = new Cake();
                cake.setId(Integer.parseInt(cursor.getString(0)));
                cake.setName(cursor.getString(1));
                // Adding cake to list
                cakeList.add(cake);
            } while (cursor.moveToNext());
        }

        // return ingredients list
        return cakeList;
    }

    /* ---------------------------------------------------------------------------------------------

    --------------------------------------------------------------------------------------------- */
    public int updateCake(Cake cake) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, cake.getName());

        // updating row
        return db.update(TABLE_CAKE_NAMES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(cake.getId()) });
    }

    /* ---------------------------------------------------------------------------------------------

    --------------------------------------------------------------------------------------------- */
    public void deleteCake(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        try {
            db.delete(TABLE_CAKE_NAMES, KEY_NAME + " = ?",  new String[]{name});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TABLE_CAKE_NAMES, "Error while trying to delete a cake: " + name);
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    public Cake getCake(int cakeId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_CAKE_NAMES,
                new String[] { KEY_ID, KEY_NAME },
                KEY_ID + "=?",
                new String[] { String.valueOf(cakeId) },
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return new Cake(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
        }
        else
            return null;
    }

    public void clearDb() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_CAKE_NAMES,null,null);
            Log.d("CakeDBHelper", "Query to delete all from table: " + TABLE_CAKE_NAMES);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TABLE_CAKE_NAMES, "Error while trying to delete all cakes: ");
        } finally {
            db.endTransaction();
        }
    }
}