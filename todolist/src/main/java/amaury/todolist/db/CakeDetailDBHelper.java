
package amaury.todolist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;

import amaury.todolist.data.CakeDetail;
import amaury.todolist.data.RecipeDetail;

/* *************************************************************************************************
    Defines all methods related to the "recipe_details" table in database.
    The structure of the recipe_details table is:
        ID              unique ID of the entry
        recipeID        unique recipe ID referring back to the recipe table
        ingredientID    unique ingredient ID referring back to the ingredient table
        quantity        quantity of ingredient
        unit            unit in which the quantity is expressed (grams, ounces, full count etc)
************************************************************************************************* */
public class CakeDetailDBHelper extends SQLiteOpenHelper {
    public static final String TABLE_CAKE_DETAILS   = "cake_details";
    public static final String KEY_ID               = BaseColumns._ID;
    public static final String KEY_CAKE_ID          = "cakeID";
    public static final String KEY_RECIPE_ID        = "recipeID";
    public static final String KEY_QUANTITY         = "quantity";

    private static CakeDetailDBHelper sInstance;

    public static synchronized CakeDetailDBHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null)
            sInstance = new CakeDetailDBHelper(context.getApplicationContext());
        return sInstance;
    }

    public CakeDetailDBHelper(Context context) {
        super(context, DBUtils.DATABASE_NAME, null, DBUtils.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String sqlQuery = "CREATE TABLE " + TABLE_CAKE_DETAILS + "("
                + KEY_ID                + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + KEY_CAKE_ID + " INTEGER, "
                + KEY_RECIPE_ID + " INTEGER, "
                + KEY_QUANTITY          + " DOUBLE )";

        Log.d("CakeDetailDBHelper", "Query to form table: " + sqlQuery);
        sqlDB.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int oldVersion, int newVersion) {
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TABLE_CAKE_DETAILS);
        onCreate(sqlDB);
    }

    /* ---------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------- */
    public int updateDetail(CakeDetail detail) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CAKE_ID, detail.getRecipeId());
        values.put(KEY_RECIPE_ID, detail.getRecipeId());
        values.put(KEY_QUANTITY, detail.getQuantity());

        // updating row
        return db.update(TABLE_CAKE_DETAILS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(detail.getId()) });
    }

    public void addCakeDetailToDb(CakeDetail detail, Boolean bCheckExist) {
        if ( bCheckExist ) {
            // select * from
            SQLiteDatabase sqlDB = getReadableDatabase();
            String sqlQuery = String.format(
                    "SELECT * FROM %s WHERE %s = %d and %s = %d",
                    TABLE_CAKE_DETAILS,
                    KEY_CAKE_ID,
                    detail.getCakeId(),
                    KEY_RECIPE_ID,
                    detail.getRecipeId());

            Cursor cursor = sqlDB.rawQuery(sqlQuery, null);

            if (cursor == null || !cursor.moveToFirst()) {
                addCakeDetailToDb(detail);
            }

        }
        else
            addCakeDetailToDb(detail);
    }

    /* ---------------------------------------------------------------------------------------------

    --------------------------------------------------------------------------------------------- */
    private void addCakeDetailToDb(CakeDetail detail) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.clear();

            values.put(CakeDetailDBHelper.KEY_CAKE_ID, detail.getRecipeId());
            values.put(CakeDetailDBHelper.KEY_RECIPE_ID, detail.getRecipeId());
            values.put(CakeDetailDBHelper.KEY_QUANTITY, detail.getQuantity());

            long result = db.insertWithOnConflict(CakeDetailDBHelper.TABLE_CAKE_DETAILS, null, values,
                       SQLiteDatabase.CONFLICT_IGNORE);
            db.setTransactionSuccessful();
            Log.d("addCakeDetailToDb ", String.valueOf(result));
        } catch (Exception e) {
            Log.d(TABLE_CAKE_DETAILS, "Error while trying to add cake details to database");
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    /* ---------------------------------------------------------------------------------------------

    --------------------------------------------------------------------------------------------- */
    public ArrayList<CakeDetail> getCakeDetails(int cakeId) {
        ArrayList<CakeDetail> details = new ArrayList<>();

        SQLiteDatabase sqlDB = getReadableDatabase();

        String sqlQuery = String.format(
                "SELECT * FROM %s WHERE %s = %d",
                TABLE_CAKE_DETAILS,
                KEY_CAKE_ID,
                cakeId);

        Cursor cursor = sqlDB.rawQuery(sqlQuery, null);

        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                CakeDetail detail = new CakeDetail();
                detail.setCakeId(Integer.parseInt(cursor.getString(0)));
                detail.setRecipeId(Integer.parseInt(cursor.getString(1)));
                detail.setQuantity(Double.parseDouble(cursor.getString(2)));

                // Adding recipe to list
                details.add(detail);

            } while (cursor.moveToNext());
        }

        // return ingredients list
        return details;
    }

    /* ---------------------------------------------------------------------------------------------

    --------------------------------------------------------------------------------------------- */
    public void removeDetailFromDb(int recipeId, int ingredientId) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            db.delete(TABLE_CAKE_DETAILS,
                    KEY_CAKE_ID + "=? and " + KEY_RECIPE_ID + "=?",
                    new String[]{String.valueOf(recipeId),String.valueOf(ingredientId)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TABLE_CAKE_DETAILS,
                    "Error while trying to remove recipe details to database - recipeId " +
                    recipeId +
                    ", ingredientId " +
                    ingredientId);
        } finally {
            db.endTransaction();
        }
        db.close();
    }
}
