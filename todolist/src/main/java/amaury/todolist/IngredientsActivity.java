package amaury.todolist;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import amaury.todolist.db.IngredientDBHelper;
import amaury.todolist.utils.UiUtils;

public class IngredientsActivity extends AppCompatActivity {
    private IngredientDBHelper helper;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_list);
        setTitle(UiUtils.TITLE_ACTIVITY_INGREDIENTS);
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_ingredients; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ingredients, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_add_ingredient:
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
        builder.setTitle(UiUtils.TITLE_POPUP_ADD_INGREDIENT);
        final EditText inputField = new EditText(this);
        builder.setView(inputField);

        // when click on "Add", add ingredient to the INGREDIENT table
        builder.setPositiveButton(UiUtils.ADD, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //addIngredientToDb(inputField);
                helper.addIngredientToDb(inputField.getText().toString());
                updateUI();
            }
        });

        builder.setNegativeButton(UiUtils.CANCEL, null);
        builder.create().show();
    }

    /* ---------------------------------------------------------------------------------------------

    --------------------------------------------------------------------------------------------- */
    private void updateUI() {
        helper = IngredientDBHelper.getInstance(IngredientsActivity.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(IngredientDBHelper.TABLE_INGREDIENTS,
                new String[]{IngredientDBHelper.KEY_ID, IngredientDBHelper.KEY_NAME},
                null, null, null, null, null);

        listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.view_ingredient,
                cursor,
                new String[] { IngredientDBHelper.KEY_NAME},
                new int[] { R.id.ingredientNameView},
                0
        );

        // Display the list view
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(listAdapter);

    }

    /* ---------------------------------------------------------------------------------------------
        When clicking on the remove button, we delete the ingredient from the database
    --------------------------------------------------------------------------------------------- */
    public void onRemoveIngredientClick(View view) {
        View v = (View) view.getParent();
        TextView ingredientTextView = (TextView) v.findViewById(R.id.ingredientNameView);
        helper.deleteIngredient(ingredientTextView.getText().toString());

        // TODO on ingredient remove make sure all the relevant cleanup is done on recipe/db

        updateUI();
    }
}
