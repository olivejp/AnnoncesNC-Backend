package com.oliweb.DB.DAO;

import com.oliweb.DB.utility.Properties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyConnection {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static volatile Connection instance = null;

	/** 
	 * Cette m�thode ne sert que dans le cas ou on se sert du fichier META-INF/context.xml
	private static Connection initConnect(){
		Connection connection = null;
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:comp/env");
			DataSource ds = (DataSource) envContext.lookup("jdbc/annoncesRESTdb");
			connection = ds.getConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(MyConnection.class.toString() + e.getMessage());
			e.printStackTrace();
		}
		return connection;
	}
	 */

    public static Connection getInstance() {

        String dbUrl = Properties.getProperty(Properties.DB_URL);
        String dbUser = Properties.getProperty(Properties.DB_USER);
        String dbPass = Properties.getProperty(Properties.DB_PASS);
        String dbClass = Properties.getProperty(Properties.DB_CLASS);

        //Le "Double-Checked Connexion"/"Connexion doublement v�rifi�" permet
		//d'�viter un appel co�teux � synchronized, 
		//une fois que l'instanciation est faite.
		if (MyConnection.instance == null){
			// Le mot-cl� synchronized sur ce bloc emp�che toute instanciation
			// multiple m�me par diff�rents "threads".
			// Il est TRES important.
			synchronized(MyConnection.class) {
				if (MyConnection.instance == null) {


                    // Tentative de r�cup�ration d'une connexion
					try {
                        Class.forName(dbClass);
                    } catch (ClassNotFoundException e) {
                        LOGGER.log(Level.SEVERE, e.getMessage(), e);
                    }

					try {
                        // MyConnection.instance = DriverManager.getConnection(Properties.dbUrl, Properties.dbUser, Properties.dbPwd);

                        MyConnection.instance = DriverManager.getConnection(dbUrl, dbUser, dbPass);
                    } catch (SQLException e) {
                        LOGGER.log(Level.SEVERE, e.getMessage(), e);
                    }


					// Tentative de r�cup�ration d'une connexion
					// MyConnection.instance = initConnect();
				}
			}
			// On a bien une instance, mais si la connection est ferm�e alors on va la r�ouvrir.
		}else{
			// On regarde si la connection est ferm�e.
			boolean closed = false;
			try {
				closed = MyConnection.instance.isClosed();
			} catch (SQLException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }

			if (closed){
				// Nouvelle r�cup�ration
				// MyConnection.instance = initConnect();

				// Ancienne r�cup�ration
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