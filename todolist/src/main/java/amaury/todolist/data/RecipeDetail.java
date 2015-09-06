package amaury.todolist.data;

import android.content.Intent;
import android.text.TextUtils;

import java.util.Map;

import amaury.todolist.utils.UnitUtils;

public class RecipeDetail {
    private int id;
    private int recipeId;
    private int ingredientId;
    private double quantity;
    private String unit;

    public RecipeDetail() {
    }

    public RecipeDetail(int recipeId, int ingredientId, double quantity, String unit) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        this.quantity = quantity;

        if ( unit != null)
            this.unit = unit;
        else
            this.unit = UnitUtils.UNIT_GRAM;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}