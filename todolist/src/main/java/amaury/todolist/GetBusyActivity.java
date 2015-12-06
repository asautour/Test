package amaury.todolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import amaury.todolist.data.BakingDetail;
import amaury.todolist.data.CakeDetail;
import amaury.todolist.data.Recipe;
import amaury.todolist.db.BakingDetailDBHelper;
import amaury.todolist.db.CakeDBHelper;
import amaury.todolist.db.CakeDetailDBHelper;
import amaury.todolist.db.RecipeDBHelper;
import amaury.todolist.db.RecipeDetailDBHelper;
import amaury.todolist.utils.CakeDetailArrayAdapter;
import amaury.todolist.utils.UiUtils;

public class GetBusyActivity extends AppCompatActivity {
    private static BakingDetailDBHelper helperBaking;
    private static CakeDetailDBHelper helperCakeDetail;
    private static CakeDBHelper helperCake;
    private static RecipeDBHelper helperRecipe;

    private ArrayList<BakingDetail> listDetails;
    private ArrayList<Recipe> listRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_list);

        helperBaking = BakingDetailDBHelper.getInstance(this);
        helperCake = CakeDBHelper.getInstance(this);
        helperCakeDetail = CakeDetailDBHelper.getInstance(this);
        helperRecipe = RecipeDBHelper.getInstance(this);

        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_get_busy, menu);
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

    /* ---------------------------------------------------------------------------------------------

    --------------------------------------------------------------------------------------------- */
    private void updateUI() {
        // TODO aggregated list of recipes to make
        listDetails = helperBaking.getBakingDetails();
        Map<Integer, Integer> mapQtyByRecipe = new HashMap<>();

        // retrieve recipes from each cake
        for (BakingDetail bakingDetail : listDetails) {

            // retrieve quantity of current cake, which will be a multiplier
            int cakeQty = bakingDetail.getQuantity();

            // get all recipe components of the current cake
            ArrayList<CakeDetail> cakeDetails = helperCakeDetail.getCakeDetails(bakingDetail.getCakeId());

            // consolidate to the overall list of recipes
            for (CakeDetail cakeDetail : cakeDetails) {
                Integer qty = mapQtyByRecipe.get(cakeDetail.getRecipeId());
                if (qty == null)
                    qty = 0;

                // quantity to add = quantity allocated to current recipe/cake times number of cakes
                int addQty = new Double(cakeDetail.getQuantity()).intValue() * cakeQty + qty;

                if ( addQty > 0 )
                    mapQtyByRecipe.put(cakeDetail.getRecipeId(), qty+addQty);
            }
        }

        ArrayList<CakeDetail> listCakeDetails = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : mapQtyByRecipe.entrySet()) {
            int recipeId = entry.getKey();
            int totalQty = entry.getValue();
            CakeDetail detail = new CakeDetail(0,recipeId,totalQty);
            listCakeDetails.add(detail);
        }

        ListAdapter adapter = new CakeDetailArrayAdapter(this, 0, listCakeDetails);

        // Display the list view
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
    }

    /* ---------------------------------------------------------------------------------------------
        On recipe click, the details screen is opened with the list of ingredients, associated
        quantities and the relevant units.
    --------------------------------------------------------------------------------------------- */
    public void onCakeRecipeClick(View view) {
        View v = (View) view.getParent();
        TextView recipeName = (TextView) v.findViewById(R.id.textCakeRecipe);
        TextView recipeQty = (TextView) v.findViewById(R.id.textRecipeQty);

        // Parameter to build the detailed view is the recipe's ID.
        Recipe recipe = helperRecipe.getRecipe(recipeName.getText().toString());
        Intent intent = new Intent(getApplicationContext(), RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailDBHelper.KEY_RECIPE_ID, recipe.getId());
        intent.putExtra(UiUtils.NAME, recipe.getName());
        intent.putExtra(UiUtils.QUANTITY, Double.parseDouble(recipeQty.getText().toString()));
        startActivity(intent);
    }
}
