package example.models;

import annotations.*;

@Table(name = "customers")
public class Customer {
    @PrimaryKey
    @Column(name = "id")
    public String id;

    @Column(name = "name", nullable = false)
    public String name;

    public Customer() {}

    public Customer(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
