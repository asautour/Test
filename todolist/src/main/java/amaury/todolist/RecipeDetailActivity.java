package amaury.todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import amaury.todolist.data.Ingredient;
import amaury.todolist.data.RecipeDetail;
import amaury.todolist.db.IngredientDBHelper;
import amaury.todolist.db.RecipeDBHelper;
import amaury.todolist.db.RecipeDetailDBHelper;
import amaury.todolist.utils.RecipeDetailArrayAdapter;
import amaury.todolist.utils.UiUtils;

public class RecipeDetailActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private int recipeId;
    private static RecipeDBHelper helper;
    private static RecipeDetailDBHelper helperDetail;
    private static IngredientDBHelper helperIngredient;

    private ArrayList<RecipeDetail> listDetails;
    private EditText quantityField;
    private double totalQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_list);

        Intent intent = getIntent();
        setTitle(intent.getStringExtra(UiUtils.NAME));
        this.recipeId = intent.getIntExtra(RecipeDetailDBHelper.KEY_RECIPE_ID, 1);

        // if the screen is launched with a quantity in parameter, it means we are coming from the
        // cake screen, i.e. targeting a total recipe weight
        this.totalQuantity = intent.getDoubleExtra(UiUtils.QUANTITY,1);

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
        builder.setTitle(UiUtils.TITLE_ACTIVITY_RECIPE_DETAILS);

        builder.setPositiveButton(UiUtils.OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                for (int i=0; i<bool.length; i++) {
                    if ( bool[i] ) {
                        // add ingredient to the recipe
                        RecipeDetail detail = new RecipeDetail(recipeId,listIngredients.get(i).getId(),0, null);
                        helperDetail.addRecipeDetailToDb(detail, true);
                    }
                    else
                        helperDetail.removeRecipeDetailFromDb(recipeId, listIngredients.get(i).getId());
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
        ListAdapter adapter = new RecipeDetailArrayAdapter(this, 0, listDetails, getRatio());

        // Display the list view
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RecipeDetail detail = listDetails.get(position);
    }

    /* ---------------------------------------------------------------------------------------------
        Returns the recipe's base weight, as entered by the user
    --------------------------------------------------------------------------------------------- */
    public double getBaseWeight() {
        double weight = 0;

        for (int i=0; i<listDetails.size(); i++) {
            RecipeDetail detail = listDetails.get(i);
            weight += detail.getQuantity();
        }

        return Math.max(1, weight);
    }

    /* ---------------------------------------------------------------------------------------------
        This will be used to adjust the ingredients quantities for the total target weight, and
        is simply a ratio from the total target weight and the recipe's base weight
    --------------------------------------------------------------------------------------------- */
    private double getRatio() {
        return totalQuantity / getBaseWeight();
    }
}
