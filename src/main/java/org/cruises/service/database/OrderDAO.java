package org.cruises.service.database;

import org.cruises.model.Order;
import org.cruises.utils.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO implements BaseDAO<Order> {

    @Override
    public boolean save(Order order) throws SQLException {
        String sql = "INSERT INTO orders (passengerId, Date, Status) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, order.getPassengerId());
            stmt.setTimestamp(2, Timestamp.valueOf(order.getOrderDateTime()));
            stmt.setString(3, order.getOrderStatus());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        order.setOrderId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    @Override
    public List<Order> getAll() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("orderId"));
                order.setPassengerId(rs.getInt("passengerId"));
                order.setOrderDateTime(rs.getTimestamp("Date").toLocalDateTime());
                order.setOrderStatus(rs.getString("Status"));
                orders.add(order);
            }
        }

        return orders;
    }

    @Override
    public boolean delete(Order order) throws SQLException {
        String sql = "DELETE FROM orders WHERE orderId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, order.getOrderId());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(Order order) throws SQLException {
        String sql = "UPDATE orders SET passengerId = ?, Date = ?, Status = ? WHERE orderId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, order.getPassengerId());
            stmt.setTimestamp(2, Timestamp.valueOf(order.getOrderDateTime()));
            stmt.setString(3, order.getOrderStatus());
            stmt.setInt(4, order.getOrderId());

            return stmt.executeUpdate() > 0;
        }
    }
}
