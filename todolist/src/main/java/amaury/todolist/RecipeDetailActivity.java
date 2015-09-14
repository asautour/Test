package amaury.todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import amaury.todolist.data.Ingredient;
import amaury.todolist.data.RecipeContent;
import amaury.todolist.data.RecipeDetail;
import amaury.todolist.db.DBUtils;
import amaury.todolist.db.IngredientDBHelper;
import amaury.todolist.db.RecipeDBHelper;
import amaury.todolist.db.RecipeDetailDBHelper;
import amaury.todolist.utils.RecipeDetailArrayAdapter;

public class RecipeDetailActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private RecipeContent recipeContent;
    private int recipeId;
    private static RecipeDetailDBHelper helperDetail;
    private static RecipeDBHelper helper;
    private ArrayList<RecipeDetail> listDetails;

    private IngredientDBHelper helperIngredient;
    private ListAdapter listIngredientsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_list);

        Intent intent = getIntent();
        this.recipeId = intent.getIntExtra(RecipeDetailDBHelper.KEY_RECIPE_ID, 1);

        helper = RecipeDBHelper.getInstance(RecipeDetailActivity.this);
        helperDetail = RecipeDetailDBHelper.getInstance(RecipeDetailActivity.this);
        helperIngredient = IngredientDBHelper.getInstance(this);

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
        Shows a popup which allows the selection of available ingredients, as set in the
        Ingredient view.
    --------------------------------------------------------------------------------------------- */
    private void showPopupAdd() {
        // first retrieve all ingredients from the database
        final List<Ingredient> listIngredients = helperIngredient.getAllIngredients();

        // populate IDs of all ingredients already in the recipe
        Iterator<RecipeDetail> it = listDetails.iterator();
        List<Integer> listId = new ArrayList<>();
        for ( int i=0; i<listDetails.size(); i++ ) {
            listId.add(listDetails.get(i).getIngredientId());
        }

        final CharSequence[] seq = new CharSequence[listIngredients.size()];
        final boolean[] bool = new boolean[listIngredients.size()];

        for ( int i=0; i<listIngredients.size(); i++ ) {
            Ingredient ing = listIngredients.get(i);
            seq[i] = ing.getName();
            bool[i] = listId.contains(ing.getId());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                for (int i=0; i<bool.length; i++) {
                    if ( bool[i] ) {
                        // add ingredient to the recipe
                        RecipeDetail detail = new RecipeDetail(recipeId,listIngredients.get(i).getId(),0, null);
                        helperDetail.addRecipeDetailToDb(detail, true);
                    }
                }

                updateUI();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        builder.setMultiChoiceItems(seq, bool, new DialogInterface.OnMultiChoiceClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                bool[which] = isChecked;
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /* ---------------------------------------------------------------------------------------------

    --------------------------------------------------------------------------------------------- */
    private void updateUI() {
        listDetails = helperDetail.getRecipeDetails(recipeId);

        ListAdapter adapter = new RecipeDetailArrayAdapter(this, 0, listDetails);

        /*String[] tableColumns = new String[] {
            RecipeDetailDBHelper.KEY_ID,
            RecipeDetailDBHelper.KEY_INGREDIENT_ID,
            RecipeDetailDBHelper.KEY_QUANTITY,
            RecipeDetailDBHelper.KEY_UNIT
        };
        String whereClause = RecipeDetailDBHelper.KEY_RECIPE_ID + " = ?";
        String[] whereArgs = new String[] { String.valueOf(recipeId) };

        SQLiteDatabase sqlDB = helperDetail.getReadableDatabase();
        Cursor cursor = sqlDB.query(
                RecipeDetailDBHelper.TABLE_RECIPE_DETAILS,
                tableColumns, whereClause, whereArgs, null,null,null);

        ListAdapter listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.view_recipe_detail,
                cursor,
                // map the following columns in the recipe_details table to...
                new String[] {
                        helperIngredient.getIngredient(RecipeDetailDBHelper.KEY_INGREDIENT_ID).getName(),
                        RecipeDetailDBHelper.KEY_QUANTITY // + RecipeDetailDBHelper.KEY_UNIT
                },
                // the following view fields in view_recipe_detail.xml
                new int[] { R.id.textRecipeIngredient, R.id.textIngredientQty },
                0){};*/

        // Display the list view
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
}
