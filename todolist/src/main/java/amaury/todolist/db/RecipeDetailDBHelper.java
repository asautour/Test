
package amaury.todolist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
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
public class RecipeDetailDBHelper extends SQLiteOpenHelper {
    public static final String TABLE_RECIPE_DETAILS = "recipe_details";
    public static final String KEY_ID               = BaseColumns._ID;
    public static final String KEY_RECIPE_ID        = "recipeID";
    public static final String KEY_INGREDIENT_ID    = "ingredientID";
    public static final String KEY_QUANTITY         = "quantity";
    public static final String KEY_UNIT             = "unit";

    private static RecipeDetailDBHelper sInstance;

    public static synchronized RecipeDetailDBHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null)
            sInstance = new RecipeDetailDBHelper(context.getApplicationContext());
        return sInstance;
    }

    public RecipeDetailDBHelper(Context context) {
        super(context, DBUtils.DATABASE_NAME, null, DBUtils.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String sqlQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_RECIPE_DETAILS  + "("
                + KEY_ID                + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + KEY_RECIPE_ID         + " INTEGER, "
                + KEY_INGREDIENT_ID     + " INTEGER, "
                + KEY_QUANTITY          + " DOUBLE, "
                + KEY_UNIT              + " TEXT )";

        Log.d("RecipeDetailDBHelper", "Query to form table: " + sqlQuery);
        sqlDB.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int oldVersion, int newVersion) {
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_DETAILS);
        onCreate(sqlDB);
    }

    /* ---------------------------------------------------------------------------------------------

--------------------------------------------------------------------------------------------- */
    public int updateDetail(RecipeDetail detail) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RECIPE_ID, detail.getRecipeId());
        values.put(KEY_INGREDIENT_ID, detail.getIngredientId());
        values.put(KEY_QUANTITY, detail.getQuantity());
        values.put(KEY_UNIT, detail.getUnit());

        // updating row
        return db.update(TABLE_RECIPE_DETAILS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(detail.getId()) });
    }

    public void addRecipeDetailToDb(RecipeDetail detail, Boolean bCheckExist) {
        if ( bCheckExist ) {
            // select * from
            SQLiteDatabase sqlDB = getReadableDatabase();
            String sqlQuery = String.format(
                    "SELECT * FROM %s WHERE %s = %d and %s = %d",
                    TABLE_RECIPE_DETAILS,
                    KEY_RECIPE_ID,
                    detail.getRecipeId(),
                    KEY_INGREDIENT_ID,
                    detail.getIngredientId());

            Cursor cursor = sqlDB.rawQuery(sqlQuery, null);

            if (cursor == null || !cursor.moveToFirst()) {
                addRecipeDetailToDb(detail);
            }

        }
        else
            addRecipeDetailToDb(detail);
    }

    /* ---------------------------------------------------------------------------------------------

    --------------------------------------------------------------------------------------------- */
    private void addRecipeDetailToDb(RecipeDetail detail) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.clear();

            values.put(RecipeDetailDBHelper.KEY_RECIPE_ID, detail.getRecipeId());
            values.put(RecipeDetailDBHelper.KEY_INGREDIENT_ID, detail.getIngredientId());
            values.put(RecipeDetailDBHelper.KEY_QUANTITY, detail.getQuantity());
            values.put(RecipeDetailDBHelper.KEY_UNIT, detail.getUnit());

            long result = db.insertWithOnConflict(RecipeDetailDBHelper.TABLE_RECIPE_DETAILS, null, values,
                       SQLiteDatabase.CONFLICT_IGNORE);
            db.setTransactionSuccessful();
            Log.d("addRecipeDetailToDb ", String.valueOf(result));
        } catch (Exception e) {
            Log.d(TABLE_RECIPE_DETAILS, "Error while trying to add recipe details to database");
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    /* ---------------------------------------------------------------------------------------------

    --------------------------------------------------------------------------------------------- */
    public ArrayList<RecipeDetail> getRecipeDetails(int recipeId) {
        ArrayList<RecipeDetail> recipeList = new ArrayList<>();

        SQLiteDatabase sqlDB = getReadableDatabase();

        String sqlQuery = String.format(
                "SELECT * FROM %s WHERE %s = %d",
                TABLE_RECIPE_DETAILS,
                KEY_RECIPE_ID,
                recipeId);

        Cursor cursor = sqlDB.rawQuery(sqlQuery, null);

        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                RecipeDetail detail = new RecipeDetail();
                detail.setId(Integer.parseInt(cursor.getString(0)));
                detail.setRecipeId(Integer.parseInt(cursor.getString(1)));
                detail.setIngredientId(Integer.parseInt(cursor.getString(2)));
                detail.setQuantity(Double.parseDouble(cursor.getString(3)));
                detail.setUnit(cursor.getString(4));

                // Adding recipe to list
                recipeList.add(detail);

            } while (cursor.moveToNext());
        }

        // return ingredients list
        return recipeList;
    }

    /* ---------------------------------------------------------------------------------------------

    --------------------------------------------------------------------------------------------- */
    public void removeRecipeDetailFromDb(int recipeId, int ingredientId) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            db.delete(TABLE_RECIPE_DETAILS,
                    KEY_RECIPE_ID + "=? and " + KEY_INGREDIENT_ID + "=?",
                    new String[]{String.valueOf(recipeId),String.valueOf(ingredientId)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TABLE_RECIPE_DETAILS,
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
