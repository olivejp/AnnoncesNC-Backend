package com.oliweb.DB.DAO;

import com.oliweb.DB.DTO.MessageDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.oliweb.DB.Contract.MessageContract.*;

public class MessageDAO {
	private Integer idMessage;
	private String idSender;
	private String idReceiver;
	private String message;
	private Timestamp dateMessage;

    private Connection dbConn;

    public MessageDAO(Connection dbConn) {
        this.dbConn = dbConn;
    }

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

    private MessageDTO transfertMessage(ResultSet rs) throws Exception {
        // Création d'une nouvelle annonce
        MessageDTO message = new MessageDTO();

		// Renseignement des champs de l'annonce
		message.setIdMessage(rs.getInt(COL_ID_MESSAGE));
        UtilisateurDAO utilisateurDAO = new UtilisateurDAO(dbConn);
        message.setIdReceiver(utilisateurDAO.getById(rs.getInt(COL_ID_RECEIVER)));
        message.setIdSender(utilisateurDAO.getById(rs.getInt(COL_ID_SENDER)));

		// On formate la date Timestamp en String 
		String dateAsText = new SimpleDateFormat("yyyyMMddHHmm").format(rs.getTimestamp(COL_DATE_MESSAGE));

		message.setDateMessage(Long.valueOf(dateAsText));
		message.setMessage(rs.getString(COL_MESSAGE));
		return message;
	}

    private int getMaxId(Integer idSender) throws Exception {
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

    public boolean insert(MessageDTO message) throws Exception {
        boolean insertStatus;
        int records;
		String query;
		Statement stmt = dbConn.createStatement();
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
			message.setIdMessage(getMaxId(message.getSender().getIdUTI()));
		}else{
			insertStatus = false;
		}
		stmt.close();
		return insertStatus;
	}

    public boolean delete(Integer idMessage) throws Exception {
        boolean deleteStatus;
        int records;
		String delete;
		Statement stmt = dbConn.createStatement();
		
		delete = "DELETE FROM " + TABLE_NAME + " WHERE " + COL_ID_MESSAGE + " = " + String.valueOf(idMessage);

		records = stmt.executeUpdate(delete);
        deleteStatus = records != 0;
        stmt.close();
        return deleteStatus;
	}

    public ArrayList<MessageDTO> listByIdSender(Integer idSender) {
        ArrayList<MessageDTO> myList = new ArrayList<>();

		try {
			Statement stmt = dbConn.createStatement();
			String query = "SELECT " + COL_ID_MESSAGE + ", "
					+ COL_ID_SENDER + ", "
					+ COL_ID_RECEIVER + ", "
					+ COL_ID_MESSAGE + ", "
					+ COL_DATE_MESSAGE
					+ " FROM " + TABLE_NAME
					+ " WHERE " + COL_ID_SENDER + " = " + String.valueOf(idSender)
					+ " ORDER BY " + COL_DATE_MESSAGE + " DESC";

			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				MessageDTO messageDTO = transfertMessage(rs);
				myList.add(messageDTO);
			}
			stmt.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		return myList;
	}
}
