package com.oliweb.utility;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Proprietes {

    public static final String DB_USER = "DB_USER";
    public static final String DB_PASS = "DB_PASS";
    public static final String DB_CLASS = "DB_CLASS";
    public static final String DIRECTORY_IMAGE = "DIRECTORY_IMAGE";
    public static final String DIRECTORY_UPLOAD = "DIRECTORY_UPLOAD";
    public static final String PAGINATION = "PAGINATION";
    public static final String MAX_ANNONCE = "MAX_ANNONCE";
    public static final String ID_ALL_CATEGORIE = "ID_ALL_CATEGORIE";
    public static final String DB_NAME = "DB_NAME";
    public static final String DB_URL = "DB_URL";
    public static final String APPLI_NAME = "APPLI_NAME";
    public static final String SERVER_EXCEPTION = "SERVER_EXCEPTION";
    public static final String CRYPTO_PASS = "CRYPTO_PASS";
    public static final String EMAIL_USERNAME = "EMAIL_USERNAME";
    public static final String EMAIL_FROM = "EMAIL_FROM";
    public static final String EMAIL_PASS = "EMAIL_PASSWORD";
    public static final String SMTP_HOST = "SMTP_HOST";
    public static final String SMTP_PORT = "SMTP_PORT";
    public static final String DIFFRENT_NAME = "DIFFRENT_NAME";
    public static final String SAME_NAME = "SAME_NAME";
    public static final String PHOTO_ALREADY_EXIST = "PHOTO_ALREADY_EXIST";
    public static final String JWT_ISSUER = "JWT_ISSUER";
    public static final String JWT_SUBJECT_LOGIN = "JWT_SUBJECT_LOGIN";

    public static final String FICHIER_CONF = "conf.properties";
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static volatile java.util.Properties properties = null;

    private Proprietes() {
    }

    public static String getProperty(String key) {
        if (properties != null) {
            return properties.getProperty(key);
        }

        synchronized (Proprietes.class) {
            try {
                if (properties == null) {
                    Class.forName(Proprietes.class.getName());
                    properties = new java.util.Properties();
                    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                    InputStream propertiesStream = classLoader.getResourceAsStream(FICHIER_CONF);
                    if (propertiesStream != null) {
                        properties.load(propertiesStream);
                    }
                }
            } catch (ClassNotFoundException | IOException e) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
        return properties.getProperty(key);
    }
}
