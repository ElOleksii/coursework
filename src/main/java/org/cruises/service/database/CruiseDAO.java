package org.cruises.service.database;

import org.cruises.model.Cruise;
import org.cruises.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CruiseDAO implements BaseDAO<Cruise> {

    @Override
    public boolean save(Cruise cruise) throws SQLException {
        String sql = "INSERT INTO Cruise (RouteId, StartDate, EndDate, CruiseName, BasicPrice, ShipId) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cruise.getRouteId());
            stmt.setDate(2, cruise.getStartDate());
            stmt.setDate(3, cruise.getEndDate());
            stmt.setString(4, cruise.getCruiseName());
            stmt.setFloat(5, cruise.getBasicPrice());
            stmt.setInt(6, cruise.getShipId());  // NEW

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public List<Cruise> getAll() throws SQLException {
        List<Cruise> cruises = new ArrayList<>();

        String sql = """
        SELECT c.CruiseId, c.RouteId, c.StartDate, c.EndDate, c.CruiseName, c.BasicPrice, c.ShipId,
               r.DeparturePort, r.ArrivalPort,
               s.Name AS ShipName
        FROM Cruise c
        JOIN Route r ON c.RouteId = r.RouteId
        JOIN Ship s ON c.ShipId = s.ShipId
    """;

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cruise cruise = new Cruise();
                cruise.setCruiseId(rs.getInt("CruiseId"));
                cruise.setRouteId(rs.getInt("RouteId"));
                cruise.setStartDate(rs.getDate("StartDate"));
                cruise.setEndDate(rs.getDate("EndDate"));
                cruise.setCruiseName(rs.getString("CruiseName"));
                cruise.setBasicPrice(rs.getFloat("BasicPrice"));

                cruise.setShipId(rs.getInt("ShipId"));
                cruise.setShipName(rs.getString("ShipName"));

                String routeDesc = rs.getString("DeparturePort") + " - " + rs.getString("ArrivalPort");
                cruise.setRouteDescription(routeDesc);

                cruises.add(cruise);
            }
        }

        return cruises;
    }

    @Override
    public boolean delete(Cruise cruise) throws SQLException {
        String sql = "DELETE FROM Cruise WHERE CruiseId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cruise.getCruiseId());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(Cruise cruise) throws SQLException {
        String sql = "UPDATE Cruise SET RouteId = ?, StartDate = ?, EndDate = ?, CruiseName = ?, BasicPrice = ?, ShipId = ? WHERE CruiseId = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cruise.getRouteId());
            stmt.setDate(2, cruise.getStartDate());
            stmt.setDate(3, cruise.getEndDate());
            stmt.setString(4, cruise.getCruiseName());
            stmt.setFloat(5, cruise.getBasicPrice());
            stmt.setInt(6, cruise.getShipId());  // NEW
            stmt.setInt(7, cruise.getCruiseId());

            return stmt.executeUpdate() > 0;
        }
    }



}
