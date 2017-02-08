package com.oliweb.DB.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.oliweb.DB.DTO.UtilisateurDTO;

public class UtilisateurDAO {
	private Integer idutilisateur;
	private String nomUtilisateur;
	private String prenomUtilisateur;
	private Integer telephoneUtilisateur;
	private String emailUtilisateur;
	private Timestamp dateCreationUtilisateur;
	private String adminUtilisateur;

	public static final String TABLE_NAME = "utilisateur";
	public static final String COL_ID_UTILISATEUR = "idutilisateur";
	public static final String COL_TELEPHONE_UTILISATEUR = "telephoneUtilisateur";
	public static final String COL_EMAIL_UTILISATEUR = "emailUtilisateur";
	public static final String COL_DATE_CREATION_UTILISATEUR = "dateCreationUtilisateur";
	public static final String COL_PASSWORD_UTILISATEUR = "passwordUtilisateur";
	public static final String COL_DATE_LAST_CONNEXION = "dateLastConnexion";
	public static final String COL_ADMIN_UTILISATEUR = "adminUtilisateur";
	public static final String COL_STATUT_UTILISATEUR = "statutUtilisateur";
	public static final String COL_SALT_UTILISATEUR = "saltUtilisateur";

	public UtilisateurDAO(Integer idutilisateur, String nomUtilisateur, String prenomUtilisateur,
			Integer telephoneUtilisateur, String emailUtilisateur, Timestamp dateCreationUtilisateur, String adminUtilisateur) {
		super();
		this.idutilisateur = idutilisateur;
		this.nomUtilisateur = nomUtilisateur;
		this.prenomUtilisateur = prenomUtilisateur;
		this.telephoneUtilisateur = telephoneUtilisateur;
		this.emailUtilisateur = emailUtilisateur;
		this.dateCreationUtilisateur = dateCreationUtilisateur;
		this.adminUtilisateur = adminUtilisateur;
	}

	public UtilisateurDAO() {
		super();
	}

	public Integer getIdutilisateur() {
		return idutilisateur;
	}
	public void setIdutilisateur(Integer idutilisateur) {
		this.idutilisateur = idutilisateur;
	}
	public String getNomUtilisateur() {
		return nomUtilisateur;
	}
	public void setNomUtilisateur(String nomUtilisateur) {
		this.nomUtilisateur = nomUtilisateur;
	}
	public String getPrenomUtilisateur() {
		return prenomUtilisateur;
	}
	public void setPrenomUtilisateur(String prenomUtilisateur) {
		this.prenomUtilisateur = prenomUtilisateur;
	}
	public Integer getTelephoneUtilisateur() {
		return telephoneUtilisateur;
	}
	public void setTelephoneUtilisateur(Integer telephoneUtilisateur) {
		this.telephoneUtilisateur = telephoneUtilisateur;
	}
	public String getEmailUtilisateur() {
		return emailUtilisateur;
	}
	public void setEmailUtilisateur(String emailUtilisateur) {
		this.emailUtilisateur = emailUtilisateur;
	}
	public Timestamp getDateCreationUtilisateur() {
		return dateCreationUtilisateur;
	}
	public void setDateCreationUtilisateur(Timestamp dateCreationUtilisateur) {
		this.dateCreationUtilisateur = dateCreationUtilisateur;
	}
	public String getAdminUtilisateur() {
		return adminUtilisateur;
	}

