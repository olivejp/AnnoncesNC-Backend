package com.oliweb.db.dao;

import com.oliweb.db.dto.MessageDTO;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static com.oliweb.db.contract.MessageContract.*;

public class MessageDAO extends AbstractDAO<MessageDTO> {

    public MessageDAO(Connection dbConn) {
        super(dbConn);
    }

    private MessageDTO transfertMessage(ResultSet rs) throws SQLException {
        // Création d'une nouvelle annonce
        MessageDTO message = new MessageDTO();

        // Renseignement des champs de l'annonce
        message.setIdMessage(rs.getInt(COL_ID_MESSAGE));
        UtilisateurDAO utilisateurDAO = new UtilisateurDAO(dbConn);
        message.setIdReceiver(utilisateurDAO.get(rs.getInt(COL_ID_RECEIVER)));
        message.setIdSender(utilisateurDAO.get(rs.getInt(COL_ID_SENDER)));

        // On formate la date Timestamp en String
        String dateAsText = new SimpleDateFormat("yyyyMMddHHmm").format(rs.getTimestamp(COL_DATE_MESSAGE));

        message.setDateMessage(Long.valueOf(dateAsText));
        message.setMessage(rs.getString(COL_MESSAGE));
        return message;
    }

    public List<MessageDTO> listByIdSender(Integer idSender) {
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

    @Override
    public boolean save(MessageDTO item) {
        boolean insertStatus = false;

        String query = "INSERT INTO " + TABLE_NAME + " (" + COL_ID_SENDER + ", "
                + COL_ID_RECEIVER + ", "
                + COL_MESSAGE + ", "
                + COL_DATE_MESSAGE + ")"
                + " VALUES( ?, ?, ?, ?)";

        try {
            PreparedStatement stmt = dbConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, item.getSender().getIdUTI());
            stmt.setInt(2, item.getReceiver().getIdUTI());
            stmt.setString(3, item.getMessage());
            stmt.setString(4, "CURRENT_TIME()");

            if (stmt.executeUpdate() != 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                rs.next();
                item.setIdMessage(rs.getInt(1));  // On met à jour l'ID de l'annonce qu'on vient de créer.
                insertStatus = true;
                rs.close();
            }
            stmt.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "insert", e);
        }
        return insertStatus;
    }

    @Override
    public boolean update(MessageDTO item) {
        return false;
    }

    @Override
    public MessageDTO get(int id) {
        return null;
    }

    @Override
    public boolean delete(int itemId) {
        return false;
    }
}
