package amaury.todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DebugUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import amaury.todolist.db.DBUtils;
import amaury.todolist.utils.IngredientUtils;
import amaury.todolist.utils.UiUtils;

public class HomeScreenActivity extends AppCompatActivity {
    private final int CLEAR_DB = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_home_screen);
        setTitle(UiUtils.TITLE_ACTIVITY_HOME);

        if ( DBUtils.DATABASE_RESET )
            DBUtils.clearDb(getBaseContext());

        if ( DBUtils.DATABASE_INIT )
            DBUtils.initiateDb(getBaseContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_ingredients; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    public void buttonOnClick(View v) {
        Button button = (Button) v;

        switch (button.getId()) {
            case R.id.buttonIngredients:
                startActivity(new Intent(HomeScreenActivity.this, IngredientsActivity.class));
                break;
            case R.id.buttonRecipes:
                startActivity(new Intent(HomeScreenActivity.this, RecipesActivity.class));
                break;
            case R.id.buttonCakes:
                startActivity(new Intent(HomeScreenActivity.this, CakesActivity.class));
                break;
            case R.id.buttonStartBaking:
                startActivity(new Intent(HomeScreenActivity.this, StartBakingActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.sub_menu_clear_db:
                showPopup(CLEAR_DB);
                return true;
            default:
                return false;
        }
    }

    private void showPopup(final int actionId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String title;

        switch (actionId) {
            case CLEAR_DB:
                title = UiUtils.TITLE_ARE_YOU_SURE; break;
            default:
                title = ""; break;
        }
        builder.setTitle(title);

        // when click on "OK", add ingredient to the INGREDIENT table
        builder.setPositiveButton(UiUtils.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // remove all ingredients
                if (actionId==CLEAR_DB)
                    DBUtils.clearDb(getBaseContext());
            }
        });

        builder.setNegativeButton(UiUtils.CANCEL, null);
        builder.create().show();
    }
}
