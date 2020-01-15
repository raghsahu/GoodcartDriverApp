package com.logical.driverapp.model;

/**
 * Created by Ravindra Birla on 12/11/2019.
 */
public class Profile {
    private String id;
    private String id_number, name, email,mobile,address,
            id_proof,bank_name,account_number,
            branch_name,ifsc_code,image,area_id,user_name,password,status,date;


    public Profile(String id, String id_number, String  name, String email,
                String mobile,String address,String  id_proof,
                String bank_name,String account_number,String branch_name,
                String ifsc_code,String image,String area_id,String user_name,
                String password,String status,String date) {
        this.id = id;
        this.id_number = id_number;
        this.name = name;
        this. email =  email;
        this.mobile = mobile;
        this.address=address;
        this.id_proof=id_proof;
        this.bank_name=bank_name;
        this.account_number=account_number;
        this.branch_name=branch_name;
        this.ifsc_code=ifsc_code;
        this.image=image;
        this.area_id=area_id;
        this.user_name=user_name;
        this.password=password;
        this.status=status;
        this.date=date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIfsc_code() {
        return ifsc_code;
    }

    public void setIfsc_code(String ifsc_code) {
        this.ifsc_code = ifsc_code;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getBank_name() {
        return bank_name;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getEmail() {
        return email;
    }

    public String getId_proof() {
        return id_proof;
    }

    public void setId_proof(String id_proof) {
        this.id_proof = id_proof;
    }

    public String getAddress() {
        return address;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
