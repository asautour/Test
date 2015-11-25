package amaury.todolist.utils;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import amaury.todolist.data.BakingDetail;
import amaury.todolist.data.CakeDetail;
import amaury.todolist.db.BakingDetailDBHelper;
import amaury.todolist.db.CakeDBHelper;

/**
 * Created by su on 17/10/2015.
 */
public class BakingDetailEditorActionListener implements EditText.OnEditorActionListener {
    private TextView view;
    private ArrayList<BakingDetail> listDetails;
    private int position;
    private static BakingDetailDBHelper bakingDBHelper;
    private static CakeDBHelper cakeDBHelper;

    public BakingDetailEditorActionListener(TextView view, ArrayList<BakingDetail> listDetails,
                                            int position,
                                            BakingDetailDBHelper helper,
                                            CakeDBHelper cakeDBHelper) {
        this.view = view;
        this.listDetails = listDetails;
        this.position = position;
        this.bakingDBHelper = helper;
        this.cakeDBHelper = cakeDBHelper;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT ||
                event != null && (event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

            // retrieve quantity typed in by the user
            double quantity = Double.valueOf(view.getText().toString());

            // from here update the recipe detail so the quantity is saved down
            updateDetail(quantity, position);

            // remove focus from the current quantity cell
            view.clearFocus();

            // consume
            return true;
        }
        return false; // pass on to other listeners.}
    }

    protected void updateDetail(double quantity, int position) {
        //BakingDetail detail =
        /*CakeDetail detail = listDetails.get(position);
        detail.setQuantity(quantity);

        // save down quantity to database
        detailDBHelper.updateDetail(detail);*/
    }
}