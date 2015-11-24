package amaury.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import amaury.todolist.data.Recipe;
import amaury.todolist.db.RecipeDetailDBHelper;
import amaury.todolist.utils.UiUtils;

/**
 * Created by su on 22/11/2015.
 */
public class StartBakingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_start_baking);
        setTitle(UiUtils.TITLE_ACTIVITY_START_BAKING);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void buttonOnSelectCakesClick(View view) {
        View v = (View) view.getParent();
        TextView textView = (TextView) v.findViewById(R.id.recipeTextView);

        // Parameter to build the detailed view is the recipe's ID.
        Intent intent = new Intent(getApplicationContext(), BakingDetailActivity.class);
        startActivity(intent);
    }

}
