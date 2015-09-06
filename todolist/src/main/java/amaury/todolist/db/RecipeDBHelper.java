package amaury.todolist.db;

import android.content.ContentValues;
 import android.content.Context;
 import android.database.Cursor;
 import android.database.sqlite.SQLiteDatabase;
 import android.database.sqlite.SQLiteOpenHelper;
 import android.provider.BaseColumns;
 import android.util.Log;

 import java.util.ArrayList;
 import java.util.List;

 import amaury.todolist.data.Recipe;

public class RecipeDBHelper extends SQLiteOpenHelper {
    public static final String TABLE_RECIPE_NAMES   = "recipe_names";
    public static final String KEY_ID               = BaseColumns._ID;
    public static final String KEY_NAME             = "recipe";

    private static RecipeDBHelper sInstance;

    public static synchronized RecipeDBHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null)
            sInstance = new RecipeDBHelper(context.getApplicationContext());
        return sInstance;
    }

    public RecipeDBHelper(Context context) {
        super(context, DBUtils.DATABASE_NAME, null, DBUtils.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String sqlQuery = String.format(
                "CREATE TABLE %s (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "%s TEXT)",
                TABLE_RECIPE_NAMES,
                KEY_NAME);

        Log.d("RecipeDBHelper", "Query to form table: " + sqlQuery);
        sqlDB.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int oldVersion, int newVersion) {
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_NAMES);
        onCreate(sqlDB);
    }

    void addRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, recipe.getName());

            // Inserting Row
            db.insert(TABLE_RECIPE_NAMES, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TABLE_RECIPE_NAMES, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    public Recipe getRecipe(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_RECIPE_NAMES,
                new String[] { KEY_ID, KEY_NAME },
                KEY_ID + "=?",
                new String[] { String.valueOf(id) },
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return new Recipe(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
        }
        else
            return null;
    }

    public Recipe getRecipe(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_RECIPE_NAMES,
                new String[] { KEY_ID, KEY_NAME },
                KEY_NAME + "=?",
                new String[] { String.valueOf(name) },
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return new Recipe(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
        }
        else
            return null;
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipeList = new ArrayList<Recipe>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RECIPE_NAMES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe();
                recipe.setId(Integer.parseInt(cursor.getString(0)));
                recipe.setName(cursor.getString(1));
                // Adding recipe to list
                recipeList.add(recipe);
            } while (cursor.moveToNext());
        }

        // return ingredients list
        return recipeList;
    }

    public int updateRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, recipe.getName());

        // updating row
        return db.update(TABLE_RECIPE_NAMES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(recipe.getId()) });
    }

    public void deleteRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        try {
            db.delete(TABLE_RECIPE_NAMES, KEY_ID + " = ?",
                    new String[]{String.valueOf(recipe.getId())});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TABLE_RECIPE_NAMES, "Error while trying to delete a recipe");
        } finally {
            db.endTransaction();
        }
    }

    public void deleteRecipe(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        try {
            db.delete(TABLE_RECIPE_NAMES, KEY_NAME + " = ?",  new String[]{name});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TABLE_RECIPE_NAMES, "Error while trying to delete a recipe: " + name);
        } finally {
            db.endTransaction();
        }
    }
}