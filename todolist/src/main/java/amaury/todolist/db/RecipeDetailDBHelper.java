
package amaury.todolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/*
    Defines all methods related to the "recipe_details" table in database.
    The structure of the recipe_details table is:
        ID              unique ID of the entry
        recipeID        unique recipe ID referring back to the recipe table
        ingredientID    unique ingredient ID referring back to the ingredient table
        quantity        quantity of ingredient
        unit            unit in which the quantity is expressed (grams, ounces, full count etc)
 */
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
                + KEY_RECIPE_ID         + " TEXT, "
                + KEY_INGREDIENT_ID     + " TEXT, "
                + KEY_QUANTITY          + " DOUBLE, "
                + KEY_UNIT              + " TEXT )";

        Log.d("RecipeDetailDBHelper", "Query to form table: " + sqlQuery);
        sqlDB.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int oldVersion, int newVersion) {
        sqlDB.execSQL("DROP TABLE IF EXISTS "+ TABLE_RECIPE_DETAILS);
        onCreate(sqlDB);
    }
}
