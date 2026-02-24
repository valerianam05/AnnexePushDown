package org.spring;

import java.math.BigDecimal;


public class Main {
    public static void main(String[] args) {
        DataRetriever retriever = new DataRetriever();


        System.out.println("\nQ5-B) Chiffre d’affaires TTC pondéré");
        BigDecimal turnoverTtc = retriever.computeWeightedTurnoverTtc();
        System.out.printf("%.2f%n", turnoverTtc);

//        System.out.println("\n--- RÉSULTAT QUESTION 05 (TVA & TTC) ---");
//        List<InvoiceTaxSummary> summaries = retriever.findInvoiceTaxSummaries();
//
//        for (int i = 0; i < summaries.size(); i++) {
//            InvoiceTaxSummary s = summaries.get(i);
//            // Affichage : 1 | Nom | HT 0.00 | TVA 0.00 | TTC 0.00
//            System.out.printf("%d | %s | HT %.2f | TVA %.2f | TTC %.2f%n",
//                    (i + 1),
//                    s.customerName(),
//                    s.totalHT(),
//                    s.totalTVA(),
//                    s.totalTTC());
//        }

            // Affichage Q3
//            System.out.println("Q3 - Totaux cumulés par statut");
//            InvoiceStatusTotals statusTotals = retriever.computeStatusTotals();
//            System.out.println(statusTotals); // Utilise le toString() du record
//
//            // Affichage Q4
//            System.out.println("\nQ4 - Chiffre d’affaires pondéré");
//            Double weightedTurnover = retriever.computeWeightedTurnover();
//            System.out.printf("%.2f%n", weightedTurnover);
        }


//        System.out.println("findInvoiceTotals() :");
//        List<InvoiceTotal> q1 = retriever.getInvoiceTotals();
//        for (int i = 0; i < q1.size(); i++) {
//            InvoiceTotal t = q1.get(i);
//            // Format : 1 | Alice | 250.00
//            System.out.printf("%d | %s | %.2f%n", (i + 1), t.getCustomerName(), t.getTotalAmount());
//        }
//
//        System.out.println("\nQ2 - Total des factures confirmées et payées");
//        System.out.println("Résultat attendu lors de l'affichage dans le Main :");
//
//        // --- AFFICHAGE QUESTION 02 ---
//        List<InvoiceTotal> q2 = retriever.getConfirmedInvoiceTotals();
//        for (int i = 0; i < q2.size(); i++) {
//            InvoiceTotal t = q2.get(i);
//            // Format avec Status : 1 | Alice | CONFIRMED | 250.00
//            System.out.printf("%d | %s | %s | %.2f%n",
//                    (i + 1),
//                    t.getCustomerName(),
//                    t.getStatus(),
//                    t.getTotalAmount());
//        }

}