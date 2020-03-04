package com.hamzaazam.fyp_frontend.Model;

public class ReceiptM {

    public String recId;


    public String recCategory;
    public String recDate;
    public Double recAmount;
    public String recName;
    public String recDesc;

    public String recImageUrl;


    public String recRawOCR;


    public ReceiptM(){}


    public String getRecId() {
        return recId;
    }

    public void setRecId(String recId) {
        this.recId = recId;
    }




    public String getRecCategory() {
        return recCategory;
    }

    public void setRecCategory(String recCategory) {
        this.recCategory = recCategory;
    }

    public String getRecDate() {
        return recDate;
    }

    public void setRecDate(String recDate) {
        this.recDate = recDate;
    }

    public Double getRecAmount() {
        return recAmount;
    }

    public void setRecAmount(Double recAmount) {
        this.recAmount = recAmount;
    }

    public String getRecName() {
        return recName;
    }

    public void setRecName(String recName) {
        this.recName = recName;
    }

    public String getRecDesc() {
        return recDesc;
    }

    public void setRecDesc(String recDesc) {
        this.recDesc = recDesc;
    }

    public String getRecImageUrl() {
        return recImageUrl;
    }

    public void setRecImageUrl(String recImageUrl) {
        this.recImageUrl = recImageUrl;
    }

    public String getRecRawOCR() {
        return recRawOCR;
    }

    public void setRecRawOCR(String recRawOCR) {
        this.recRawOCR = recRawOCR;
    }

    public ReceiptM(String category, Double amount, String date, String imageURL, String description, String rawOCR, String name){
        recCategory= category;
        recAmount=amount;
        recDesc = description;
        recName = name;
        recDate=date;
        recImageUrl =imageURL;

        recRawOCR= rawOCR;

    }

}
