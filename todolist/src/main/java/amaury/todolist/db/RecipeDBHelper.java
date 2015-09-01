
package amaury.todolist.db;

     import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;

public class RecipeDBHelper extends SQLiteOpenHelper {

    public RecipeDBHelper(Context context) {
        super(context, RecipeContract.DB_NAME, null, RecipeContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String sqlQuery =
                String.format("CREATE TABLE %s (" +
                                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "%s TEXT)", RecipeContract.TABLE,
                        RecipeContract.Columns.RECIPE);

        Log.d("RecipeDBHelper", "Query to form table: " + sqlQuery);
        sqlDB.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
        sqlDB.execSQL("DROP TABLE IF EXISTS "+ RecipeContract.TABLE);
        onCreate(sqlDB);
    }
}