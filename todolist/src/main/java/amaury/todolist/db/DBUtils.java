package amaury.todolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by su on 04/09/2015.
 */
public class DBUtils {

    public static final String DATABASE_NAME = "recipeManager";
    public static final int DATABASE_VERSION = 2;
    public static final boolean DATABASE_RESET = true;

    public static void initiateDb(Context context) {
        IngredientDBHelper helperIngredient = IngredientDBHelper.getInstance(context);
        SQLiteDatabase sqlDB = helperIngredient.getWritableDatabase();
        helperIngredient.onCreate(sqlDB);

        RecipeDBHelper helperRecipe = RecipeDBHelper.getInstance(context);
        sqlDB = helperRecipe.getWritableDatabase();
        helperRecipe.onCreate(sqlDB);

        RecipeDetailDBHelper helperDetail = RecipeDetailDBHelper.getInstance(context);
        sqlDB = helperDetail.getWritableDatabase();
        helperDetail.onCreate(sqlDB);

        CakeDBHelper helperCake = CakeDBHelper.getInstance(context);
        sqlDB = helperCake.getWritableDatabase();
        helperCake.onCreate(sqlDB);

        CakeDetailDBHelper helperCakeDetail = CakeDetailDBHelper.getInstance(context);
        sqlDB = helperCakeDetail.getWritableDatabase();
        helperCakeDetail.onCreate(sqlDB);

        BakingDetailDBHelper helperBakingDetail = BakingDetailDBHelper.getInstance(context);
        sqlDB = helperBakingDetail.getWritableDatabase();
        helperBakingDetail.onCreate(sqlDB);

    }
}
