package com.oliweb.DB.DAO;

import com.oliweb.DB.DTO.MessageDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.oliweb.DB.Contract.MessageContract.*;

public class MessageDAO {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private Connection dbConn;

    public MessageDAO(Connection dbConn) {
        this.dbConn = dbConn;
    }

    private MessageDTO transfertMessage(ResultSet rs) throws SQLException {
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

    private int getMaxId(Integer idSender) {
        String query;
        int retour = -1;
        try {
            Statement stmt = dbConn.createStatement();
            query = "Select MAX(" + COL_ID_MESSAGE + ") as MAXID from " + TABLE_NAME + " WHERE " + COL_ID_SENDER + " = " + String.valueOf(idSender);
            ResultSet rs;
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                retour = rs.getInt("MAXID");
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "getMaxId", e);
        }
        return retour;
	}

    public boolean insert(MessageDTO message) {
        boolean insertStatus = false;
        int records;
        try {
            Statement stmt = dbConn.createStatement();
            String query = "INSERT INTO " + TABLE_NAME + " (" + COL_ID_SENDER + ", "
                    + COL_ID_RECEIVER + ", "
                    + COL_MESSAGE + ", "
                    + COL_DATE_MESSAGE + ")"
                    + " values(" +
                    message.getSender().getIdUTI() + "," +
                    message.getReceiver().getIdUTI() + ",'" +
                    message.getMessage() + "'," +
                    "CURRENT_TIME())";

            records = stmt.executeUpdate(query);

            // When record is successfully inserted
            if (records != 0) {
                insertStatus = true;
                message.setIdMessage(getMaxId(message.getSender().getIdUTI()));
            } else {
                insertStatus = false;
            }
            stmt.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "insert", e);
        }

		return insertStatus;
	}

    public boolean delete(Integer idMessage) {
        boolean deleteStatus = false;
        int records;
		String delete;
        try {
            Statement stmt = dbConn.createStatement();
            delete = "DELETE FROM " + TABLE_NAME + " WHERE " + COL_ID_MESSAGE + " = " + String.valueOf(idMessage);
            records = stmt.executeUpdate(delete);
            deleteStatus = records != 0;
            stmt.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "delete", e);
        }
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
            rs.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "listByIdSender", e);
        }
		return myList;
	}
}
