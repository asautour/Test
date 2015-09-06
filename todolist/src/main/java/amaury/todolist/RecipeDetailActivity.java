package amaury.todolist;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import amaury.todolist.data.RecipeDetail;
import amaury.todolist.db.RecipeDetailDBHelper;

public class RecipeDetailActivity extends AppCompatActivity {
    private RecipeDetail recipe;
    private RecipeDetailDBHelper helper = new RecipeDetailDBHelper(RecipeDetailActivity.this);;
    private ListAdapter listAdapter;

    public RecipeDetailActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_recipe_detail);


        recipe = new RecipeDetail(getIntent());
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_ingredients; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateUI() {
        //helper = new RecipeDetailDBHelper(RecipeDetailActivity.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(RecipeDetailDBHelper.TABLE_RECIPE_DETAILS,
                new String[]{RecipeDetailDBHelper.KEY_ID, RecipeDetailDBHelper.KEY_INGREDIENT_ID},
                null,null,null,null,null);

        listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.view_recipe_detail,
                cursor,
                new String[] { RecipeDetailDBHelper.KEY_INGREDIENT_ID, RecipeDetailDBHelper.KEY_QUANTITY},
                new int[] { R.id.textRecipeIngredient, R.id.textIngredientWeight },
                0){};

        //this.setListAdapter(listAdapter);
        // Display the list view
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(listAdapter);
    }
}
