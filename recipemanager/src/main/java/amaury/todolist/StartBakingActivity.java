package amaury.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import amaury.todolist.data.Cake;
import amaury.todolist.db.CakeDBHelper;
import amaury.todolist.db.CakeDetailDBHelper;
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

    public void buttonOnGetBusyClick(View view) {
        View v = (View) view.getParent();
        TextView textView = (TextView) v.findViewById(R.id.cakeTextView);

        // Parameter to build the detailed view is the cake's ID.
        Intent intent = new Intent(getApplicationContext(), GetBusyActivity.class);
        //intent.putExtra(CakeDetailDBHelper.KEY_CAKE_ID, cake.getId());
        //intent.putExtra(UiUtils.NAME, cake.getName());
        startActivity(intent);
    }

}
