package com.oliweb.db.dao;

import com.oliweb.db.dto.UtilisateurDTO;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import static com.oliweb.db.contract.UtilisateurContract.*;

public class UtilisateurDAO extends AbstractDAO<UtilisateurDTO> {

    public UtilisateurDAO(Connection connection) {
        super(connection);
    }

    public boolean checkLogin(String email, String pwd) {
        boolean isUserAvailable = false;

        String query = "SELECT * FROM " + TABLE_NAME
                + " WHERE " + COL_EMAIL_UTILISATEUR + " = ? "
                + " AND " + COL_MOT_PASSE_UTILISATEUR + "= ? "
                + " AND " + COL_STATUT_UTILISATEUR + "= ?";

        try {
            PreparedStatement stmt = dbConn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, pwd);
            stmt.setString(3, enumStatutUtilisateur.VALID.valeur());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                isUserAvailable = true;
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "checkLogin", e);
        }
        return isUserAvailable;
    }

    public boolean checkAdmin(String email, String pwd) {
        boolean isUserAvailable = false;

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_EMAIL_UTILISATEUR + " =  ?"
                + " AND " + COL_MOT_PASSE_UTILISATEUR + "= ?"
                + " AND " + COL_ADMIN_UTILISATEUR + "= ?"
                + " AND " + COL_STATUT_UTILISATEUR + "= ?";

        try {
            PreparedStatement stmt = dbConn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, pwd);
            stmt.setString(3, enumAdminUtilisateur.OUI.valeur());
            stmt.setString(4, enumStatutUtilisateur.VALID.valeur());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                isUserAvailable = true;
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "checkAdmin", e);
        }
        return isUserAvailable;
    }

    public boolean checkAdmin(Integer idUser) {
        boolean isAdmin = false;
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_ID_UTILISATEUR + " = ?"
                + " AND " + COL_ADMIN_UTILISATEUR + "= ?"
                + " AND " + COL_STATUT_UTILISATEUR + "= ?";
        try {
            PreparedStatement stmt = dbConn.prepareStatement(query);
            stmt.setInt(1, idUser);
            stmt.setString(2, "O");
            stmt.setString(3, enumStatutUtilisateur.VALID.valeur());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                isAdmin = true;
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "checkAdmin", e);
        }
        return isAdmin;
    }

    public void updateDateLastConnexion(Integer idUser) {
        try {
            Statement stmt = dbConn.createStatement();
            String dateAsString = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String update = "UPDATE " + TABLE_NAME + " SET " + COL_DATE_LAST_CONNEXION + " = " + dateAsString + " WHERE " + COL_ID_UTILISATEUR + " =" + String.valueOf(idUser);
            stmt.executeUpdate(update);
            stmt.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "updateDateLastConnexion", e);
        }
    }

    @Override
    public boolean save(UtilisateurDTO utilisateurDTO) {
        boolean insertStatus = false;
        String query = "INSERT INTO " + TABLE_NAME
                + "(" + COL_EMAIL_UTILISATEUR + ", "
                + COL_TELEPHONE_UTILISATEUR + ", "
                + COL_MOT_PASSE_UTILISATEUR + ")"
                + " VALUES (?,?,?)";

        try {
            PreparedStatement stmt = dbConn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, utilisateurDTO.getEmailUTI());
            stmt.setInt(2, utilisateurDTO.getTelephoneUTI());
            stmt.setString(3, utilisateurDTO.getPasswordUTI());

            if (stmt.executeUpdate() != 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                rs.next();
                insertStatus = true;
                utilisateurDTO.setIdUTI(rs.getInt(1));
                rs.close();
            } else {
                insertStatus = false;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "insert", e);
        }

        return insertStatus;
    }

    @Override
    public boolean update(UtilisateurDTO user) {
        int retour = 0;
        try {
            Statement stmt = dbConn.createStatement();
            String update = "UPDATE " + TABLE_NAME
                    + " SET " + COL_EMAIL_UTILISATEUR + " = '" + user.getEmailUTI()
                    + "' , " + COL_TELEPHONE_UTILISATEUR + " = " + user.getTelephoneUTI()
                    + " WHERE " + COL_ID_UTILISATEUR + " =" + String.valueOf(user.getIdUTI());
            retour = stmt.executeUpdate(update);
            stmt.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "update", e);
        }
        return retour != 0;
    }

    @Override
    public boolean delete(int idUser) {
        boolean deleteStatus = false;
        try {
            Statement stmt = dbConn.createStatement();
            int retour;

            // On supprime d'abord les annonces de l'utilisateur
            AnnonceDAO annonceDAO = new AnnonceDAO(dbConn);
            if (annonceDAO.deleteByIdUser(idUser)) {
                String delete = "DELETE FROM " + TABLE_NAME
                        + " WHERE " + COL_ID_UTILISATEUR + " =" + String.valueOf(idUser);
                retour = stmt.executeUpdate(delete);

                deleteStatus = retour != 0;
            }
            stmt.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "deleteById", e);
        }
        return deleteStatus;
    }

    public boolean deleteAll() {
        boolean deleteStatus = false;
        try {
            Statement stmt = dbConn.createStatement();
            int retour;
            String delete = "DELETE FROM " + TABLE_NAME;
            retour = stmt.executeUpdate(delete);

            deleteStatus = retour != 0;
            stmt.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "deleteAll", e);
        }
        return deleteStatus;
    }

    @Override
    public UtilisateurDTO get(int idUtilisateur) {
        UtilisateurDTO utilisateur = null;

        String query = "SELECT " + COL_ID_UTILISATEUR + ", "
                + COL_TELEPHONE_UTILISATEUR + ", "
                + COL_EMAIL_UTILISATEUR + ", "
                + COL_DATE_CREATION_UTILISATEUR + ", "
                + COL_MOT_PASSE_UTILISATEUR + ", "
                + COL_DATE_LAST_CONNEXION + ", "
                + COL_ADMIN_UTILISATEUR + ", "
                + COL_STATUT_UTILISATEUR
                + " FROM " + TABLE_NAME
                + " WHERE " + COL_ID_UTILISATEUR + " = ? ";

        try {
            PreparedStatement stmt = dbConn.prepareStatement(query);
            stmt.setInt(1, idUtilisateur);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                utilisateur = new UtilisateurDTO();
                utilisateur.setIdUTI(rs.getInt(COL_ID_UTILISATEUR));
                utilisateur.setEmailUTI(rs.getString(COL_EMAIL_UTILISATEUR));
                utilisateur.setTelephoneUTI(rs.getInt(COL_TELEPHONE_UTILISATEUR));
                utilisateur.setDateCreationUTI(rs.getString(COL_DATE_CREATION_UTILISATEUR));
                utilisateur.setPasswordUTI(rs.getString(COL_MOT_PASSE_UTILISATEUR));
                utilisateur.setDateLastConnexionUTI(rs.getString(COL_DATE_LAST_CONNEXION));
                utilisateur.setAdminUTI(rs.getString(COL_ADMIN_UTILISATEUR));
                utilisateur.setStatutUTI(rs.getString(COL_STATUT_UTILISATEUR));
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "getById", e);
        }
        return utilisateur;
    }

    public boolean existByEmail(String email) {
        boolean exist = false;

        String query = "SELECT COUNT(*) FROM " + TABLE_NAME
                + " WHERE " + COL_EMAIL_UTILISATEUR + " = ?"
                + " AND " + COL_STATUT_UTILISATEUR + "= ?";
        try {
            PreparedStatement stmt = dbConn.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, enumStatutUtilisateur.VALID.valeur());
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) >= 1) {
                exist = true;
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "existByEmail", e);
        }
        return exist;
    }

    public UtilisateurDTO getByEmail(String email) {
        UtilisateurDTO utilisateur = new UtilisateurDTO();
        Statement stmt;
        String query;
        ResultSet results;

        try {
            stmt = dbConn.createStatement();
            query = "SELECT " + COL_ID_UTILISATEUR + ", "
                    + COL_EMAIL_UTILISATEUR + ", "
                    + COL_TELEPHONE_UTILISATEUR + " "
                    + " FROM " + TABLE_NAME
                    + " WHERE " + COL_EMAIL_UTILISATEUR + "='" + email + "'"
                    + " AND " + COL_STATUT_UTILISATEUR + "='" + enumStatutUtilisateur.VALID.valeur() + "'";

            results = stmt.executeQuery(query);
            while (results.next()) {
                utilisateur.setIdUTI(results.getInt(COL_ID_UTILISATEUR));
                utilisateur.setEmailUTI(results.getString(COL_EMAIL_UTILISATEUR));
                utilisateur.setTelephoneUTI(results.getInt(COL_TELEPHONE_UTILISATEUR));
            }
            stmt.close();
            results.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "getByEmail", e);
        }
        return utilisateur;
    }

    public boolean changePassword(Integer idUser, String newPassword) {

        Statement stmt;
        String update;
        int retour;
        boolean updateStatus = false;

        try {
            stmt = dbConn.createStatement();
            update = "UPDATE " + TABLE_NAME + " SET " + COL_MOT_PASSE_UTILISATEUR + " = '" + newPassword + "' WHERE " + COL_ID_UTILISATEUR + " =" + String.valueOf(idUser);

            retour = stmt.executeUpdate(update);

            updateStatus = retour != 0;

            stmt.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "changePassword", e);
        }
        return updateStatus;
    }

    public boolean unregisterUser(Integer idUser) {

        Statement stmt;
        String update;
        int retour;
        boolean updateStatus = false;

        // On supprime d'abord les annonces de l'utilisateur
        AnnonceDAO annonceDAO = new AnnonceDAO(dbConn);
        if (annonceDAO.devalideByIdUser(idUser)) {
            try {
                stmt = dbConn.createStatement();
                update = "UPDATE " + TABLE_NAME + " SET " + COL_STATUT_UTILISATEUR + " = '" + enumStatutUtilisateur.UNREGISTRED.valeur() + "' WHERE " + COL_ID_UTILISATEUR + " =" + String.valueOf(idUser);
                retour = stmt.executeUpdate(update);
                updateStatus = retour != 0;
                stmt.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "unregisterUser", e);
            }
        }
        return updateStatus;
    }

    public Integer getNbUser() {
        Statement stmt;
        ResultSet results;
        String query;
        Integer nb_user = 0;

        try {
            stmt = dbConn.createStatement();
            query = "SELECT COUNT(*) FROM " + TABLE_NAME
                    + " WHERE " + COL_STATUT_UTILISATEUR + "='" + enumStatutUtilisateur.VALID.valeur() + "'";

            results = stmt.executeQuery(query);
            if (results.next()) {
                nb_user = results.getInt(1);
            }
            stmt.close();
            results.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "getNbUser", e);
        }
        return nb_user;
    }
}
