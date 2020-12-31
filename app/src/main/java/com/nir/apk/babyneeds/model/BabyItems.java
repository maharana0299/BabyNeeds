package com.nir.apk.babyneeds.model;

import androidx.annotation.NonNull;

public class BabyItems {
    private int id;
    private String items;
    private int quantity;
    private String color;
    private int size;
    private String dateItemAdded;

    public BabyItems(){}



    public String getDateItemAdded() {
        return dateItemAdded;
    }

    public void setDateItemAdded(String dateItemAdded) {
        this.dateItemAdded = dateItemAdded;
    }

    public BabyItems(int id, String items, int quantity, String color, int size, String dateItemAdded) {
        this.id = id;
        this.items = items;
        this.quantity = quantity;
        this.color = color;
        this.size = size;
        this.dateItemAdded = dateItemAdded;
    }



    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public String getDateItemAdded() {
//        return dateItemAdded;
//    }

//    public void setDateItemAdded(String dateItemAdded) {
//        this.dateItemAdded = dateItemAdded;
//    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "BabyItems{" +
                "id=" + id +
                ", items='" + items + '\'' +
                ", quantity=" + quantity +
                ", color='" + color + '\'' +
                ", size=" + size +
                ", dateItemAdded='" + dateItemAdded + '\'' +
                '}';
    }



}
