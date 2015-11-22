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

import amaury.todolist.data.Ingredient;

public class IngredientDBHelper extends SQLiteOpenHelper {
    public static final String TABLE_INGREDIENTS    = "ingredients";
    public static final String KEY_ID               = BaseColumns._ID;
    public static final String KEY_NAME             = "ingredient";

    private static IngredientDBHelper sInstance;

    public static synchronized IngredientDBHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null)
            sInstance = new IngredientDBHelper(context.getApplicationContext());
        return sInstance;
    }

    public IngredientDBHelper(Context context) {
        super(context, DBUtils.DATABASE_NAME, null, DBUtils.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String sqlQuery = String.format(
                "CREATE TABLE %s (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "%s TEXT)",
                TABLE_INGREDIENTS,
                KEY_NAME);

        Log.d("IngredientDBHelper", "Query to form table: " + sqlQuery);
        sqlDB.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int oldVersion, int newVersion) {
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        onCreate(sqlDB);
    }

    /* ---------------------------------------------------------------------------------------------

    --------------------------------------------------------------------------------------------- */
    void addIngredient(Ingredient ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, ingredient.getName()); // Contact Name

            // Inserting Row
            db.insert(TABLE_INGREDIENTS, null, values);
            db.close(); // Closing database connection
        } catch (Exception e) {
            Log.d(TABLE_INGREDIENTS, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    /* ---------------------------------------------------------------------------------------------

    --------------------------------------------------------------------------------------------- */
    public void addIngredientToDb(String ingredientName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.clear();
        values.put(KEY_NAME, ingredientName);

        long result = db.insertWithOnConflict(TABLE_INGREDIENTS, null, values,
                SQLiteDatabase.CONFLICT_IGNORE);
        Log.d("addIngredientToDb ", String.valueOf(result));
    }

    /* ---------------------------------------------------------------------------------------------
        Add ingredients from a list
    --------------------------------------------------------------------------------------------- */
    public void addIngredientsToDb(List<String> ingredientNames) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.beginTransaction();
        try {
            for (int i=0; i<ingredientNames.size(); i++) {
                values.clear();
                values.put(KEY_NAME, ingredientNames.get(i));

                long result = db.insertWithOnConflict(TABLE_INGREDIENTS, null, values,
                        SQLiteDatabase.CONFLICT_IGNORE);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TABLE_INGREDIENTS, "Error while trying to add default ingredients");
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    /* ---------------------------------------------------------------------------------------------

    --------------------------------------------------------------------------------------------- */
    public Ingredient getIngredient(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_INGREDIENTS,
                new String[] { KEY_ID, KEY_NAME },
                KEY_ID + "=?",
                new String[] { String.valueOf(id) },
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            return new Ingredient(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
        }
        else
            return null;
    }

    /* ---------------------------------------------------------------------------------------------

    --------------------------------------------------------------------------------------------- */
    public List<Ingredient> getAllIngredients() {
        List<Ingredient> ingredientList = new ArrayList<Ingredient>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_INGREDIENTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Ingredient ingredient = new Ingredient();
                ingredient.setId(Integer.parseInt(cursor.getString(0)));
                ingredient.setName(cursor.getString(1));
                // Adding ingredient to list
                ingredientList.add(ingredient);
            } while (cursor.moveToNext());
        }

        // return ingredients list
        return ingredientList;
    }

    /* ---------------------------------------------------------------------------------------------

    --------------------------------------------------------------------------------------------- */
    public int updateIngredient(Ingredient ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, ingredient.getName());

        // updating row
        return db.update(TABLE_INGREDIENTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(ingredient.getId()) });
    }

    /* ---------------------------------------------------------------------------------------------

    --------------------------------------------------------------------------------------------- */
    public void deleteIngredient(Ingredient ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        try {
            db.delete(TABLE_INGREDIENTS, KEY_ID + " = ?",
                    new String[]{String.valueOf(ingredient.getId())});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TABLE_INGREDIENTS, "Error while trying to delete an ingredient");
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    /* ---------------------------------------------------------------------------------------------

    --------------------------------------------------------------------------------------------- */
    public void deleteIngredient(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        try {
            db.delete(TABLE_INGREDIENTS, KEY_NAME + " = ?",  new String[] { name });
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TABLE_INGREDIENTS, "Error while trying to delete an ingredient: " + name);
        } finally {
            db.endTransaction();
        }
    }

    public void deleteAllIngredients() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_INGREDIENTS,null,null);
            Log.d("IngredientDBHelper", "Query to delete all from table: " + TABLE_INGREDIENTS);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TABLE_INGREDIENTS, "Error while trying to delete all ingredients: ");
        } finally {
            db.endTransaction();
        }
    }
}