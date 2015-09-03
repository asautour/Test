package amaury.todolist.db;

import android.provider.BaseColumns;

public class RecipeContentTable {
    public static final String DB_NAME = "amaury.todolist.db.recipe_content";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "recipe_content";

    public class Columns {
        public static final String _ID = BaseColumns._ID;
        public static final String RECIPE_ID = "recipeID";
        public static final String INGREDIENT_ID = "ingredientID";
        public static final String INGREDIENT_WEIGHT = "weight";
    }
}