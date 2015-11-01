package amaury.todolist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by su on 04/09/2015.
 */
public class DBUtils {

    public static final String DATABASE_NAME = "recipeManager";
    public static final int DATABASE_VERSION = 3;
    public static final boolean DATABASE_RESET = false;

    public static void initiateDb(Context context) {
        IngredientDBHelper helperIngredient = IngredientDBHelper.getInstance(context);
        SQLiteDatabase sqlDB = helperIngredient.getWritableDatabase();
        helperIngredient.onUpgrade(sqlDB, 1, DATABASE_VERSION);

        RecipeDBHelper helperRecipe = RecipeDBHelper.getInstance(context);
        sqlDB = helperRecipe.getWritableDatabase();
        helperRecipe.onUpgrade(sqlDB, 1, DATABASE_VERSION);

        RecipeDetailDBHelper helperDetail = RecipeDetailDBHelper.getInstance(context);
        sqlDB = helperDetail.getWritableDatabase();
        helperDetail.onUpgrade(sqlDB,1,DATABASE_VERSION);

        CakeDBHelper helperCake = CakeDBHelper.getInstance(context);
        sqlDB = helperCake.getWritableDatabase();
        helperCake.onUpgrade(sqlDB,1,DATABASE_VERSION);

        CakeDetailDBHelper helperCakeDetail = CakeDetailDBHelper.getInstance(context);
        sqlDB = helperCakeDetail.getWritableDatabase();
        helperCakeDetail.onUpgrade(sqlDB,1,DATABASE_VERSION);
    }

    /* returns an ingredient name from the INGREDIENT table using its unique ID */
    public void getIngredientName(IngredientDBHelper ingredientsDBHelper, int ingredientId) {

    }

    public void getRecipeName(RecipeDBHelper recipeDBHelper, int recipeId) {

    }

    public void getRecipeIngredients(RecipeDetailDBHelper recipeDetailDBHelper, int recipeId) {

    }
}
