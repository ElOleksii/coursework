package org.cruises.controller.cruise;

import org.cruises.model.Cruise;
import org.cruises.model.Route;
import org.cruises.model.Ship;
import org.cruises.service.database.CruiseDAO;
import org.cruises.service.database.RouteDAO;
import org.cruises.service.database.ShipDAO;

import java.sql.SQLException;
import java.util.List;

public class CruiseService {
    private final CruiseDAO cruiseDAO = new CruiseDAO();
    private final RouteDAO routeDAO = new RouteDAO();
    private final ShipDAO shipDAO = new ShipDAO();

    public List<Cruise> getAllCruises() throws SQLException {
        return cruiseDAO.getAll();
    }

    public List<Route> getAllRoutes() throws SQLException {
        return routeDAO.getAll();
    }

    public List<Ship> getAllShips() throws SQLException {
        return shipDAO.getAll();
    }

    public boolean saveCruise(Cruise cruise) throws SQLException {
        return cruiseDAO.save(cruise);
    }

    public boolean updateCruise(Cruise cruise) throws SQLException {
        return cruiseDAO.update(cruise);
    }

    public void deleteCruise(Cruise cruise) throws SQLException {
        cruiseDAO.delete(cruise);
    }
}

