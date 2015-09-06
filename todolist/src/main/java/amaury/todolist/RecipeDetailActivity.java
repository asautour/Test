package amaury.todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import amaury.todolist.data.Ingredient;
import amaury.todolist.data.Recipe;
import amaury.todolist.data.RecipeContent;
import amaury.todolist.data.RecipeDetail;
import amaury.todolist.db.RecipeDBHelper;
import amaury.todolist.db.RecipeDetailDBHelper;
import amaury.todolist.utils.RecipeDetailArrayAdapter;
import amaury.todolist.utils.UnitUtils;

public class RecipeDetailActivity extends AppCompatActivity {
    private RecipeContent recipeContent;
    private int recipeId;
    private static RecipeDetailDBHelper helperDetail;
    private static RecipeDBHelper helper;
    private RecipeDetailArrayAdapter listAdapter;
    private List<RecipeDetail> listDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_list);

        Intent intent = getIntent();
        this.recipeId = intent.getIntExtra(RecipeDetailDBHelper.KEY_RECIPE_ID, 1);

        helper = RecipeDBHelper.getInstance(RecipeDetailActivity.this);
        helperDetail = RecipeDetailDBHelper.getInstance(RecipeDetailActivity.this);

        //setContentView(R.layout.view_recipe_detail);
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
        switch (item.getItemId()) {
            case R.id.action_add_ingredient_to_recipe:
                showPopupAdd();
                return true;
            default:
                return false;
        }
    }

    /* ---------------------------------------------------------------------------------------------

    --------------------------------------------------------------------------------------------- */
    private void showPopupAdd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select an ingredient to add");
        final EditText inputField = new EditText(this);
        builder.setView(inputField);

        // when click on "Add", add ingredient to the RECIPE_NAMES table
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RecipeDetail detail = new RecipeDetail(recipeId,1,1, UnitUtils.UNIT_GRAM);
                helperDetail.addRecipeDetailToDb(detail);
                updateUI();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    /* ---------------------------------------------------------------------------------------------

    --------------------------------------------------------------------------------------------- */
    private void updateUI() {
        listDetails = helperDetail.getRecipeDetails(recipeId);

        String[] tableColumns = new String[] {
            RecipeDetailDBHelper.KEY_ID,
            RecipeDetailDBHelper.KEY_INGREDIENT_ID,
            RecipeDetailDBHelper.KEY_QUANTITY,
            RecipeDetailDBHelper.KEY_UNIT
        };
        String whereClause = RecipeDetailDBHelper.KEY_RECIPE_ID + " = ?";
        String[] whereArgs = new String[] { String.valueOf(recipeId) };

        SQLiteDatabase sqlDB = helperDetail.getReadableDatabase();
        //helperDetail.onUpgrade(sqlDB,1,3);
        Cursor cursor = sqlDB.query(RecipeDetailDBHelper.TABLE_RECIPE_DETAILS,
                tableColumns, whereClause, whereArgs, null,null,null);

        ListAdapter listAdapter2 = new SimpleCursorAdapter(
                this,
                R.layout.view_recipe_detail,
                cursor,
                // map the following columns in the recipe_details table to...
                new String[] {
                        RecipeDetailDBHelper.KEY_INGREDIENT_ID,
                        RecipeDetailDBHelper.KEY_QUANTITY // + RecipeDetailDBHelper.KEY_UNIT
                },
                // the following view fields in view_recipe_detail.xml
                new int[] { R.id.textRecipeIngredient, R.id.textIngredientQty },
                0){};

        /*listAdapter = new RecipeDetailArrayAdapter(
                this,
                listDetails);*/

        // Display the list view
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(listAdapter2);
    }
}
