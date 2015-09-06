package amaury.todolist.data;

import java.util.List;
import java.util.Map;

/**
 * Created by su on 06/09/2015.
 */
public class RecipeContent {
    private int recipeId;
    private String recipeName;
    private List<String> listIngredients;
    private Map<Ingredient, String> mapIngredientsUnits;

    public RecipeContent(int recipeId, String recipeName, Map<Ingredient, String> mapIngredientsUnits) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.mapIngredientsUnits = mapIngredientsUnits;
    }

    public int getRecipeId() { return recipeId; }
    public void setRecipeId(int recipeId) { this.recipeId = recipeId; }

    public String getRecipeName() {
        return recipeName;
    }
    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public List<String> getListIngredients() {
        return listIngredients;
    }
    public void setListIngredients(List<String> listIngredients) {
        this.listIngredients = listIngredients;
    }

    public Map<Ingredient, String> getMapIngredientsUnits() {
        return mapIngredientsUnits;
    }
    public void setMapIngredientsUnits(Map<Ingredient, String> mapIngredientsUnits) {
        this.mapIngredientsUnits = mapIngredientsUnits;
    }
}
