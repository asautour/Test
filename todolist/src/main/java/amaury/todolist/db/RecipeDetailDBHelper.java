
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

import amaury.todolist.data.Recipe;
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
        String sqlQuery = "CREATE TABLE " + TABLE_RECIPE_DETAILS  + "("
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
    public void addRecipeDetailToDb(RecipeDetail detail) {
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
    public List<RecipeDetail> getRecipeDetails(int recipeId) {
        List<RecipeDetail> recipeList = new ArrayList<>();

        SQLiteDatabase sqlDB = getReadableDatabase();
        //onUpgrade(sqlDB,1,3);

        String sqlQuery = String.format(
                "SELECT * FROM %s WHERE %s = %d",
                TABLE_RECIPE_DETAILS,
                KEY_RECIPE_ID,
                recipeId);

        String query = "SELECT * FROM recipe_details";
        Cursor cursor = sqlDB.rawQuery(query, null);

        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                RecipeDetail detail = new RecipeDetail();
                detail.setId(Integer.parseInt(cursor.getString(0)));
                detail.setRecipeId(Integer.parseInt(cursor.getString(1)));
                detail.setIngredientId(Integer.parseInt(cursor.getString(2)));
                detail.setQuantity(Double.parseDouble(cursor.getString(3)));
                detail.setUnit(cursor.getString(4));

                // Adding recipe to list
                recipeList.add(detail);
            }
        }

        // return ingredients list
        return recipeList;
    }

}
