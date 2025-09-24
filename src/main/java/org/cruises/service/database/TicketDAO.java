package org.cruises.service.database;

import org.cruises.model.Ticket;
import org.cruises.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO implements BaseDAO<Ticket> {

    @Override
    public boolean save(Ticket ticket) throws SQLException {
        String sql = "INSERT INTO Ticket (PassengerId, CruiseId, CashierId, TotalPrice) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ticket.getPassengerId());
            stmt.setInt(2, ticket.getCruiseId());
            stmt.setInt(3, ticket.getCashierId());
            stmt.setDouble(4, (double) ticket.getTotalPrice());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public List<Ticket> getAll() throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT TicketId, PassengerId, CruiseId, CashierId, TotalPrice FROM Ticket";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Ticket t = new Ticket();
                t.setTicketId(rs.getInt("TicketId"));
                t.setPassengerId(rs.getInt("PassengerId"));
                t.setCruiseId(rs.getInt("CruiseId"));
                t.setCashierId(rs.getInt("CashierId"));
                t.setTotalPrice((float) rs.getDouble("TotalPrice"));

                tickets.add(t);
            }
        }

        return tickets;
    }

    @Override
    public boolean delete(Ticket ticket) throws SQLException {
        String sql = "DELETE FROM Ticket WHERE TicketId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ticket.getTicketId());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(Ticket ticket) throws SQLException {
        String sql = "UPDATE Ticket SET PassengerId = ?, CruiseId = ?, CashierId = ?, TotalPrice = ? WHERE TicketId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ticket.getPassengerId());
            stmt.setInt(2, ticket.getCruiseId());
            stmt.setInt(3, ticket.getCashierId());
            stmt.setDouble(4, (double) ticket.getTotalPrice());
            stmt.setInt(5, ticket.getTicketId());

            return stmt.executeUpdate() > 0;
        }
    }
}
