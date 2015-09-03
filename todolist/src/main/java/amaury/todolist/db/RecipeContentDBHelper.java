
package amaury.todolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RecipeContentDBHelper extends SQLiteOpenHelper {

    public RecipeContentDBHelper(Context context) {
        super(context, RecipeContentTable.DB_NAME, null, RecipeContentTable.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String sqlQuery = "CREATE TABLE " + RecipeContentTable.TABLE  + "("
                + RecipeContentTable.Columns._ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + RecipeContentTable.Columns.RECIPE_ID + " TEXT, "
                + RecipeContentTable.Columns.INGREDIENT_ID + " TEXT, "
                + RecipeContentTable.Columns.INGREDIENT_WEIGHT + " DOUBLE )";

        Log.d("RecipeContentDBHelper", "Query to form table: " + sqlQuery);
        sqlDB.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
        sqlDB.execSQL("DROP TABLE IF EXISTS "+ RecipeContentTable.TABLE);
        onCreate(sqlDB);
    }
}
