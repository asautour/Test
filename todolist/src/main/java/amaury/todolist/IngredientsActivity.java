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

import amaury.todolist.db.IngredientContract;
import amaury.todolist.db.IngredientDBHelper;

public class IngredientsActivity extends AppCompatActivity {

    private IngredientDBHelper helper;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_add_ingredient:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Add an ingredient");
                builder.setMessage("What do you want to do?");
                final EditText inputField = new EditText(this);
                builder.setView(inputField);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String ingredient = inputField.getText().toString();
                        Log.d("IngredientsActivity",ingredient);

                        IngredientDBHelper helper = new IngredientDBHelper(IngredientsActivity.this);
                        SQLiteDatabase db = helper.getWritableDatabase();
                        ContentValues values = new ContentValues();

                        values.clear();
                        values.put(IngredientContract.Columns.INGREDIENT,ingredient);

                        db.insertWithOnConflict(IngredientContract.TABLE, null, values,
                                SQLiteDatabase.CONFLICT_IGNORE);

                        updateUI();
                    }
                });

                builder.setNegativeButton("Cancel",null);

                builder.create().show();
                return true;

            default:
                return false;
        }
    }

    private void updateUI() {
        helper = new IngredientDBHelper(IngredientsActivity.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(IngredientContract.TABLE,
                new String[]{IngredientContract.Columns._ID, IngredientContract.Columns.INGREDIENT},
                null,null,null,null,null);

        listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.ingredient_view,
                cursor,
                new String[] { IngredientContract.Columns.INGREDIENT},
                new int[] { R.id.ingredientTextView},
                0
        );

        //this.setListAdapter(listAdapter);
        // Display the list view
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(listAdapter);

    }

    public void onDoneButtonClick(View view) {
        View v = (View) view.getParent();
        TextView ingredientTextView = (TextView) v.findViewById(R.id.ingredientTextView);
        String ingredient = ingredientTextView.getText().toString();

        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                IngredientContract.TABLE,
                IngredientContract.Columns.INGREDIENT,
                ingredient);


        helper = new IngredientDBHelper(IngredientsActivity.this);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);
        updateUI();
    }
}
