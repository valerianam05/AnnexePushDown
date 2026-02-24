package org.spring;

public record InvoiceStatusTotals(double totalPaid, double totalConfirmed, double totalDraft) {
    @Override
    public String toString() {
        return String.format("total_paid = %.2f\ntotal_confirmed = %.2f\ntotal_draft = %.2f",
                totalPaid, totalConfirmed, totalDraft);
    }
}