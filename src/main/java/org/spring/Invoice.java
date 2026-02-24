package org.spring;

public class Invoice {
    private int id;
    private String customerName;
    private InvoiceStatus status;

    public Invoice(int id, String customerName, InvoiceStatus status) {
        this.id = id;
        this.customerName = customerName;
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", status=" + status +
                '}';
    }
}
