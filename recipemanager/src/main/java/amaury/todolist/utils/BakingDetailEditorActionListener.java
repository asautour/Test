package amaury.todolist.utils;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import amaury.todolist.data.BakingDetail;
import amaury.todolist.data.Cake;
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
    private List<Cake> listCakes;

    public BakingDetailEditorActionListener(TextView view, ArrayList<BakingDetail> listDetails,
                                            int position,
                                            BakingDetailDBHelper helper,
                                            CakeDBHelper cakeDBHelper) {
        this.view = view;
        this.listDetails = listDetails;
        this.position = position;
        this.bakingDBHelper = helper;
        this.cakeDBHelper = cakeDBHelper;

        listCakes = cakeDBHelper.getAllCakes();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT ||
                event != null && (event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

            // retrieve quantity typed in by the user
            int quantity = Integer.valueOf(view.getText().toString());

            // from here update the recipe detail so the quantity is saved down
            updateDetail(quantity, position);

            // remove focus from the current quantity cell
            view.clearFocus();

            // consume
            return true;
        }
        return false; // pass on to other listeners.}
    }

    protected void updateDetail(int quantity, int position) {
        Cake cake = listCakes.get(position);
        BakingDetail myDetail = null;

        for (BakingDetail detail : listDetails) {
            if ( detail.getCakeId() == cake.getId() ) {
                myDetail = detail;
                break;
            }
        }

        if (myDetail!=null) {
            myDetail.setQuantity(quantity);
            bakingDBHelper.updateDetail(myDetail);
        }
        else {
            myDetail = new BakingDetail();
            myDetail.setQuantity(quantity);
            myDetail.setCakeId(cake.getId());
            bakingDBHelper.addDetailToDb(myDetail, false);
        }
    }
}