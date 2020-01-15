package com.logical.driverapp.model;

/**
 * Created by Ravindra Birla on 14/11/2019.
 */
public class Notification {

    String id, order_id,sender_id,receiver_id,receiver_type,message,date_time,status;


    public Notification(String id, String order_id, String sender_id, String receiver_id, String receiver_type, String message, String date_time, String status) {
        this.id=id;
        this.order_id=order_id;
        this.sender_id=sender_id;
        this.receiver_id=receiver_id;
        this.receiver_type=receiver_type;
        this.message=message;
        this.date_time=date_time;
        this.status=status;


    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver_type() {
        return receiver_type;
    }

    public void setReceiver_type(String receiver_type) {
        this.receiver_type = receiver_type;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
