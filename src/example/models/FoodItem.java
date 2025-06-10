package example.models;

import annotations.*;

@Table(name = "food_items")
public class FoodItem {
    @PrimaryKey
    @Column(name = "id")
    public String id;

    @Column(name = "name")
    public String name;

    @Column(name = "price", nullable = false)
    public double price;

    public FoodItem() {}

    public FoodItem(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
