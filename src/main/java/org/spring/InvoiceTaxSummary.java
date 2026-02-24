package org.spring;

public record InvoiceTaxSummary(String customerName, double totalHT, double totalTVA, double totalTTC) {
    @Override
    public String toString() {
        return String.format("%s | HT %.2f | TVA %.2f | TTC %.2f",
                customerName, totalHT, totalTVA, totalTTC);
    }
}
