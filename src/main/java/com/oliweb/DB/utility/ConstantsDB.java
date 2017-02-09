package com.oliweb.DB.utility;

//Change these parameters according to your DB
public class ConstantsDB {

    public static final String dbUser = "root";
    public static final String dbPwd = "Toyota84";
    public static final String dbClass = "com.mysql.jdbc.Driver";
    public static final String directory_image = "C:\\wampProd\\www\\OliwebAnnonces\\AnnoncesNC\\uploads\\";
    public static final String upload_directory = "http://annoncesnc.ddns.net/AnnoncesNC/uploads/";
    public static final Integer PAGINATION = 10;
    public static final Integer MAX_ANNONCE = 5;

    // Serveur Google Cloud SQL
//    public static final String dbName = "annoncesrest";
//    public static final String dbUser = "root";
//    public static final String dbPwd = "Toyota84";
//    public static final String dbIp = "130.211.241.131";
//    public static final String dbInstance = "annonces-nc";
//    public static final String dbUrl = String.format("jdbc:mysql://%s:3306/%s",dbIp,dbInstance);
//    public static final String dbClass = "com.mysql.jdbc.Driver";

//		// Configuration Serveur tizoo
//		private static final String dbName= "oliweb_annoncesrest";
//		public static final String dbUser = "oliweb_annoncesnc";
//		public static final String dbPwd = "je@n-p@ul84";
//		public static final String directory_image = "C:\\wampProd\\www\\OliwebAnnonces\\AnnoncesNC\\uploads\\";
//		public static final String upload_directory = "http://oliweb.nc/Annonces/uploads/";

    // Configuration COMMUNE
    public static final int id_all_categories = 999;
    // Configuration serveur local
    private static final String dbName = "annoncesrest";
    public static final String dbUrl = "jdbc:mysql://localhost:3306/" + dbName + "?autoReconnect=true";


}
