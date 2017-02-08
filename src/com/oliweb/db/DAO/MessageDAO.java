package com.oliweb.DB.DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.oliweb.DB.DTO.MessageDTO;

public class MessageDAO {
	private Integer idMessage;
	private String idSender;
	private String idReceiver;
	private String message;
	private Timestamp dateMessage;

	public static final String TABLE_NAME = "message";
	public static final String COL_ID_MESSAGE = "idMessage";
	public static final String COL_ID_SENDER = "idSender";
	public static final String COL_ID_RECEIVER = "idReceiver";
	public static final String COL_MESSAGE = "message";
	public static final String COL_DATE_MESSAGE = "dateMessage";
	
	public Integer getIdMessage() {
		return idMessage;
	}
	public void setIdMessage(Integer idMessage) {
		this.idMessage = idMessage;
	}
	public String getIdSender() {
		return idSender;
	}
	public void setIdSender(String idSender) {
		this.idSender = idSender;
	}
	public String getIdReceiver() {
		return idReceiver;
	}
	public void setIdReceiver(String idReceiver) {
		this.idReceiver = idReceiver;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Timestamp getDateMessage() {
		return dateMessage;
	}
	public void setDateMessage(Timestamp dateMessage) {
		this.dateMessage = dateMessage;
	}
	
	
	/**
	 * 
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	public static MessageDTO transfertMessage(ResultSet rs) throws Exception{
		// Cr�ation d'une nouvelle annonce
		MessageDTO message = new MessageDTO();

		// Renseignement des champs de l'annonce
		message.setIdMessage(rs.getInt(COL_ID_MESSAGE));
		message.setIdReceiver(UtilisateurDAO.getById(rs.getInt(COL_ID_RECEIVER)));
		message.setIdSender(UtilisateurDAO.getById(rs.getInt(COL_ID_SENDER)));

		// On formate la date Timestamp en String 
		String dateAsText = new SimpleDateFormat("yyyyMMddHHmm").format(rs.getTimestamp(COL_DATE_MESSAGE));

		message.setDateMessage(Long.valueOf(dateAsText));
		message.setMessage(rs.getString(COL_MESSAGE));
		return message;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public static int getMaxId(Integer idSender) throws Exception{
		Connection dbConn = MyConnection.getInstance();
		String query;
		int retour = -1;

		Statement stmt = dbConn.createStatement();

		query = "Select MAX(" + COL_ID_MESSAGE + ") as MAXID from " + TABLE_NAME +" WHERE " + COL_ID_SENDER + " = " + String.valueOf(idSender);
		ResultSet rs;
		rs = stmt.executeQuery(query);
		if (rs.next()){
			retour = rs.getInt("MAXID");
		}
		stmt.close();
		return retour;
	}
	
	/**
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public static boolean insert(MessageDTO message) throws Exception {
		Connection dbConn = MyConnection.getInstance();
		boolean insertStatus = false;
		int records;
		String query;
		Statement stmt = (Statement) dbConn.createStatement();
		query = "INSERT INTO " + TABLE_NAME + " (" + COL_ID_SENDER + ", "
				+ COL_ID_RECEIVER + ", "
				+ COL_MESSAGE + ", "
				+ COL_DATE_MESSAGE +")"
				+ " values("+
				message.getSender().getIdUTI() + ","+
				message.getReceiver().getIdUTI() + ",'"+
				message.getMessage()+ "',"+
				"CURRENT_TIME())";
		
		records = stmt.executeUpdate(query);

		// When record is successfully inserted
		if (records != 0) {
			insertStatus = true;
			// On met � jour l'ID du message qu'on vient de cr�er.
			message.setIdMessage(getMaxId(message.getSender().getIdUTI()));
			TransactionDAO.insert(dbConn, query); // On ins�re la transaction
		}else{
			insertStatus = false;
		}
		stmt.close();
		return insertStatus;
	}
	
	/**
	 * 
	 * @param idMessage
	 * @return
	 * @throws Exception
	 */
	public static boolean delete(Integer idMessage) throws Exception {
		Connection dbConn = MyConnection.getInstance();
		boolean deleteStatus = false;
		int records;
		String delete = null;
		Statement stmt = (Statement) dbConn.createStatement();
		
		delete = "DELETE FROM " + TABLE_NAME + " WHERE " + COL_ID_MESSAGE + " = " + String.valueOf(idMessage);

		records = stmt.executeUpdate(delete);
		//When record is successfully inserted
		if (records != 0) {
			deleteStatus = true;
			TransactionDAO.insert(dbConn, delete); // On ins�re la transaction
		}else{
			deleteStatus = false;
		}
		stmt.close();
		return deleteStatus;
	}

	
	/**
	 * 
	 * @param idSender
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<MessageDTO> listByIdSender(Integer idSender) throws Exception{
		Connection dbConn = MyConnection.getInstance();
		ArrayList<MessageDTO> myList = new ArrayList<MessageDTO>();

		// On va r�cup�rer tous les messages pour l'utilisateur demand�
		Statement stmt = (Statement) dbConn.createStatement();
		String query = "SELECT " + COL_ID_MESSAGE + ", " + COL_ID_SENDER + ", " + COL_ID_RECEIVER+ ", " + COL_ID_MESSAGE + ", " + COL_DATE_MESSAGE
				+ " FROM " + TABLE_NAME
				+ " WHERE " + COL_ID_SENDER + " = " + String.valueOf(idSender)
				+ " ORDER BY " + COL_DATE_MESSAGE + " DESC";
		
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()){
			// Cr�ation d'un nouveau message
			MessageDTO messageDTO = transfertMessage(rs); 
					
			// Ajout du message dans la liste
			myList.add(messageDTO);
		}

		stmt.close();

		// On retourne la liste de message que l'on vient de cr�er.
		return myList;
	}
	
}
