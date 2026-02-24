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
    public BigDecimal computeWeightedTurnoverTtc() {
        BigDecimal result = BigDecimal.ZERO;

        String sql = """
        SELECT 
            SUM(
                (il.quantity * il.unit_price) * (1 + t.rate / 100.0) * CASE 
                    WHEN i.status = 'PAID' THEN 1.0
                    WHEN i.status = 'CONFIRMED' THEN 0.5
                    ELSE 0 
                END
            ) as total_ttc_weighted
        FROM invoice i
        JOIN invoice_line il ON i.id = il.invoice_id
        CROSS JOIN tax_config t
        WHERE t.label = 'TVA STANDARD'
    """;
// On peut utilsier le methode qui calcule le HT et ce création de view en BD(correction)
        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                result = rs.getBigDecimal("total_ttc_weighted");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<InvoiceTaxSummary> findInvoiceTaxSummaries() {
        List<InvoiceTaxSummary> summaries = new ArrayList<>();

        String sql = """
        SELECT 
            i.customer_name,
            SUM(il.quantity * il.unit_price) as total_ht,
            SUM(il.quantity * il.unit_price) * (t.rate / 100.0) as total_tva,
            SUM(il.quantity * il.unit_price) * (1 + t.rate / 100.0) as total_ttc
        FROM invoice i
            JOIN invoice_line il ON i.id = il.invoice_id
            CROSS JOIN tax_config t 
        GROUP BY i.id, i.customer_name, t.rate
    """;
    // il y a ce BY ID on peut recuperer , tax_config (c'est à dire à traver
        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                summaries.add(new InvoiceTaxSummary(
                        rs.getString("customer_name"),
                        rs.getDouble("total_ht"),
                        rs.getDouble("total_tva"),
                        rs.getDouble("total_ttc")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return summaries;
    }

    public Double computeWeightedTurnover() {
        Double turnover = 0.0;
        // Push-down : le calcul (1.0 * PAID + 0.5 * CONFIRMED) est fait par la base de données
        String sql = """
        SELECT 
            SUM(CASE 
                WHEN i.status = 'PAID' THEN (il.quantity * il.unit_price) * 1.0
                WHEN i.status = 'CONFIRMED' THEN (il.quantity * il.unit_price) * 0.5
                ELSE 0 
            END) as weighted_total
        FROM invoice i 
        JOIN invoice_line il ON i.id = il.invoice_id; 
   
    """;
        //pourqoi on doit faire *0 car loresque c'est draft pas comme ça donc  else*0.5
         // et si non par defaut c'est 0 sur le FRADT pn se pas comment o0 else 0 end
         //JOIN invoice_line il ON i.id = il.invoice_id 1<->1
        //il y a ce methode

        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                turnover = rs.getDouble("weighted_total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return turnover;
    }

    public InvoiceStatusTotals computeStatusTotals() {
        String sql = """
        SELECT 
            SUM(CASE WHEN i.status = 'PAID' THEN il.quantity * il.unit_price ELSE 0 END) as paid,
            SUM(CASE WHEN i.status = 'CONFIRMED' THEN il.quantity * il.unit_price ELSE 0 END) as confirmed,
            SUM(CASE WHEN i.status = 'DRAFT' THEN il.quantity * il.unit_price ELSE 0 END) as draft
        FROM invoice i
        JOIN invoice_line il ON i.id = il.invoice_id
    """;

        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return new InvoiceStatusTotals(
                        rs.getDouble("paid"),
                        rs.getDouble("confirmed"),
                        rs.getDouble("draft")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error in compute Status : " + e.getMessage());
        }
        return new InvoiceStatusTotals(0, 0, 0);
    }

    public List<InvoiceTotal> getInvoiceTotalsByStatus(String status) {
        List<InvoiceTotal> totals = new ArrayList<>();

        String sql = """
        SELECT i.customer_name, i.status, SUM(il.quantity * il.unit_price) as total_invoice
        FROM invoice i
        JOIN invoice_line il ON i.id = il.invoice_id
        WHERE i.status = ?
        GROUP BY i.id, i.customer_name, i.status
    """;

        // le resultat doit être en seule ligne  sum( case when = 'PAID' the invooice_line
        //SUM( case when = 'PAID' the invoice_line il.quantity * il.unit_price)
         //from invoice_line
        try (Connection conn = new DBConnection().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    totals.add(new InvoiceTotal(
                            rs.getString("customer_name"),
                            rs.getDouble("total_invoice"),
                            rs.getString("status")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error in Invoice Totals ByStatus : " + e.getMessage());
        }
        return totals;
    }

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