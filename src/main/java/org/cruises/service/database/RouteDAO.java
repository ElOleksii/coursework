package org.cruises.service.database;

import org.cruises.model.Route;
import org.cruises.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RouteDAO implements BaseDAO<Route> {

    @Override
    public boolean save(Route route) throws SQLException {
        String sql = "INSERT INTO route (DeparturePort, ArrivalPort) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, route.getDeparturePort());
            stmt.setString(2, route.getArrivalPort());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        route.setRouteId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        }
    }

    @Override
    public List<Route> getAll() throws SQLException {
        List<Route> routes = new ArrayList<>();
        String sql = "SELECT RouteId, DeparturePort, ArrivalPort FROM route";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Route route = new Route();
                route.setRouteId(rs.getInt("RouteId"));
                route.setDeparturePort(rs.getString("DeparturePort"));
                route.setArrivalPort(rs.getString("ArrivalPort"));
                routes.add(route);
            }
        }
        return routes;
    }

    @Override
    public boolean delete(Route route) throws SQLException {
        String sql = "DELETE FROM route WHERE RouteId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, route.getRouteId());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean update(Route route) throws SQLException {
        String sql = "UPDATE route SET DeparturePort = ?, ArrivalPort = ? WHERE RouteId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, route.getDeparturePort());
            stmt.setString(2, route.getArrivalPort());
            stmt.setInt(3, route.getRouteId());

            return stmt.executeUpdate() > 0;
        }
    }

    public Route findById(int routeId) throws SQLException {
        String sql = "SELECT RouteId, DeparturePort, ArrivalPort FROM route WHERE RouteId = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, routeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Route route = new Route();
                    route.setRouteId(rs.getInt("RouteId"));
                    route.setDeparturePort(rs.getString("DeparturePort"));
                    route.setArrivalPort(rs.getString("ArrivalPort"));
                    return route;
                }
            }
        }
        return null;
    }



}
