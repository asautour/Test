package amaury.todolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class IngredientDBHelper extends SQLiteOpenHelper {

    public IngredientDBHelper(Context context) {
        super(context, IngredientContract.DB_NAME, null, IngredientContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String sqlQuery =
                String.format("CREATE TABLE %s (" +
                                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "%s TEXT)", IngredientContract.TABLE,
                        IngredientContract.Columns.INGREDIENT);

        Log.d("IngredientDBHelper","Query to form table: "+sqlQuery);
        sqlDB.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
        sqlDB.execSQL("DROP TABLE IF EXISTS "+ IngredientContract.TABLE);
        onCreate(sqlDB);
    }
}