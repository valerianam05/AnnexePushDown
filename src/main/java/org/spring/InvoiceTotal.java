package org.spring;

public class InvoiceTotal {
    private String customerName;
    private double totalAmount;
    private String status;

    public InvoiceTotal(String customerName, double totalAmount, String status) {
        this.customerName = customerName;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public String getCustomerName() { return customerName; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
}