package amaury.todolist.data;

/**
 * Created by su on 05/09/2015.
 */
public class Recipe {
    private int id;
    private String name;

    public Recipe() {
    }

    public Recipe(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
