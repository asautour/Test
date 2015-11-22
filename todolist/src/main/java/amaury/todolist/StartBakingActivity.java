package amaury.todolist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import amaury.todolist.utils.UiUtils;

/**
 * Created by su on 22/11/2015.
 */
public class StartBakingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_list);
        setTitle(UiUtils.TITLE_ACTIVITY_START_BAKING);
    }
}
