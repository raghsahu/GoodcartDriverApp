package com.logical.driverapp.model;

/**
 * Created by Ravindra Birla on 13/11/2019.
 */
public class OrderDetails {

    private String product_id, products_name,quantity,price,total_price;


    public OrderDetails(String product_id, String products_name, String quantity, String price, String total_price) {

        this.product_id=product_id;
        this.products_name=products_name;
        this.quantity=quantity;
        this.price=price;
        this.total_price=total_price;





    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProducts_name() {
        return products_name;
    }

    public void setProducts_name(String products_name) {
        this.products_name = products_name;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
}
