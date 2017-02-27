package com.oliweb.restservice;


import com.google.gson.Gson;
import com.oliweb.db.dao.AnnonceDAO;
import com.oliweb.db.dao.MessageDAO;
import com.oliweb.db.dao.MyConnection;
import com.oliweb.db.dao.UtilisateurDAO;
import com.oliweb.db.dto.AnnonceDTO;
import com.oliweb.db.dto.UtilisateurDTO;
import com.oliweb.mail.PropertiesMail;
import com.oliweb.mail.SendMail;
import com.oliweb.utility.Crypto;
import com.oliweb.utility.Proprietes;
import com.oliweb.utility.Utility;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static com.oliweb.utility.Proprietes.*;

@Path("/utilisateurs")
public class UtilisateurRestService {

    private static final String ERROR_MAIL_ALREADY_EXIST = "ERROR_MAIL_ALREADY_EXIST";
    private static Gson gson = new Gson();
    private AnnonceDAO annonceDAO = new AnnonceDAO(MyConnection.getInstance());
    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO(MyConnection.getInstance());
    private MessageDAO messageDAO = new MessageDAO(MyConnection.getInstance());

    @GET
    @Path("{idUser}/annonces")
    @Produces(MediaType.APPLICATION_JSON)
    public String listAnnonceByUser(@PathParam("idUser") Integer idUser,
                                    @QueryParam("page") Integer page) {
        ReturnWS rs = new ReturnWS("listAnnonceByUser", false, null, null);
        List<AnnonceDTO> myList = annonceDAO.getByIdUser(idUser, page);
        if (myList != null) {
            rs.setStatus(true);
            if (!myList.isEmpty()) {
                rs.setMsg(gson.toJson(myList));
            }
        }
        return gson.toJson(rs);
    }

