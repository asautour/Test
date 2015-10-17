package amaury.todolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import amaury.todolist.db.DBUtils;
import amaury.todolist.utils.UiUtils;

public class HomeScreenActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_home_screen);
        setTitle(UiUtils.TITLE_ACTIVITY_HOME);

        if ( DBUtils.DATABASE_RESET )
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