	public void setAdminUtilisateur(String adminUtilisateur) {
		this.adminUtilisateur = adminUtilisateur;
	}
	/**
	 * Methode de v�rification de la concordance entre email et mot de passe
	 * 
	 * @param email
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	public static boolean checkLogin(String email, String pwd) throws Exception {
		boolean isUserAvailable = false;
		Connection dbConn = MyConnection.getInstance();

		Statement stmt = dbConn.createStatement();
		String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_EMAIL_UTILISATEUR + " = '" + email
				+ "' AND " + COL_PASSWORD_UTILISATEUR + "='" + pwd + "' AND " + COL_STATUT_UTILISATEUR + "='" + enumStatutUtilisateur.VALID.valeur()+"'";
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()) {
			isUserAvailable = true;
		}
		stmt.close();

		return isUserAvailable;
	}

	/**
	 * Methode de v�rification de la concordance entre email et mot de passe + V�rification que l'utilisateur est Admin
	 * @param email
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	public static boolean checkAdmin(String email, String pwd) throws Exception {
		boolean isUserAvailable = false;
		Connection dbConn = MyConnection.getInstance();

		Statement stmt = dbConn.createStatement();
		String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_EMAIL_UTILISATEUR + " = '" + email
				+ "' AND " + COL_PASSWORD_UTILISATEUR + "=" + "'" + pwd + "' AND " + COL_ADMIN_UTILISATEUR + "='O'"
				+ " AND " + COL_STATUT_UTILISATEUR + "='" + enumStatutUtilisateur.VALID.valeur()+"'";
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()) {
			isUserAvailable = true;
		}
		stmt.close();

		return isUserAvailable;
	}


	/**
	 * Methode de V�rification que l'utilisateur est Admin
	 * @param idUser
	 * @return
	 * @throws Exception
	 */
	public static boolean checkAdmin(Integer idUser) throws Exception {
		boolean isAdmin = false;
		Connection dbConn = MyConnection.getInstance();

		Statement stmt = dbConn.createStatement();
		String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_ID_UTILISATEUR + " = " + String.valueOf(idUser)
		+ " AND " + COL_ADMIN_UTILISATEUR + "='O'"
		+ " AND " + COL_STATUT_UTILISATEUR + "='" + enumStatutUtilisateur.VALID.valeur()+"'";;
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()) {
			isAdmin = true;
		}
		stmt.close();

