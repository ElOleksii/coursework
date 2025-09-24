package org.cruises.model.logic;

import org.cruises.model.Ship;
import org.cruises.model.Stateroom;
import org.cruises.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MapStateroom {
    public void markReserved(int stateroomId) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE Stateroom SET IsReserved = TRUE WHERE StateroomId = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, stateroomId);
                stmt.executeUpdate();
            }
        }
    }

    public List<Stateroom> findAvailableByShip(int shipId) throws SQLException {
        List<Stateroom> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM Stateroom WHERE ShipId = ? AND IsReserved = FALSE";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, shipId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Stateroom s = new Stateroom();
                    s.setStateroomId(rs.getInt("StateroomId"));
                    s.setStateroomClass(rs.getString("StateroomClass"));
                    s.setCapacity(rs.getInt("Capacity"));
                    s.setPrice((int) rs.getDouble("Price"));
                    s.setReserved(rs.getBoolean("IsReserved"));
                    s.setShipId(rs.getInt("ShipId"));
                    list.add(s);
                }
            }
        }
        return list;
    }
}