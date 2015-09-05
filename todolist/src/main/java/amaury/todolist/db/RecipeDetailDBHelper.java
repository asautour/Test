
package amaury.todolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class RecipeDetailDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "recipeManager";
    public static final String TABLE_RECIPE_DETAILS = "recipe_details";

    public static final String KEY_ID = BaseColumns._ID;
    public static final String KEY_RECIPE_ID = "recipeID";
    public static final String KEY_INGREDIENT_ID = "ingredientID";
    public static final String KEY_QUANTITY = "quantity";
    public static final String KEY_UNIT = "unit";

    public RecipeDetailDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DBUtils.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String sqlQuery = "CREATE TABLE " + TABLE_RECIPE_DETAILS  + "("
                + KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + KEY_RECIPE_ID + " TEXT, "
                + KEY_INGREDIENT_ID + " TEXT, "
                + KEY_QUANTITY + " DOUBLE, "
                + KEY_UNIT + " TEXT )";

        Log.d("RecipeDetailDBHelper", "Query to form table: " + sqlQuery);
        sqlDB.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int oldVersion, int newVersion) {
        sqlDB.execSQL("DROP TABLE IF EXISTS "+ TABLE_RECIPE_DETAILS);
        onCreate(sqlDB);
    }
}
