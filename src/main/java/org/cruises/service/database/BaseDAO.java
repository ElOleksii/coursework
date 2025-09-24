package org.cruises.service.database;

import java.sql.SQLException;
import java.util.List;

public interface BaseDAO<T> {
    boolean save(T item) throws SQLException;
    List<T> getAll() throws SQLException;
    boolean delete(T item) throws SQLException;
    boolean update(T item) throws SQLException;
}
