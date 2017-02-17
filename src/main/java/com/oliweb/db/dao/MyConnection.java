package com.oliweb.db.dao;

import com.oliweb.utility.Proprietes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyConnection {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static volatile Connection instance = null;

    private MyConnection() {
    }

    public static Connection getInstance() {

        String dbUrl = Proprietes.getProperty(Proprietes.DB_URL);
        String dbUser = Proprietes.getProperty(Proprietes.DB_USER);
        String dbPass = Proprietes.getProperty(Proprietes.DB_PASS);
        String dbClass = Proprietes.getProperty(Proprietes.DB_CLASS);

		if (MyConnection.instance == null){
			synchronized(MyConnection.class) {
				if (MyConnection.instance == null) {
					try {
                        Class.forName(dbClass);
                    } catch (ClassNotFoundException e) {
                        LOGGER.log(Level.SEVERE, e.getMessage(), e);
                    }

					try {
                        MyConnection.instance = DriverManager.getConnection(dbUrl, dbUser, dbPass);
                    } catch (SQLException e) {
                        LOGGER.log(Level.SEVERE, e.getMessage(), e);
                    }
				}
			}
		}else{
			boolean closed = false;
			try {
				closed = MyConnection.instance.isClosed();
			} catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }

			if (closed){
				try {
                    MyConnection.instance = DriverManager.getConnection(dbUrl, dbUser, dbPass);
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, e.getMessage(), e);
                }
			}
		}
		return MyConnection.instance;
	}
}