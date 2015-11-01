package amaury.todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import amaury.todolist.data.Cake;
import amaury.todolist.db.CakeDBHelper;
import amaury.todolist.db.CakeDetailDBHelper;
import amaury.todolist.utils.UiUtils;

/* *************************************************************************************************

************************************************************************************************* */
public class CakesActivity extends AppCompatActivity {

    private static CakeDBHelper helper;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_list);
        setTitle(UiUtils.TITLE_ACTIVITY_CAKES);
        helper = CakeDBHelper.getInstance(CakesActivity.this);
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cakes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_add_cake:
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
        builder.setTitle("Add a cake");
        final EditText inputField = new EditText(this);
        builder.setView(inputField);

        // when click on "Add", add ingredient to the CAKE_NAMES table
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                helper.addCakeToDb(inputField.getText().toString());
                updateUI();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    /* ---------------------------------------------------------------------------------------------

    --------------------------------------------------------------------------------------------- */
    private void updateUI() {
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(CakeDBHelper.TABLE_CAKE_NAMES,
                new String[]{CakeDBHelper.KEY_ID, CakeDBHelper.KEY_NAME},
                null,null,null,null,null);

        listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.view_cakes,
                cursor,
                new String[]{CakeDBHelper.KEY_NAME},
                new int[]{R.id.cakeTextView},
                0
        );

        // Display the list view
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(listAdapter);

        // TODO update code on cake long click action
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                Log.v("long clicked", "pos: " + pos);
                return true;
            }
        });
    }

    /* ---------------------------------------------------------------------------------------------

    --------------------------------------------------------------------------------------------- */
    public void onRemoveCakeClick(View view) {
        View v = (View) view.getParent();
        TextView textView = (TextView) v.findViewById(R.id.cakeTextView);
        helper.deleteCake(textView.getText().toString());
        updateUI();
    }

    /* ---------------------------------------------------------------------------------------------
        On cake click, the details screen is opened with the list of ingredients, associated
        quantities and the relevant units.
    --------------------------------------------------------------------------------------------- */
    public void onCakeClick(View view) {
        View v = (View) view.getParent();
        TextView textView = (TextView) v.findViewById(R.id.cakeTextView);

        // Parameter to build the detailed view is the cake's ID.
        Cake cake = helper.getCake(textView.getText().toString());
        Intent intent = new Intent(getApplicationContext(), CakeDetailActivity.class);
        intent.putExtra(CakeDetailDBHelper.KEY_CAKE_ID, cake.getId());
        intent.putExtra(UiUtils.NAME, cake.getName());
        startActivity(intent);
    }

}
