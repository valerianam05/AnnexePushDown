package org.spring;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

    // throw et throws

public class DataRetriever {


    public List<InvoiceTotal> getConfirmedInvoiceTotals() {
        List<InvoiceTotal> totals = new ArrayList<>();

        // La requête filtre par statut et on récupère i.status pour l'affichage
        String sql = """
            SELECT 
                i.customer_name, 
                i.status,
                SUM(il.quantity * il.unit_price) as total_invoice
            FROM invoice i
            JOIN invoice_line il ON i.id = il.invoice_id
            WHERE i.status IN ('PAID', 'CONFIRMED')
            GROUP BY i.id, i.customer_name, i.status
        """;

        DBConnection dbConnection = new DBConnection();

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // On envoie 3 arguments : Nom, Montant, Statut
                totals.add(new InvoiceTotal(
                        rs.getString("customer_name"),
                        rs.getDouble("total_invoice"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error in Confirmation InvoiceTotals  : " + e.getMessage());
        }
        return totals;
    }

    public List<InvoiceTotal> getInvoiceTotals() {

        List<InvoiceTotal> totals = new ArrayList<>();

        String sql = """
            SELECT 
                i.customer_name, 
                i.status,
                SUM(il.quantity * il.unit_price) as total_invoice
            FROM invoice i
            JOIN invoice_line il ON i.id = il.invoice_id
            GROUP BY i.id, i.customer_name, i.status
        """;

        DBConnection dbConnection = new DBConnection();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                totals.add(new InvoiceTotal(
                        rs.getString("customer_name"),
                        rs.getDouble("total_invoice"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error in invoice count : " + e.getMessage());
        }
        return totals;
    }

}