package amaury.todolist;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import amaury.todolist.db.RecipeContract;
import amaury.todolist.db.RecipeDBHelper;

public class RecipesInventoryActivity extends AppCompatActivity {

    private RecipeDBHelper helper;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_recipes; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_add_recipe:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Add a recipe");
                builder.setMessage("Please enter the recipe's name");
                final EditText inputField = new EditText(this);
                builder.setView(inputField);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String recipe = inputField.getText().toString();
                        Log.d("RecipesInventoryAct", recipe);

                        RecipeDBHelper helper = new RecipeDBHelper(RecipesInventoryActivity.this);
                        SQLiteDatabase db = helper.getWritableDatabase();
                        ContentValues values = new ContentValues();

                        values.clear();
                        values.put(RecipeContract.Columns.RECIPE,recipe);

                        db.insertWithOnConflict(RecipeContract.TABLE, null, values,
                                SQLiteDatabase.CONFLICT_IGNORE);

                        updateUI();
                    }
                });

                builder.setNegativeButton("Cancel",null);

                builder.create().show();
                return true;

            default:
                return false;
        }
    }

    private void updateUI() {
        helper = new RecipeDBHelper(RecipesInventoryActivity.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(RecipeContract.TABLE,
                new String[]{RecipeContract.Columns._ID, RecipeContract.Columns.RECIPE},
                null,null,null,null,null);

        listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.view_recipes,
                cursor,
                new String[] { RecipeContract.Columns.RECIPE},
                new int[] { R.id.recipeTextView},
                0
        );

        //this.setListAdapter(listAdapter);
        // Display the list view
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(listAdapter);

    }

    public void onDoneButtonClick(View view) {
        View v = (View) view.getParent();
        TextView recipeTextView = (TextView) v.findViewById(R.id.recipeTextView);
        String recipe = recipeTextView.getText().toString();

        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                RecipeContract.TABLE,
                RecipeContract.Columns.RECIPE,
                recipe);


        helper = new RecipeDBHelper(RecipesInventoryActivity.this);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);
        updateUI();
    }
}
