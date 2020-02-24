package com.hamzaazam.fyp_frontend.Model;

import java.util.HashMap;
import java.util.List;

public class BillM {

    public String billId;
    public String userID;

    public String billCategory;
    public String billDate;
    public String billAmount;
    public String billCustomerName;

    public String billImageUrl;

    public String billAddNote;

    HashMap<String,Object> billText;


    public BillM(){}


    public BillM(String bc,String bd, String ba, String bcn, String biu, String ban, HashMap<String,Object> hm){
        billCategory=bc;
        billAmount=ba;
        billAddNote=ban;
        billCustomerName=bcn;
        billDate=bd;
        billImageUrl=biu;

        billText=hm;

    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getBillCategory() {
        return billCategory;
    }

    public void setBillCategory(String billCategory) {
        this.billCategory = billCategory;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(String billAmount) {
        this.billAmount = billAmount;
    }

    public String getBillCustomerName() {
        return billCustomerName;
    }

    public void setBillCustomerName(String billCustomerName) {
        this.billCustomerName = billCustomerName;
    }

    public String getBillImageUrl() {
        return billImageUrl;
    }

    public void setBillImageUrl(String billImageUrl) {
        this.billImageUrl = billImageUrl;
    }

    public String getBillAddNote() {
        return billAddNote;
    }

    public void setBillAddNote(String billAddNote) {
        this.billAddNote = billAddNote;
    }

    public HashMap<String,Object> getBillText() {
        return billText;
    }

    public void setBillText(HashMap<String,Object> billText) {
        this.billText = billText;
    }
}
