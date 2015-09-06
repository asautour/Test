package amaury.todolist.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import amaury.todolist.R;
import amaury.todolist.data.RecipeDetail;

/**
 * Created by su on 06/09/2015.
 */
public class RecipeDetailArrayAdapter extends ArrayAdapter<RecipeDetail> {
    private final Context context;
    private final List<RecipeDetail> listDetails;

    public RecipeDetailArrayAdapter(Context context, List<RecipeDetail> list) {
        super(context, -1, list);
        this.context = context;
        this.listDetails = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.view_recipe_detail, parent, false);
        TextView textIngredientName = (TextView) rowView.findViewById(R.id.textRecipeIngredient);
        TextView textQty = (TextView) rowView.findViewById(R.id.textIngredientQty);

        RecipeDetail detail = getItem(position);
        textIngredientName.setText(detail.getId());
        textQty.setText(detail.getQuantity() + detail.getUnit());

        return rowView;
    }

    @Override
    public int getCount() {
        return listDetails.size();
    }

    @Override
    public RecipeDetail getItem(int position) {
        return listDetails.get(position);
    }
}
