package amaury.todolist.db;

import android.provider.BaseColumns;

public class RecipeContract {
    public static final String DB_NAME = "amaury.todolist.db.recipes";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "recipes";

    public class Columns {
        public static final String RECIPE = "recipe";
        public static final String _ID = BaseColumns._ID;
    }
}