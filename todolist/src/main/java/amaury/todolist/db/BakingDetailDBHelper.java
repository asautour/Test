
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

import amaury.todolist.data.BakingDetail;

/* *************************************************************************************************
    Defines all methods related to the "recipe_details" table in database.
    The structure of the recipe_details table is:
        ID              unique ID of the entry
        cakeID          unique recipe ID referring back to the cake table
        quantity        quantity of ingredient
        unit            unit in which the quantity is expressed (grams, ounces, full count etc)
************************************************************************************************* */
public class BakingDetailDBHelper extends SQLiteOpenHelper {
    public static final String TABLE_BAKING_DETAILS = "baking_details";
    public static final String KEY_ID               = BaseColumns._ID;
    public static final String KEY_CAKE_ID          = "cakeID";
    public static final String KEY_QUANTITY         = "quantity";

    private static BakingDetailDBHelper sInstance;

    public static synchronized BakingDetailDBHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null)
            sInstance = new BakingDetailDBHelper(context.getApplicationContext());
        return sInstance;
    }

    public BakingDetailDBHelper(Context context) {
        super(context, DBUtils.DATABASE_NAME, null, DBUtils.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String sqlQuery = "CREATE TABLE " + TABLE_BAKING_DETAILS + "("
                + KEY_ID        + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + KEY_CAKE_ID   + " INTEGER, "
                + KEY_QUANTITY  + " INTEGER )";

        Log.d("BakingDetailDBHelper", "Query to form table: " + sqlQuery);
        sqlDB.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int oldVersion, int newVersion) {
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TABLE_BAKING_DETAILS);
        onCreate(sqlDB);
    }

    /* ---------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------- */
    public int updateDetail(BakingDetail detail) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CAKE_ID, detail.getCakeId());
        values.put(KEY_QUANTITY, detail.getQuantity());

        // updating row
        int result = db.update(TABLE_BAKING_DETAILS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(detail.getId()) });
        Log.d("CakeDetailDBHelper updateDetail ", String.valueOf(result));

        return result;
    }

    // TODO fix this
    public void addDetailToDb(BakingDetail detail, Boolean bCheckExist) {
        if ( bCheckExist ) {
            // select * from
            SQLiteDatabase sqlDB = getReadableDatabase();
            String sqlQuery = String.format(
                    "SELECT * FROM %s WHERE %s = %d",
                    TABLE_BAKING_DETAILS,
                    KEY_CAKE_ID,
                    detail.getCakeId());

            Cursor cursor = sqlDB.rawQuery(sqlQuery, null);

            if (cursor == null || !cursor.moveToFirst()) {
                addDetailToDb(detail);
            }

        }
        else
            addDetailToDb(detail);
    }

    /* ---------------------------------------------------------------------------------------------

    --------------------------------------------------------------------------------------------- */
    private void addDetailToDb(BakingDetail detail) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.clear();

            values.put(BakingDetailDBHelper.KEY_CAKE_ID, detail.getCakeId());
            values.put(BakingDetailDBHelper.KEY_QUANTITY, detail.getQuantity());

            long result = db.insertWithOnConflict(BakingDetailDBHelper.TABLE_BAKING_DETAILS, null, values,
                       SQLiteDatabase.CONFLICT_IGNORE);
            db.setTransactionSuccessful();
            Log.d("addCakeDetailToDb ", String.valueOf(result));
        } catch (Exception e) {
            Log.d(TABLE_BAKING_DETAILS, "Error while trying to add baking details to database");
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    /* ---------------------------------------------------------------------------------------------

    --------------------------------------------------------------------------------------------- */
    public ArrayList<BakingDetail> getBakingDetails() {
        ArrayList<BakingDetail> details = new ArrayList<>();
        SQLiteDatabase sqlDB = getReadableDatabase();
        String sqlQuery = "SELECT * FROM " + TABLE_BAKING_DETAILS;
        Cursor cursor = sqlDB.rawQuery(sqlQuery, null);

        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                BakingDetail detail = new BakingDetail();
                detail.setId(Integer.parseInt(cursor.getString(0)));
                detail.setCakeId(Integer.parseInt(cursor.getString(1)));
                detail.setQuantity(Integer.parseInt(cursor.getString(2)));

                // Adding recipe to list
                details.add(detail);

            } while (cursor.moveToNext());
        }

        // return ingredients list
        return details;
    }

    /* ---------------------------------------------------------------------------------------------

    --------------------------------------------------------------------------------------------- */
    public void removeDetailFromDb(int cakeId) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            db.delete(TABLE_BAKING_DETAILS,
                    KEY_CAKE_ID + "=?" ,
                    new String[]{String.valueOf(cakeId)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TABLE_BAKING_DETAILS,
                    "Error while trying to remove baking details to database - cakeId " +
                    cakeId);
        } finally {
            db.endTransaction();
        }
        db.close();
    }
}
