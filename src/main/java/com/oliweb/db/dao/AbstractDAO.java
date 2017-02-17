package com.oliweb.db.dao;

import java.sql.Connection;
import java.util.logging.Logger;

public abstract class AbstractDAO<T> implements InterfaceDAO<T> {

    protected static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    Connection dbConn;

    AbstractDAO(Connection dbConn) {
        this.dbConn = dbConn;
    }
}