    @PUT
    @Path("{idUser}")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateUser(@PathParam("idUser") Integer idUser,
                             @QueryParam("emailUser") String emailUser,
                             @QueryParam("telephoneUser") Integer telephoneUser) {
        ReturnWS rs = new ReturnWS("updateUser", false, null, null);
        if (utilisateurDAO.get(idUser) != null) {
            UtilisateurDTO user = utilisateurDAO.get(idUser);
            user.setEmailUTI(emailUser);
            user.setTelephoneUTI(telephoneUser);

            if (utilisateurDAO.update(user)) {
                rs.setStatus(true);
            } else {
                rs.setMsg("L'utilisateur n'a pas pu être mis à jour dans la BD");
            }
        }
        return gson.toJson(rs);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String doregister(@QueryParam("email") String email,
                             @QueryParam("password") String encryptedPwd,
                             @QueryParam("telephone") Integer telephone) {
        ReturnWS rs = new ReturnWS("doregister", false, null, null);

        // On décrypte le mot de passe s'il est <> de null
        String decryptedPwd = (encryptedPwd != null) ? Crypto.desDecryptIt(encryptedPwd) : null;

        if (Utility.isNotNull(email) && Utility.isNotNull(decryptedPwd)) {
            UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
            utilisateurDTO.setEmailUTI(email);
            utilisateurDTO.setPasswordUTI(decryptedPwd);
            utilisateurDTO.setTelephoneUTI(telephone);

            // Check that the mail doesnt already exists
            if (utilisateurDAO.existByEmail(utilisateurDTO.getEmailUTI())) {
                rs.setMsg(ERROR_MAIL_ALREADY_EXIST);
                return gson.toJson(rs);
            }

            // try to save the user
            if (utilisateurDAO.save(utilisateurDTO)) {
                rs.setStatus(true);
                rs.setId(utilisateurDTO.getIdUTI());
            }
        }
        return gson.toJson(rs);
    }

    @DELETE
    @Path("{idUser}")
    @Produces(MediaType.APPLICATION_JSON)
    public String unregister(@PathParam("idUser") Integer idUser) {
        ReturnWS rs = new ReturnWS("unregister", false, null, null);
        if (utilisateurDAO.get(idUser) != null) {
            if (utilisateurDAO.unregisterUser(idUser)) {
                rs.setStatus(true);
            } else {
                rs.setMsg("L'utilisateur n'a pas pu être supprimé");
            }
        }
        return gson.toJson(rs);
    }


    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    public String getnbuser() {
        ReturnWS rs = new ReturnWS("getnbuser", false, null, null);
        rs.setStatus(true);
        rs.setMsg(String.valueOf(utilisateurDAO.getNbUser()));
        return gson.toJson(rs);
    }

    @POST
    @Path("{idUser}/change-password")
    @Produces(MediaType.APPLICATION_JSON)
    public String changepassword(@PathParam("idUser") Integer idUser,
                                 @QueryParam("oldPassword") String oldEncryptedPwd,
                                 @QueryParam("newPassword") String newEncryptedPwd) {
        ReturnWS rs = new ReturnWS("changepassword", false, null, null);

        String oldDecryptedPwd = null;
        String newDecryptedPwd = null;

        // On décrypte l'ancien et nouveau mot de passe
        if (oldEncryptedPwd != null) {
            oldDecryptedPwd = Crypto.desDecryptIt(oldEncryptedPwd);
        }
        if (newEncryptedPwd != null) {
            newDecryptedPwd = Crypto.desDecryptIt(newEncryptedPwd);
        }

        if (utilisateurDAO.get(idUser) != null) {
            // Vérification que l'ancien password est le même que celui qu'on a renvoyé
            String password = utilisateurDAO.get(idUser).getPasswordUTI();
            if (password.equals(oldDecryptedPwd)) {
                if (utilisateurDAO.changePassword(idUser, newDecryptedPwd)) {
                    rs.setStatus(true);
                } else {
                    rs.setMsg("Le mot de passe n'a pas pu être changé.");
                }
            } else {
                rs.setMsg("Erreur sur l'ancien mot de passe.");
            }
        } else {
            rs.setMsg("L'utilisateur est inconnu.");
        }
        return gson.toJson(rs);
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public String doLogin(@QueryParam("email") String email,
                          @QueryParam("password") String encryptedPwd) {
        boolean result;
        ReturnWS rs = new ReturnWS("doLogin", false, null, null);

        // On décrypte le mot de passe
        String decryptPwd = (encryptedPwd != null) ? Crypto.desDecryptIt(encryptedPwd) : null;

        result = Utility.isNotNull(email) && Utility.isNotNull(decryptPwd) && utilisateurDAO.checkLogin(email, decryptPwd);

        if (result) {
            UtilisateurDTO utilisateur = utilisateurDAO.getByEmail(email);
            rs.setStatus(true);
            rs.setMsg(gson.toJson(utilisateur));

            // Mise à jour de la date de dernière connexion de l'utilisateur
            utilisateurDAO.updateDateLastConnexion(utilisateur.getIdUTI());
        } else {
            rs.setMsg("Email ou Mot de passe incorrect");
        }
        return gson.toJson(rs);
    }

    @POST
    @Path("/lostpassword")
    @Produces(MediaType.APPLICATION_JSON)
    public String lostpassword(@QueryParam("email") String email) {
        ReturnWS rs = new ReturnWS("lostpassword", false, null, null);

        if (utilisateurDAO.existByEmail(email)) {
            UtilisateurDTO user = utilisateurDAO.getByEmail(email);

            rs.setStatus(true);
            String newPassword = Utility.generateRandomInt();
            utilisateurDAO.changePassword(user.getIdUTI(), newPassword);

            String emailFrom = Proprietes.getProperty(EMAIL_FROM);
            String smtpHost = Proprietes.getProperty(SMTP_HOST);
            String smtp_port = Proprietes.getProperty(SMTP_PORT);
            String emailUsername = Proprietes.getProperty(EMAIL_USERNAME);
            String emailPassword = Proprietes.getProperty(EMAIL_PASS);

            PropertiesMail propertiesMail = new PropertiesMail(emailFrom, smtpHost, smtp_port, emailUsername, emailPassword);

            SendMail.executeTLS(propertiesMail, email, "Application " + Proprietes.getProperty(APPLI_NAME) + " - Mot de passe perdu", "Voici votre mot de passe : " + newPassword);

        } else {
            rs.setMsg("Email inconnu dans la base de données");
        }
        return gson.toJson(rs);
    }

    @GET
    @Path("{idUser}/messages")
    @Produces(MediaType.APPLICATION_JSON)
    public String listMessages(@PathParam("idUser") Integer idUser) {
        ReturnWS rs = new ReturnWS("listMessages", false, null, null);
        rs.setMsg(gson.toJson(messageDAO.listByIdSender(idUser)));
        return gson.toJson(rs);
    }
}
