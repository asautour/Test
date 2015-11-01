package amaury.todolist.utils;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import amaury.todolist.data.Cake;
import amaury.todolist.data.CakeDetail;
import amaury.todolist.data.RecipeDetail;
import amaury.todolist.db.CakeDetailDBHelper;
import amaury.todolist.db.RecipeDetailDBHelper;

/**
 * Created by su on 17/10/2015.
 */
public class CakeDetailEditorActionListener implements EditText.OnEditorActionListener {
    private TextView view;
    private ArrayList<CakeDetail> listDetails;
    private int position;
    private CakeDetailDBHelper detailDBHelper;

    public CakeDetailEditorActionListener(TextView view, ArrayList<CakeDetail> listDetails, int position, CakeDetailDBHelper detailDBHelper) {
        this.view = view;
        this.listDetails = listDetails;
        this.position = position;
        this.detailDBHelper = detailDBHelper;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

            // retrieve quantity typed in by the user
            double quantity = Double.valueOf(view.getText().toString());

            // remove focus from the current quantity cell
            view.clearFocus();

            // from here update the recipe detail so the quantity is saved down
            updateDetail(quantity, position);

            // consume
            return true;
        }
        return false; // pass on to other listeners.}
    }

    protected void updateDetail(double quantity, int position) {
        CakeDetail detail = listDetails.get(position);
        detail.setQuantity(quantity);

        // save down quantity to database
        detailDBHelper.updateDetail(detail);
    }
}