package amaury.todolist.utils;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import amaury.todolist.data.RecipeDetail;
import amaury.todolist.db.RecipeDetailDBHelper;

/**
 * Created by su on 17/10/2015.
 */
public class RecipeDetailEditorActionListener implements EditText.OnEditorActionListener {
    private ArrayList<RecipeDetail> listDetails;
    private int position;
    private RecipeDetailDBHelper detailDBHelper;

    public RecipeDetailEditorActionListener(ArrayList<RecipeDetail> listDetails, int position, RecipeDetailDBHelper detailDBHelper) {
        this.listDetails = listDetails;
        this.position = position;
        this.detailDBHelper = detailDBHelper;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT ||
                event != null && (event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

            // retrieve quantity typed in by the user
            double quantity = Double.valueOf(v.getText().toString());

            // from here update the recipe detail so the quantity is saved down
            updateDetail(quantity, position);

            // remove focus from the current quantity cell
            v.clearFocus();

            // consume
            return true;
        }
        return false; // pass on to other listeners.}
    }

    protected void updateDetail(double quantity, int position) {
        RecipeDetail detail = listDetails.get(position);
        detail.setQuantity(quantity);

        // save down quantity to database
        detailDBHelper.updateDetail(detail);
    }
}