package amaury.todolist.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

import amaury.todolist.R;
import amaury.todolist.RecipeDetailActivity;
import amaury.todolist.data.Ingredient;
import amaury.todolist.data.RecipeDetail;

/**
 * Created by su on 06/09/2015.
 */
public class RecipeDetailArrayAdapter extends ArrayAdapter<Ingredient> {
    protected final Context context;

    public RecipeDetailArrayAdapter(Context context) {
        super(context, -1);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.view_popup_ingredients, parent, false);
        TextView textIngredientName = (TextView) rowView.findViewById(R.id.ingredientName);

        Ingredient ingredient = getItem(position);
        textIngredientName.setText(ingredient.getName());

        return rowView;
    }


}
