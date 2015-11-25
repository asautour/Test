package amaury.todolist.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import amaury.todolist.R;
import amaury.todolist.data.BakingDetail;
import amaury.todolist.data.Cake;
import amaury.todolist.db.BakingDetailDBHelper;
import amaury.todolist.db.CakeDBHelper;

/**
 * Created by su on 06/09/2015.
 */
public class BakingDetailArrayAdapter extends ArrayAdapter<BakingDetail> {
    private Activity activity;
    private ArrayList<BakingDetail> listDetails;
    private List<Cake> listCakes = new ArrayList<>();
    private static LayoutInflater inflater = null;
    private BakingDetailDBHelper bakingDetailDBHelper = BakingDetailDBHelper.getInstance(activity);
    private CakeDBHelper cakeDBHelper = CakeDBHelper.getInstance(activity);

    public BakingDetailArrayAdapter(Activity activity, int textViewResourceId, ArrayList<BakingDetail> details) {
        super(activity, textViewResourceId, details);
        try {
            this.activity = activity;
            this.listDetails = details;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listCakes = cakeDBHelper.getAllCakes();

        } catch (Exception e) {

        }
    }

    @Override
    public int getCount() {
        return listCakes.size();
    }

    public static class ViewHolder {
        public TextView display_name;
        public TextView display_number;

    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;

        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.view_select_cakes, null);
                holder = new ViewHolder();

                holder.display_name = (TextView) vi.findViewById(R.id.textCake);
                holder.display_number = (TextView) vi.findViewById(R.id.textCakeQty);

                holder.display_number.setInputType(Configuration.KEYBOARD_12KEY);
                holder.display_number.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                holder.display_number.setSelectAllOnFocus(true);
                holder.display_number.setOnEditorActionListener(new BakingDetailEditorActionListener(
                        holder.display_number, listDetails, position,
                        bakingDetailDBHelper, cakeDBHelper));
                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }

            holder.display_name.setText(listCakes.get(position).getName());
            holder.display_number.setText("0");//Double.toString(listDetails.get(position).getQuantity()));


        } catch (Exception e) {


        }
        return vi;
    }
}
