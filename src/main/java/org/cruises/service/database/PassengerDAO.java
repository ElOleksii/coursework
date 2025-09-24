package org.cruises.service.database;

import org.cruises.model.Passenger;
import org.cruises.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PassengerDAO implements BaseDAO<Passenger> {

    @Override
    public boolean save(Passenger passenger) throws SQLException {
        String sql = "INSERT INTO Passenger (FullName, PhoneNumber) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, passenger.getFullName());
            stmt.setString(2, passenger.getPhoneNumber());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        passenger.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }

            return false;
        }
    }


    @Override
    public List<Passenger> getAll() throws SQLException {
        List<Passenger> passengers = new ArrayList<>();
        String sql = "SELECT * FROM Passenger";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Passenger p = new Passenger();
                p.setId(rs.getInt("PassengerId")); // Перевір ім'я колонки id у таблиці
                p.setFullName(rs.getString("FullName"));
                p.setPhoneNumber(rs.getString("PhoneNumber"));
                passengers.add(p);
            }
        }

        return passengers;
    }

    @Override
    public boolean delete(Passenger passenger) throws SQLException {
        String sql = "DELETE FROM Passenger WHERE PassengerId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, passenger.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(Passenger passenger) throws SQLException {
        String sql = "UPDATE Passenger SET FullName = ?, PhoneNumber = ? WHERE PassengerId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, passenger.getFullName());
            stmt.setString(2, passenger.getPhoneNumber());
            stmt.setInt(3, passenger.getId());

            return stmt.executeUpdate() > 0;
        }
    }
}
