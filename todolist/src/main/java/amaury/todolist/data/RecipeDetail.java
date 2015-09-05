package amaury.todolist.data;

import android.content.Intent;

import java.util.Map;

public class RecipeDetail {
    public static final String RECIPE_EXTRA_ID = "id";
    public static final String RECIPE_EXTRA_NAME = "name";
    public static final String RECIPE_EXTRA_INGREDIENTS = "listIdIngredients";
    public static final String RECIPE_EXTRA_WEIGHTS = "listWeights";

    private int id;
    private String name;
    private int[] listIdIngredients;
    private double[] listWeights;
    private Map<Double, Double> mapIngredients;

    public RecipeDetail() {
    }

    public RecipeDetail(Intent intent) {
        this.name = intent.getExtras().getString(RECIPE_EXTRA_NAME);
        this.listIdIngredients = intent.getExtras().getIntArray(RECIPE_EXTRA_INGREDIENTS);
        this.listWeights = intent.getExtras().getDoubleArray(RECIPE_EXTRA_WEIGHTS);
        this.id = intent.getExtras().getInt(RECIPE_EXTRA_ID);

        // each line of the view will reference the ingredient's name and its weight
        // rather than carry the ingredient's value, we carry the Ingredient object's ID in the database
        for (int i = 0; i < listIdIngredients.length; i++) {
            mapIngredients.put(new Double(listIdIngredients[i]), new Double(listWeights[i]));
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getListIdIngredients() {
        return listIdIngredients;
    }

    public void setListIdIngredients(int[] listIdIngredients) {
        this.listIdIngredients = listIdIngredients;
    }

    public double[] getListWeights() {
        return listWeights;
    }

    public void setListWeights(double[] listWeights) {
        this.listWeights = listWeights;
    }

    public Map<Double, Double> getMapIngredients() {
        return mapIngredients;
    }

    public void setMapIngredients(Map<Double, Double> mapIngredients) {
        this.mapIngredients = mapIngredients;
    }
}
