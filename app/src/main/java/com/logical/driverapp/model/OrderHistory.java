package com.logical.driverapp.model;

/**
 * Created by Ravindra Birla on 13/11/2019.
 */
public class OrderHistory {
    private String order_id, assign_date,status,user_id,name,total;

    public OrderHistory(String order_id, String assign_date, String status, String user_id, String name, String total) {

        this.order_id=order_id;
        this.assign_date=assign_date;
        this.status=status;
        this.user_id=user_id;
        this.name=name;
        this.total=total;




    }

    public String getStatus() {
        return status;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setStatus(String status) {
        this.status = status;
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
