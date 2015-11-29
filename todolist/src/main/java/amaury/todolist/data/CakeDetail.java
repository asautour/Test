package amaury.todolist.data;


public class CakeDetail {
    private int id;
    private int cakeId;
    private int recipeId;
    private double quantity;

    public CakeDetail() {
    }

    public CakeDetail(int cakeId, int recipeId, double quantity) {
        this.recipeId = recipeId;
        this.cakeId = cakeId;
        this.quantity = quantity;
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

    public int getCakeId() {
        return cakeId;
    }
    public void setCakeId(int cakeId) {
        this.cakeId = cakeId;
    }

    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
}