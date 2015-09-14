package amaury.todolist.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import amaury.todolist.R;
import amaury.todolist.RecipeDetailActivity;
import amaury.todolist.data.Ingredient;
import amaury.todolist.data.RecipeDetail;
import amaury.todolist.db.IngredientDBHelper;

/**
 * Created by su on 06/09/2015.
 */
public class RecipeDetailArrayAdapter extends ArrayAdapter<RecipeDetail> {
    private Activity activity;
    private ArrayList<RecipeDetail> listDetails;
    private static LayoutInflater inflater = null;
    private IngredientDBHelper ingredientDBHelper = IngredientDBHelper.getInstance(activity);

    public RecipeDetailArrayAdapter (Activity activity, int textViewResourceId,ArrayList<RecipeDetail> details) {
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

    public static class ViewHolder {
        public TextView display_name;
        public TextView display_number;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.view_recipe_detail, null);
                holder = new ViewHolder();

                holder.display_name = (TextView) vi.findViewById(R.id.textRecipeIngredient);
                holder.display_number = (TextView) vi.findViewById(R.id.textIngredientQty);

                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }

            Ingredient ing = ingredientDBHelper.getIngredient(listDetails.get(position).getIngredientId());

            holder.display_name.setText(ing.getName());
            holder.display_number.setText(Double.toString(listDetails.get(position).getQuantity()));


        } catch (Exception e) {


        }
        return vi;
    }


}
