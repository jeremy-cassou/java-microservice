package com.ecommerce.microservice.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

@Entity
@JsonFilter("productFilter")
public class Product {
    @Id
    @GeneratedValue
    private int id;

    @Length(min = 3, max = 20, message = "Product name need to have length between 3 to 20")
    private String name;

    @Min(value = 1, message = "Product need to have price")
    private int price;
    private int buyingPrice;

    public Product() {
    }

    public Product(int id, String name, int price, int buyingPrice) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.buyingPrice = buyingPrice;
    }

    public int getId () {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(int buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    @Override
    public String toString() {
        return "Product{id=" + this.id +
                ",name=" + this.name +
                ",price=" + this.price +
                "}";

    }
}
