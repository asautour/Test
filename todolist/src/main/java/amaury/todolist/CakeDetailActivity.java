package amaury.todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import amaury.todolist.data.CakeDetail;
import amaury.todolist.data.Recipe;
import amaury.todolist.data.RecipeDetail;
import amaury.todolist.db.CakeDBHelper;
import amaury.todolist.db.CakeDetailDBHelper;
import amaury.todolist.db.RecipeDBHelper;
import amaury.todolist.utils.CakeDetailArrayAdapter;
import amaury.todolist.utils.RecipeDetailArrayAdapter;
import amaury.todolist.utils.UiUtils;

public class CakeDetailActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private int cakeId;
    private static CakeDBHelper helper;
    private static CakeDetailDBHelper helperDetail;
    private static RecipeDBHelper helperRecipe;

    private ArrayList<CakeDetail> listDetails;
    private EditText quantityField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_list);

        Intent intent = getIntent();
        setTitle(intent.getStringExtra(UiUtils.NAME));
        this.cakeId = intent.getIntExtra(CakeDetailDBHelper.KEY_CAKE_ID, 1);

        helper = CakeDBHelper.getInstance(CakeDetailActivity.this);
        helperDetail = CakeDetailDBHelper.getInstance(CakeDetailActivity.this);
        helperRecipe = RecipeDBHelper.getInstance(this);

        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_ingredients; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cake_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_add_recipe_to_cake:
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
        final List<Recipe> listRecipes = helperRecipe.getAllRecipes();

        // populate IDs of all ingredients already in the recipe
        Iterator<CakeDetail> it = listDetails.iterator();
        List<Integer> listId = new ArrayList<>();
        for ( int i=0; i<listDetails.size(); i++ ) {
            listId.add(listDetails.get(i).getRecipeId());
        }

        final CharSequence[] seq = new CharSequence[listRecipes.size()];
        final boolean[] bool = new boolean[listRecipes.size()];

        for ( int i=0; i<listRecipes.size(); i++ ) {
            Recipe recipe = listRecipes.get(i);
            seq[i] = recipe.getName();
            bool[i] = listId.contains(recipe.getId());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(UiUtils.TITLE_ACTIVITY_CAKE_DETAILS);

        builder.setPositiveButton(UiUtils.OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                for (int i=0; i<bool.length; i++) {
                    if ( bool[i] ) {
                        // add ingredient to the recipe
                        CakeDetail detail = new CakeDetail(cakeId,listRecipes.get(i).getId(),0);
                        helperDetail.addCakeDetailToDb(detail, true);
                    }
                    else
                        helperDetail.removeDetailFromDb(cakeId, listRecipes.get(i).getId());
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
        listDetails = helperDetail.getCakeDetails(cakeId);
        ListAdapter adapter = new CakeDetailArrayAdapter(this, 0, listDetails);

        // Display the list view
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CakeDetail detail = listDetails.get(position);

    }
}
