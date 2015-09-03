package amaury.todolist.db;

import android.provider.BaseColumns;

public class IngredientTable {
    public static final String DB_NAME = "amaury.todolist.db.ingredients";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "ingredients";

    public class Columns {
        public static final String INGREDIENT = "ingredient";
        public static final String _ID = BaseColumns._ID;
    }
}