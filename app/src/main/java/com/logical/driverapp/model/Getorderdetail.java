package com.logical.driverapp.model;

/**
 * Created by Ravindra Birla on 12/11/2019.
 */
public class Getorderdetail {
    private String  order_id,assign_date,user_id,image,quantity,
            total_price,first_name,
            last_name,email,mobile,address,lat,lng;


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Getorderdetail( String order_id, String assign_date,
                          String user_id, String image, String quantity,
                          String total_price, String first_name, String last_name,
                          String email, String mobile, String address,
                          String lat, String lng) {

        this.order_id=order_id;
        this.assign_date=assign_date;
        this.user_id=user_id;
        this.image=image;

        this.quantity=quantity;

        this.total_price=total_price;
        this.first_name=first_name;
        this.last_name=last_name;
        this.email=email;
        this.mobile=mobile;
        this.address=address;
        this.lng=lng;
        this.lat=lat;


    }

    public String getFirst_name() {
        return first_name;
    }


    public String getAddress() {
        return address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAssign_date() {
        return assign_date;
    }

    public void setAssign_date(String assign_date) {
        this.assign_date = assign_date;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }



}
