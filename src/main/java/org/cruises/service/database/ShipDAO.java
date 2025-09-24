package org.cruises.service.database;

import org.cruises.model.Ship;
import org.cruises.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShipDAO implements BaseDAO<Ship> {

    @Override
    public boolean save(Ship ship) throws SQLException {
        String sql = "INSERT INTO Ship (Name, StateroomCount) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, ship.getShipName());
            stmt.setInt(2, ship.getStateroomCount());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        ship.setShipId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }

            return false;
        }
    }

    @Override
    public List<Ship> getAll() throws SQLException {
        List<Ship> ships = new ArrayList<>();
        String sql = "SELECT * FROM Ship";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Ship ship = new Ship();
                ship.setShipId(rs.getInt("ShipId"));
                ship.setShipName(rs.getString("Name"));
                ship.setStateroomCount(rs.getInt("StateroomCount"));
                ships.add(ship);
            }
        }

        return ships;
    }

    @Override
    public boolean delete(Ship ship) throws SQLException {
        String sql = "DELETE FROM Ship WHERE ShipId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ship.getShipId());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(Ship ship) throws SQLException {
        String sql = "UPDATE Ship SET Name = ?, StateroomCount = ? WHERE ShipId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ship.getShipName());
            stmt.setInt(2, ship.getStateroomCount());
            stmt.setInt(3, ship.getShipId());

            return stmt.executeUpdate() > 0;
        }
    }

    public Ship findById(int shipId) throws SQLException {
        String sql = "SELECT ShipId, ShipName, StateroomCount FROM ship WHERE ShipId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, shipId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Ship ship = new Ship();
                    ship.setShipId(rs.getInt("ShipId"));
                    ship.setShipName(rs.getString("ShipName"));
                    ship.setStateroomCount(rs.getInt("StateroomCount"));
                    return ship;
                }
            }
        }
        return null;
    }

}
