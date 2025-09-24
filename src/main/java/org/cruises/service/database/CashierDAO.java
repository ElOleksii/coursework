package org.cruises.service.database;

import org.cruises.model.Cashier;
import org.cruises.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CashierDAO implements BaseDAO<Cashier> {

    public boolean save(Cashier cashier) throws SQLException {
        String sql = "INSERT INTO cashier (Name, PhoneNumber, OrganizationName, Shift) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, cashier.getFullName());
            stmt.setString(2, cashier.getPhoneNumber());
            stmt.setString(3, cashier.getOrganizationName());
            stmt.setBoolean(4, cashier.isShift());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    cashier.setCashierId(generatedKeys.getInt(1));
                }
            }

            return true;
        }
    }


    @Override
    public List<Cashier> getAll() throws SQLException {
        List<Cashier> list = new ArrayList<>();
        String sql = "SELECT * FROM Cashier";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cashier c = new Cashier();
                c.setId(rs.getInt("CashierId"));
                c.setFullName(rs.getString("Name"));
                c.setPhoneNumber(rs.getString("PhoneNumber"));
                c.setOrganizationName(rs.getString("OrganizationName"));
                c.setShift(rs.getBoolean("Shift"));
                list.add(c);
            }
        }

        return list;
    }

    @Override
    public boolean delete(Cashier cashier) throws SQLException {
        String sql = "DELETE FROM Cashier WHERE cashierId=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cashier.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(Cashier cashier) throws SQLException {
        String sql = "UPDATE Cashier SET Name=?, PhoneNumber=?, OrganizationName=?, Shift=? WHERE cashierId=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cashier.getFullName());
            stmt.setString(2, cashier.getPhoneNumber());
            stmt.setString(3, cashier.getOrganizationName());
            stmt.setBoolean(4, cashier.isShift());
            stmt.setInt(5, cashier.getId());
            return stmt.executeUpdate() > 0;
        }
    }


}