		return isAdmin;
	}


	/**
	 * M�thode retournant un utilisateur � partir de son Id
	 * 
	 * @param idUtilisateur
	 * @return
	 * @throws SQLException, Exception 
	 */
	public static UtilisateurDTO getById(Integer idUtilisateur) throws Exception{
		Connection dbConn = MyConnection.getInstance();
		UtilisateurDTO utilisateur = new UtilisateurDTO();

		Statement stmt = (Statement) dbConn.createStatement();
		String query = "SELECT " + COL_ID_UTILISATEUR + ", "
				+ COL_TELEPHONE_UTILISATEUR + ", "
				+ COL_EMAIL_UTILISATEUR
				+ " FROM " + TABLE_NAME
				+ " WHERE " + COL_ID_UTILISATEUR + " = " + String.valueOf(idUtilisateur)
				+ " AND " + COL_STATUT_UTILISATEUR + "='" + enumStatutUtilisateur.VALID.valeur()+"'";;

		ResultSet rs = stmt.executeQuery(query);
		if (rs.next()){
			utilisateur.setIdUTI(rs.getInt(COL_ID_UTILISATEUR));
			utilisateur.setEmailUTI(rs.getString(COL_EMAIL_UTILISATEUR));
			utilisateur.setTelephoneUTI(rs.getInt(COL_TELEPHONE_UTILISATEUR));
		}
		stmt.close();
		return utilisateur;

	}

	/**
	 * Mise � jour de la date de derni�re connexion
	 * 
	 * @param idUser
	 * @throws Exception
	 */
	public static void updateDateLastConnexion(Integer idUser) throws Exception {
		Connection dbConn = MyConnection.getInstance();
		Statement stmt = dbConn.createStatement();

		String dateAsString = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String update = "UPDATE " + TABLE_NAME + " SET " + COL_DATE_LAST_CONNEXION + " = "+ dateAsString+" WHERE " + COL_ID_UTILISATEUR + " =" + String.valueOf(idUser);
		int records = stmt.executeUpdate(update);
		stmt.close();
		
		//When record is successfully inserted
		if (records != 0) {
			TransactionDAO.insert(dbConn, update);
		}
	}


	/**
	 * 
	 * @param user
	 * @throws Exception
	 */
	public static boolean update(UtilisateurDTO user) throws Exception {
		Connection dbConn = MyConnection.getInstance();
		Statement stmt = dbConn.createStatement();

		String update = "UPDATE " + TABLE_NAME 
				+ " SET " + COL_EMAIL_UTILISATEUR + " = '"+ user.getEmailUTI() 
				+ "' , " + COL_TELEPHONE_UTILISATEUR + " = " + user.getTelephoneUTI() 
				+ " WHERE " + COL_ID_UTILISATEUR + " =" + String.valueOf(user.getIdUTI());
		System.out.println(update);
		int retour = stmt.executeUpdate(update);
		stmt.close();

		// When record is successfully inserted
		if (retour != 0) {
			TransactionDAO.insert(dbConn, update);
		}

		return retour!=0; 
	}

	/**
	 * V�rification de l'existence d'un utilisateur
	 * 
	 * @param id
	 * @throws Exception
	 */
	public static boolean existById(Integer id) throws Exception {
		Connection dbConn = MyConnection.getInstance();
		boolean exist = false;

		Statement stmt = dbConn.createStatement();
		String query = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + COL_ID_UTILISATEUR + " =" + String.valueOf(id)
		+ " AND " + COL_STATUT_UTILISATEUR + "='" + enumStatutUtilisateur.VALID.valeur()+"'";;
		ResultSet rs = stmt.executeQuery(query);
		if (rs.next()){
			if (rs.getInt(1) >= 1){
				exist = true;
			}
		}
		stmt.close();
		return exist;
	}

	/**
	 * V�rification de l'existence d'un utilisateur par son email
	 * 
	 * @param id
	 * @throws Exception
	 */
	public static boolean existByEmail(String email) throws Exception {
		Connection dbConn = MyConnection.getInstance();
		boolean exist = false;

		Statement stmt = dbConn.createStatement();
		String query = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + COL_EMAIL_UTILISATEUR + " = '" + email + "' "
				+ " AND " + COL_STATUT_UTILISATEUR + "='" + enumStatutUtilisateur.VALID.valeur()+"'";;
		ResultSet rs = stmt.executeQuery(query);
		if (rs.next()){
			if (rs.getInt(1) >= 1){
				exist = true;
			}
		}
		stmt.close();
		return exist;
	}

	/**
	 * Methode de r�cup�ration d'un utilisateur � partir de l'�galit� sur son adresse mail
	 * @param email
	 * @return Utilisateur
	 * @throws SQLException 
	 */
	// @SuppressWarnings("unused")
	public static UtilisateurDTO getByEmail(String email) throws Exception{
		Connection dbConn = MyConnection.getInstance();
		UtilisateurDTO utilisateur = new UtilisateurDTO();
		Statement stmt;
		String query;
		ResultSet results; 

		stmt = (Statement) dbConn.createStatement();
		query = "SELECT " + COL_ID_UTILISATEUR + ", "
				+ COL_EMAIL_UTILISATEUR + ", "
				+ COL_TELEPHONE_UTILISATEUR + " "
				+ " FROM " + TABLE_NAME
				+ " WHERE " + COL_EMAIL_UTILISATEUR + "='"+email+"'"
				+ " AND " + COL_STATUT_UTILISATEUR + "='" + enumStatutUtilisateur.VALID.valeur()+"'";

		results = stmt.executeQuery(query);
		while (results.next()){
			utilisateur.setIdUTI(results.getInt(COL_ID_UTILISATEUR));
			utilisateur.setEmailUTI(results.getString(COL_EMAIL_UTILISATEUR));
			utilisateur.setTelephoneUTI(results.getInt(COL_TELEPHONE_UTILISATEUR));
		}
		stmt.close();
		return utilisateur;
	}

	/**
	 * Method to insert email and pwd in DB *
	 * @param name
	 * @param email
	 * @param pwd
	 * @param prenom
	 * @param telephone
	 * @return -1 = Fail, -2 = Email d�j� pr�sent, 1 = Success
	 * @throws Exception
	 */
	public static int insert(String email, String pwd, Integer telephone) throws Exception {
		int insertStatus = -1;
		if (!existByEmail(email)){

			Connection dbConn = MyConnection.getInstance();

			Statement stmt = (Statement) dbConn.createStatement();
			String query = "INSERT INTO " + TABLE_NAME
					+ "(" + COL_EMAIL_UTILISATEUR + ", "
					+ COL_TELEPHONE_UTILISATEUR + ", "
					+ COL_PASSWORD_UTILISATEUR + ")"
					+ " VALUES ('"
					+ email + "', "
					+ String.valueOf(telephone) + ", '"
					+ pwd + "')";
			System.out.println(query);
			int records = stmt.executeUpdate(query);
			stmt.close();

			//When record is successfully inserted
			if (records != 0) {
				insertStatus = 1;
				TransactionDAO.insert(dbConn, query);
			}
		}else{
			// Il existe d�j� un enregistrement avec cette adresse mail
			insertStatus = -2;
		}
		return insertStatus;
	}

	/**
	 * @param email
	 * @return
	 * @throws Exception
	 */
	public static String getPasswordByEmail(String email) throws Exception{
		Connection dbConn = MyConnection.getInstance();
		Statement stmt;
		String query;
		ResultSet results; 
		String password = null;

		stmt = (Statement) dbConn.createStatement();
		query = "SELECT " + COL_PASSWORD_UTILISATEUR
				+ " FROM " + TABLE_NAME
				+ " WHERE " + COL_EMAIL_UTILISATEUR + "='"+email+"'";

		results = stmt.executeQuery(query);
		if (results.next()){
			password = results.getString(COL_PASSWORD_UTILISATEUR);
		}
		stmt.close();

		return password;
	}
	
	/**
	 * 
	 * @param idUser
	 * @return
	 * @throws Exception
	 */
	public static String getPasswordByIdUser(Integer idUser) throws Exception{
		Connection dbConn = MyConnection.getInstance();
		Statement stmt;
		String query;
		ResultSet results; 
		String password = null;

		stmt = (Statement) dbConn.createStatement();
		query = "SELECT " + COL_PASSWORD_UTILISATEUR
				+ " FROM " + TABLE_NAME
				+ " WHERE " + COL_ID_UTILISATEUR + "="+idUser;

		results = stmt.executeQuery(query);
		if (results.next()){
			password = results.getString(COL_PASSWORD_UTILISATEUR);
		}
		stmt.close();

		return password;
	}


	/**
	 * @param email
	 * @return
	 * @throws Exception 
	 */
	public static boolean changePassword(Integer idUser, String newPassword) throws Exception{
		Connection dbConn = MyConnection.getInstance();
		Statement stmt;
		String update;
		int retour = 0;
		boolean updateStatus = false;

		stmt = (Statement) dbConn.createStatement();
		update = "UPDATE " + TABLE_NAME + " SET " + COL_PASSWORD_UTILISATEUR + " = '"+ newPassword+"' WHERE " + COL_ID_UTILISATEUR + " =" + String.valueOf(idUser);

		retour = stmt.executeUpdate(update);
		
		if (retour != 0) {
			updateStatus = true;
			TransactionDAO.insert(dbConn, update); // On ins�re la transaction
		}else{
			updateStatus = false;
		}
		
		stmt.close();
		return updateStatus;
	}

	/**
	 * 
	 * @return
	 * @throws Exception 
	 */
	public static boolean unregisterUser(Integer idUser) throws Exception{
		Connection dbConn = MyConnection.getInstance();
		Statement stmt;
		String update;
		int retour = 0;
		boolean updateStatus = false;

		// On supprime d'abord les annonces de l'utilisateur
		if (AnnonceDAO.devalideByIdUser(idUser)){
			stmt = (Statement) dbConn.createStatement();
			update = "UPDATE " + TABLE_NAME + " SET " + COL_STATUT_UTILISATEUR + " = '"+enumStatutUtilisateur.UNREGISTRED.valeur() +"' WHERE " + COL_ID_UTILISATEUR + " =" + String.valueOf(idUser);
			retour = stmt.executeUpdate(update);
			System.out.println("Retour = " + String.valueOf(retour) + update);
			if (retour != 0) {
				updateStatus = true;
				TransactionDAO.insert(dbConn, update); // On ins�re la transaction
			}else{
				updateStatus = false;
			}
			
			stmt.close();
		}
		return updateStatus; 
	}


	/**
	 * 
	 * @return
	 */
	public static Integer getNbUser() throws SQLException{
		Connection dbConn = MyConnection.getInstance();
		Statement stmt;
		ResultSet results; 
		String query;
		Integer nb_user = 0;

		stmt = (Statement) dbConn.createStatement();
		query = "SELECT COUNT(*) FROM " + TABLE_NAME
				+ " WHERE " + COL_STATUT_UTILISATEUR + "='" + enumStatutUtilisateur.VALID.valeur()+"'";;

		results = stmt.executeQuery(query);
		if (results.next()){
			nb_user = results.getInt(1);
		}
		stmt.close();

		return nb_user;
	}

	/**
	 * 
	 * @param idUser
	 * @return
	 * @throws Exception
	 */
	public static boolean deleteById(Integer idUser) throws Exception {
		Connection dbConn = MyConnection.getInstance();
		Statement stmt = dbConn.createStatement();
		int retour = 0;
		boolean deleteStatus = false;

		// On supprime d'abord les annonces de l'utilisateur
		if (AnnonceDAO.deleteByIdUser(idUser)){
			String delete = "DELETE FROM " + TABLE_NAME 
					+ " WHERE " + COL_ID_UTILISATEUR + " =" + String.valueOf(idUser);
			retour = stmt.executeUpdate(delete);
			
			if (retour != 0) {
				deleteStatus = true;
				TransactionDAO.insert(dbConn, delete); // On ins�re la transaction
			}else{
				deleteStatus = false;
			}
			
			stmt.close();
		}

		return deleteStatus; 
	}	
}
