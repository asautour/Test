package amaury.todolist.data;


public class BakingDetail {
    private int id;
    private int cakeId;
    private int quantity;

    public BakingDetail() {
    }

    public BakingDetail(int cakeId, int quantity) {
        this.cakeId = cakeId;
        this.quantity = quantity;

    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getCakeId() {
        return cakeId;
    }
    public void setCakeId(int cakeId) {
        this.cakeId = cakeId;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}