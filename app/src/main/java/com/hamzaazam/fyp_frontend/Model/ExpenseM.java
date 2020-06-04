package com.hamzaazam.fyp_frontend.Model;

public class ExpenseM {

    public String expenseId;
    public String expenseItem;
    public String expenseDate;
    public String expenseAmount;
    public String expenseCategory;

    public ExpenseM(){}

    public ExpenseM( String expenseItem, String expenseDate, String expenseAmount, String expenseCategory) {
        this.expenseItem = expenseItem;
        this.expenseDate = expenseDate;
        this.expenseAmount = expenseAmount;
        this.expenseCategory = expenseCategory;
    }

    public String getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(String expenseId) {
        this.expenseId = expenseId;
    }

    public String getExpenseItem() {
        return expenseItem;
    }

    public void setExpenseItem(String expenseItem) {
        this.expenseItem = expenseItem;
    }

    public String getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(String expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(String expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(String expenseCategory) {
        this.expenseCategory = expenseCategory;
    }
}
