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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import amaury.todolist.data.BakingDetail;
import amaury.todolist.data.Cake;
import amaury.todolist.data.CakeDetail;
import amaury.todolist.data.Recipe;
import amaury.todolist.db.BakingDetailDBHelper;
import amaury.todolist.db.CakeDBHelper;
import amaury.todolist.db.CakeDetailDBHelper;
import amaury.todolist.db.RecipeDBHelper;
import amaury.todolist.db.RecipeDetailDBHelper;
import amaury.todolist.utils.BakingDetailArrayAdapter;
import amaury.todolist.utils.CakeDetailArrayAdapter;
import amaury.todolist.utils.UiUtils;

public class BakingDetailActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static CakeDBHelper helperCake;
    private static BakingDetailDBHelper helperBaking;
    private ArrayList<BakingDetail> listDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_list);

        Intent intent = getIntent();
        setTitle(intent.getStringExtra(UiUtils.TITLE_ACTIVITY_SELECT_CAKES));

        helperBaking = BakingDetailDBHelper.getInstance(BakingDetailActivity.this);
        helperCake = CakeDBHelper.getInstance(BakingDetailActivity.this);

        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_ingredients; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_cakes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.sub_menu_clear_all:
                // TODO
                return true;
            default:
                return false;
        }
    }

    /* ---------------------------------------------------------------------------------------------

    --------------------------------------------------------------------------------------------- */
    private void updateUI() {
        listDetails = helperBaking.getBakingDetails();
        ListAdapter adapter = new BakingDetailArrayAdapter(this, 0, listDetails);

        // Display the list view
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //CakeDetail detail = listDetails.get(position);

    }

    /* ---------------------------------------------------------------------------------------------
        On recipe click, the details screen is opened with the list of ingredients, associated
        quantities and the relevant units.
    --------------------------------------------------------------------------------------------- */
    public void onCakeRecipeClick(View view) {

    }
}
