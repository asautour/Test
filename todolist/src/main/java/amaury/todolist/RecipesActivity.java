package amaury.todolist;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import amaury.todolist.data.RecipeDetail;
import amaury.todolist.db.RecipeDBHelper;

public class RecipesActivity extends AppCompatActivity {

    private RecipeDBHelper helper;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_list);
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
                showPopupAdd();
                return true;

            default:
                return false;
        }
    }

    private void showPopupAdd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a recipe");
        final EditText inputField = new EditText(this);
        builder.setView(inputField);

        // when click on "Add", add ingredient to the RECIPE_NAMES table
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                addRecipeToDb(inputField);
                updateUI();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    private void addRecipeToDb(EditText inputField) {
        String recipeName = inputField.getText().toString();
        Log.d("RecipesActivity", recipeName);

        RecipeDBHelper helper = new RecipeDBHelper(RecipesActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.clear();
        values.put(RecipeDBHelper.KEY_NAME, recipeName);

        db.insertWithOnConflict(RecipeDBHelper.TABLE_RECIPE_NAMES, null, values,
                SQLiteDatabase.CONFLICT_IGNORE);
    }

    private void updateUI() {
        helper = new RecipeDBHelper(RecipesActivity.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(RecipeDBHelper.TABLE_RECIPE_NAMES,
                new String[]{RecipeDBHelper.KEY_ID, RecipeDBHelper.KEY_NAME},
                null,null,null,null,null);

        listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.view_recipes,
                cursor,
                new String[]{RecipeDBHelper.KEY_NAME},
                new int[]{R.id.recipeTextView},
                0
        );

        //this.setListAdapter(listAdapter);
        // Display the list view
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(listAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub

                Log.v("long clicked", "pos: " + pos);

                return true;
            }
        });

    }

    public void onRemoveRecipeClick(View view) {
        View v = (View) view.getParent();
        TextView textView = (TextView) v.findViewById(R.id.recipeTextView);
        helper.deleteRecipe(textView.getText().toString());
        updateUI();
    }

    public void onRecipeClick(View view) {
        View v = (View) view.getParent();
        TextView textView = (TextView) v.findViewById(R.id.recipeTextView);
        String dbEntry = textView.getText().toString();

        Intent intent = new Intent(getApplicationContext(), RecipeDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(RecipeDetail.RECIPE_EXTRA_NAME, dbEntry);
        bundle.putStringArrayList(RecipeDetail.RECIPE_EXTRA_INGREDIENTS, new ArrayList<String>());
        intent.putExtras(bundle);
        startActivity(intent);
    }
}