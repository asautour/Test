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

import amaury.todolist.R;
import amaury.todolist.data.CakeDetail;
import amaury.todolist.data.Recipe;
import amaury.todolist.db.CakeDetailDBHelper;
import amaury.todolist.db.RecipeDBHelper;

/**
 * Created by su on 06/09/2015.
 */
public class CakeDetailArrayAdapter extends ArrayAdapter<CakeDetail> {
    private Activity activity;
    private ArrayList<CakeDetail> listDetails;
    private static LayoutInflater inflater = null;
    private RecipeDBHelper recipeDBHelper = RecipeDBHelper.getInstance(activity);
    private CakeDetailDBHelper detailDBHelper = CakeDetailDBHelper.getInstance(activity);

    public CakeDetailArrayAdapter(Activity activity, int textViewResourceId, ArrayList<CakeDetail> details) {
        super(activity, textViewResourceId, details);
        try {
            this.activity = activity;
            this.listDetails = details;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    @Override
    public int getCount() {
        return listDetails.size();
    }

    public static class ViewHolder2 {
        public TextView display_name;
        public TextView display_number;

    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder2 holder;

        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.view_cake_detail, null);
                holder = new ViewHolder2();

                holder.display_name = (TextView) vi.findViewById(R.id.textCakeRecipe);
                holder.display_number = (TextView) vi.findViewById(R.id.textRecipeQty);

                holder.display_number.setInputType(Configuration.KEYBOARD_12KEY);
                holder.display_number.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                holder.display_number.setSelectAllOnFocus(true);
                holder.display_number.setOnEditorActionListener(new CakeDetailEditorActionListener(holder.display_number, listDetails, position, detailDBHelper));
                vi.setTag(holder);
            } else {
                holder = (ViewHolder2) vi.getTag();
            }

            Recipe recipe = recipeDBHelper.getRecipe(listDetails.get(position).getRecipeId());

            holder.display_name.setText(recipe.getName());
            holder.display_number.setText(Double.toString(listDetails.get(position).getQuantity()));


        } catch (Exception e) {


        }
        return vi;
    }
}
