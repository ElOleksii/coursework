package org.cruises.service.database;

import org.cruises.model.Stateroom;
import org.cruises.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StateroomDAO implements BaseDAO<Stateroom> {

    @Override
    public boolean save(Stateroom stateroom) throws SQLException {
        String sql = "INSERT INTO Stateroom (StateroomClass, Capacity, Price, ShipId, IsReserved) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, stateroom.getStateroomClass());
            stmt.setInt(2, stateroom.getCapacity());
            stmt.setInt(3, stateroom.getPrice());
            stmt.setInt(4, stateroom.getShipId());
            stmt.setBoolean(5, stateroom.isReserved());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    stateroom.setStateroomId(generatedKeys.getInt(1));
                }
            }

            return true;
        }
    }


    @Override
    public List<Stateroom> getAll() throws SQLException {
        List<Stateroom> list = new ArrayList<>();
        String sql = "SELECT * FROM Stateroom";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Stateroom s = new Stateroom();
                s.setStateroomId(rs.getInt("StateroomId"));
                s.setStateroomClass(rs.getString("StateroomClass"));
                s.setCapacity(rs.getInt("Capacity"));
                s.setPrice(rs.getInt("Price"));
                s.setShipId(rs.getInt("ShipId"));
                s.setReserved(rs.getBoolean("IsReserved"));
                list.add(s);
            }
        }

        return list;
    }

    @Override
    public boolean delete(Stateroom stateroom) throws SQLException {
        String sql = "DELETE FROM Stateroom WHERE StateroomId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, stateroom.getStateroomId());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(Stateroom stateroom) throws SQLException {
        String sql = "UPDATE Stateroom SET StateroomClass = ?, Capacity = ?, Price = ?, ShipId = ?, IsReserved = ? WHERE StateroomId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, stateroom.getStateroomClass());
            stmt.setInt(2, stateroom.getCapacity());
            stmt.setInt(3, stateroom.getPrice());
            stmt.setInt(4, stateroom.getShipId());
            stmt.setBoolean(5, stateroom.isReserved());
            stmt.setInt(6, stateroom.getStateroomId());

            return stmt.executeUpdate() > 0;
        }
    }
}
