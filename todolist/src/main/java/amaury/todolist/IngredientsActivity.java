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

public class IngredientsActivity extends AppCompatActivity {

    private IngredientDBHelper helper;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_list);
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

    private void showPopupAdd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add an ingredient");
        //builder.setMessage("What do you want to do?");
        final EditText inputField = new EditText(this);
        builder.setView(inputField);

        // when click on "Add", add ingredient to the INGREDIENT table
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                addIngredientToDb(inputField);
                updateUI();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    private void addIngredientToDb(EditText inputField) {
        String ingredientName = inputField.getText().toString();
        Log.d("IngredientsActivity", ingredientName);

        IngredientDBHelper helper = new IngredientDBHelper(IngredientsActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.clear();
        values.put(IngredientDBHelper.KEY_NAME, ingredientName);

        db.insertWithOnConflict(IngredientDBHelper.TABLE_INGREDIENTS, null, values,
                SQLiteDatabase.CONFLICT_IGNORE);
    }

    private void updateUI() {
        helper = new IngredientDBHelper(IngredientsActivity.this);
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

        //this.setListAdapter(listAdapter);
        // Display the list view
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(listAdapter);

    }

    public void onRemoveIngredientClick(View view) {
        View v = (View) view.getParent();
        TextView ingredientTextView = (TextView) v.findViewById(R.id.ingredientNameView);
        helper.deleteIngredient(ingredientTextView.getText().toString());
        updateUI();
    }
}
