package org.kente.loans;

import java.util.Date;

public class Loan {

    private String id;
    private Customer customer;
    private int branchCode;
    private String passcode;
    private Double amount;

    public Loan(String id, Customer customer, String passcode, int branchCode, Double amount) {
        this.id = id;
        this.customer = customer;
        this.passcode = passcode;
        this.branchCode = branchCode;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    public int getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(int branchCode) {
        this.branchCode = branchCode;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id='" + id + '\'' +
                ", customer=" + customer +
                ", branchCode=" + branchCode +
                ", passcode='" + passcode + '\'' +
                ", amount=" + amount +
                '}';
    }

    public void prettyPrint(){
        System.out.format("Here is the information of loan %9s:%n", id);
        System.out.println("-----------------------------------------");
        System.out.format("Customer name: %s %s %s%n",
                customer.getFirstName(), customer.getMiddleName(), customer.getLastName());
        System.out.format("Branch code: %d%n", branchCode);
        System.out.format("Gender: %s%n", customer.getGender().toString());
        System.out.format("Date of birth: %s%n", customer.getDateOfBirth().toString());
        System.out.format("Loan amount: %.2f%n", amount);
        System.out.format("Customer phone: %s%n", customer.getCustomerPhone());
    }
}
