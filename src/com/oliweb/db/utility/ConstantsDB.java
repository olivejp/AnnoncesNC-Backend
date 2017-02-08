package com.oliweb.DB.utility;

//Change these parameters according to your DB
public class ConstantsDB {

	// Configuration serveur local
	private static final String dbName= "annoncesrest";
	public static final String dbUser = "root";
	public static final String dbPwd = "Toyota84";
	public static final String directory_image = "C:\\wampProd\\www\\OliwebAnnonces\\AnnoncesNC\\uploads\\";
	public static final String upload_directory = "http://annonces.noip.me/AnnoncesNC/uploads/";

//		// Configuration Serveur tizoo
//		private static final String dbName= "oliweb_annoncesrest";
//		public static final String dbUser = "oliweb_annoncesnc";
//		public static final String dbPwd = "je@n-p@ul84";
//		public static final String directory_image = "C:\\wampProd\\www\\OliwebAnnonces\\AnnoncesNC\\uploads\\";
//		public static final String upload_directory = "http://oliweb.nc/Annonces/uploads/";

	// Configuration COMMUNE
	public static final String dbUrl = "jdbc:mysql://localhost:3306/"+dbName+"?autoReconnect=true";
	public static final String dbClass = "com.mysql.jdbc.Driver";
	public static final Integer PAGINATION = 10;
	public static final Integer MAX_ANNONCE = 5;
	public static final int id_all_categories = 999;





}
