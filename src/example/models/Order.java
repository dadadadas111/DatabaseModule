package example.models;

import annotations.*;
import java.util.HashMap;

@Table(name = "orders")
public class Order {
    @PrimaryKey
    @Column(name = "id")
    public String id;

    @Column(name = "order_map")
    public HashMap<String, Integer> orderMap;

    public Order() {}

    public Order(String id, HashMap<String, Integer> orderMap) {
        this.id = id;
        this.orderMap = orderMap;
    }
}
