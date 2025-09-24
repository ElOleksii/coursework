package org.cruises.service;

import org.cruises.utils.DBConnection;

import java.sql.*;

public class CruiseQueries {

    public ResultSet getPassengersByCashier(String cashierName) throws SQLException {
        String sql = "SELECT DISTINCT p.FullName FROM Passenger p " +
                "JOIN Ticket t ON p.PassengerId = t.PassengerId " +
                "JOIN Cashier c ON c.CashierId = t.CashierId " +
                "WHERE c.Name = ? " +
                "ORDER BY p.FullName";
        Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, cashierName);
        return stmt.executeQuery();
    }

    public ResultSet getPassengersByNameLike(String namePattern) throws SQLException {
        String sql = "SELECT * FROM Passenger WHERE FullName LIKE ?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, namePattern + "%"); // наприклад, щоб знайти імена що починаються з шаблону
        return stmt.executeQuery();
    }

    public ResultSet getCruisesInDateRange(String fromDate, String toDate) throws SQLException {
        String sql = "SELECT * FROM Cruise WHERE StartDate BETWEEN ? AND ?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, fromDate);
        stmt.setString(2, toDate);
        return stmt.executeQuery();
    }

    public ResultSet getOrdersLastWeek() throws SQLException {
        String sql = "SELECT COUNT(*) AS order_count_last_week\n" +
                "FROM Orders\n" +
                "WHERE Date >= NOW() - INTERVAL 7 DAY;";
        Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        return stmt.executeQuery();
    }

    public ResultSet getTicketCountByCashier() throws SQLException {
        String sql = "SELECT c.Name, COUNT(*) AS SoldTickets FROM Ticket t " +
                "JOIN Cashier c ON c.CashierId = t.CashierId " +
                "GROUP BY c.Name";
        Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        return stmt.executeQuery();
    }

    public ResultSet getMaxSellerCashier() throws SQLException {
        String sql = "SELECT c.Name\n" +
                "FROM Cashier c\n" +
                "JOIN Ticket t ON c.CashierId = t.CashierId\n" +
                "GROUP BY c.CashierId, c.Name\n" +
                "HAVING COUNT(t.TicketId) >= ALL (\n" +
                "    SELECT COUNT(*)\n" +
                "    FROM Ticket\n" +
                "    GROUP BY CashierId\n" +
                ")";
        Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        return stmt.executeQuery();
    }

    public ResultSet getMaxPriceCruisePerRoute() throws SQLException {
        String sql = "SELECT c1.CruiseName, c1.RouteId, c1.BasicPrice FROM Cruise c1 " +
                "WHERE c1.BasicPrice = (SELECT MAX(c2.BasicPrice) FROM Cruise c2 WHERE c2.RouteId = c1.RouteId)";
        Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        return stmt.executeQuery();
    }

    public ResultSet getShipsWithoutCruisesLastWeek_LeftJoin() throws SQLException {
        String sql = """
        SELECT s.Name
        FROM Ship s
        LEFT JOIN Cruise c ON s.ShipId = c.ShipId AND c.StartDate >= CURDATE() - INTERVAL 7 DAY
        WHERE c.CruiseId IS NULL
    """;
        Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        return stmt.executeQuery();
    }

    public ResultSet getShipsWithoutCruisesLastWeek_NotIn() throws SQLException {
        String sql = """
        SELECT Name FROM Ship
        WHERE ShipId NOT IN (
            SELECT DISTINCT ShipId FROM Cruise WHERE StartDate >= CURDATE() - INTERVAL 7 DAY
        )
    """;
        Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        return stmt.executeQuery();
    }

    public ResultSet getShipsWithoutCruisesLastWeek_NotExists() throws SQLException {
        String sql = """
        SELECT s.Name
        FROM Ship s
        WHERE NOT EXISTS (
            SELECT 1 FROM Cruise c WHERE c.ShipId = s.ShipId AND c.StartDate >= CURDATE() - INTERVAL 7 DAY
        )
    """;
        Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        return stmt.executeQuery();
    }


    public ResultSet getCruiseUsageActivityUnion() throws SQLException {
        String sql = """
        SELECT c.CruiseName, 'Активний круїз' AS Comment
        FROM Cruise c
        WHERE c.StartDate >= CURDATE() - INTERVAL 7 DAY
        UNION
        SELECT c.CruiseName, 'Неактивний круїз' AS Comment
        FROM Cruise c
        WHERE c.StartDate < CURDATE() - INTERVAL 7 DAY
        """;
        Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        return stmt.executeQuery();
    }



}
