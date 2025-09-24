package org.cruises.model.logic;

import org.cruises.model.*;
import org.cruises.utils.DBConnection;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Register {
    private final Calc calc;
    private final MapStateroom mapStateroom;

    public Register() {
        this.calc = new Calc();
        this.mapStateroom = new MapStateroom();
    }



    public void generateTicketPDF(Passenger passenger, Cruise cruise, Stateroom cabin, String filePath) throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Font regularFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

        document.add(new Paragraph("Cruise Ticket", titleFont));
        document.add(new Paragraph(" "));

        document.add(new Paragraph("Passenger Information", titleFont));
        document.add(new Paragraph("Name: " + passenger.getFullName(), regularFont));
        document.add(new Paragraph(" "));

        document.add(new Paragraph("Cruise Information", titleFont));
        document.add(new Paragraph("Cruise Name: " + cruise.getCruiseName(), regularFont));
        document.add(new Paragraph("Route: " + cruise.getRouteDescription(), regularFont));
        document.add(new Paragraph(" "));

        document.add(new Paragraph("Stateroom Information", titleFont));
        document.add(new Paragraph("Room Number: " + cabin.getStateroomId(), regularFont));
        document.add(new Paragraph("Class: " + cabin.getStateroomClass(), regularFont));
        document.add(new Paragraph(" "));

        document.add(new Paragraph("Thank you for choosing our cruise service!", regularFont));

        document.close();
    }


    public void createTicket(int passengerId, int cruiseId, Stateroom cabin, Cashier cashier, Cruise cruise) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            String orderSQL = "INSERT INTO Orders (PassengerId, Date, Status) VALUES (?, NOW(), ?)";
            PreparedStatement orderStmt = conn.prepareStatement(orderSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            orderStmt.setInt(1, passengerId);
            orderStmt.setString(2, "Completed");
            orderStmt.executeUpdate();

            var rs = orderStmt.getGeneratedKeys();
            int orderId = -1;
            if (rs.next()) {
                orderId = rs.getInt(1);
            }

            double price = calc.calculateTotalPrice(cabin, cruise);

            String ticketSQL = "INSERT INTO Ticket (PassengerId, CruiseId, TotalPrice, CashierId) VALUES (?, ?, ?, ?)";
            PreparedStatement ticketStmt = conn.prepareStatement(ticketSQL);
            ticketStmt.setInt(1, passengerId);
            ticketStmt.setInt(2, cruiseId);
            ticketStmt.setDouble(3, price);
            ticketStmt.setInt(4, cashier.getCashierId());
            ticketStmt.executeUpdate();
        }
    }

}
