package com.logical.driverapp.model;

public class AreaData {

    String id;
    String name;

    public AreaData(String id, String name) {
        this.id=id;
        this.name=name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }
}
