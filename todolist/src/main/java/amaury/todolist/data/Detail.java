package amaury.todolist.data;

/**
 * Created by su on 22/11/2015.
 */
public class Detail {
    private int id;
    private int objectId;
    private double quantity;

    public Detail() {
    }

    public Detail(int objectId, double quantity) {
        this.objectId = objectId;
        this.quantity = quantity;
    }

    public int getId() { return id; }
    public void setId(int id) {this.id = id;}

    public int getObjectId() { return objectId; }
    public void setObjectId(int objectId) { this.objectId = objectId;}

    public double getQuantity() {
        return quantity;
    }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

}
