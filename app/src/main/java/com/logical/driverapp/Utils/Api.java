package com.logical.driverapp.Utils;

/**
 * Created by Raghvendra Sahu on 31/12/2019.
 */
public interface Api {

    String BaseUrl= "https://logicalsofttech.com/goodcart/DeliveryApi/";


    String assign_orders= "assign_orders";
    String get_cities= "http://logicalsofttech.com/goodcart/Api/get_cities";
    String get_areas= "http://logicalsofttech.com/goodcart/Api/get_areas";
    String update_shipping_address= "https://logicalsofttech.com/goodcart/DeliveryApi/update_shipping_address";
    String mark_as_delievered= "https://logicalsofttech.com/goodcart/DeliveryApi/mark_as_delievered";
}
