package com.elidaniel92.app.product;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateUpdateProductDTO {
    private String name;
    private int price;
    private int quantityInStock;

    @JsonCreator
    public CreateUpdateProductDTO(
            @JsonProperty("name") String name,
            @JsonProperty("price") int price,
            @JsonProperty("quantityInStock") int quantityInStock) {
        this.name = name;
        this.price = price;
        this.quantityInStock = quantityInStock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    @Override
    public String toString() {
        return "CreateProductDTO{" +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantityInStock=" + quantityInStock +
                '}';
    }
}
