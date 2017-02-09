package com.oliweb.restservice;


import com.google.gson.Gson;
import com.oliweb.DB.DAO.AnnonceDAO;
import com.oliweb.DB.DAO.MessageDAO;
import com.oliweb.DB.DAO.MyConnection;
import com.oliweb.DB.DAO.UtilisateurDAO;
import com.oliweb.DB.DTO.AnnonceDTO;
import com.oliweb.DB.DTO.UtilisateurDTO;
import com.oliweb.DB.utility.ReturnClass;
import com.oliweb.S_MAIL.PropertiesMail;
import com.oliweb.S_MAIL.SendMail;
import com.oliweb.utility.Constants;
import com.oliweb.utility.Crypto;
import com.oliweb.utility.SessionIdentifierGenerator;
import com.oliweb.utility.Utility;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/utilisateurs")
public class Utilisateur {

    private static Gson gson = new Gson();
    private AnnonceDAO annonceDAO = new AnnonceDAO(MyConnection.getInstance());
    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO(MyConnection.getInstance());
    private MessageDAO messageDAO = new MessageDAO(MyConnection.getInstance());

    @GET
    @Path("{idUser}/annonces")
    @Produces(MediaType.APPLICATION_JSON)
    public String listAnnonceByUser(@PathParam("idUser") Integer idUser,
                                    @QueryParam("page") Integer page) {
        ReturnClass rs = new ReturnClass("listAnnonceByUser", false, null, null);
        ArrayList<AnnonceDTO> myList = annonceDAO.getByIdUser(idUser, page);
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
                             @QueryParam("telephoneUser") Integer telephoneUser){
        ReturnClass rs = new ReturnClass("updateUser", false, null, null);
        if (utilisateurDAO.existById(idUser)) {
            UtilisateurDTO user = utilisateurDAO.getById(idUser);
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
    public String doregister(@QueryParam("email") String email, @QueryParam("password") String encryptedPwd, @QueryParam("telephone") Integer telephone) {
        int result;
        ReturnClass rs = new ReturnClass("doregister", false, null, null);

        // On décrypte le mot de passe
        String decryptedPwd = (encryptedPwd != null) ? Crypto.desDecryptIt(encryptedPwd) : null;

        if (Utility.isNotNull(email) && Utility.isNotNull(decryptedPwd)) {
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(MyConnection.getInstance());
            result = utilisateurDAO.insert(email, decryptedPwd, telephone);
        } else {
            result = -4;
        }

        switch (result) {
            case 1:
                return doLogin(email, encryptedPwd);
            case -2:
                rs.setMsg("Cette adresse mail est déjà utilisée.");
                break;
            case -3:
                rs.setMsg("Insertion utilisateur échouée.");
                break;
            case -4:
                rs.setMsg("Email ou mot de passe vide");
                break;
        }
        return gson.toJson(rs);
    }

    @DELETE
    @Path("{idUser}")
    @Produces(MediaType.APPLICATION_JSON)
    public String unregister(@PathParam("idUser") Integer idUser){
        ReturnClass rs = new ReturnClass("unregister", false, null, null);
        if (utilisateurDAO.existById(idUser)) {
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
        ReturnClass rs = new ReturnClass("getnbuser", false, null, null);
        rs.setStatus(true);
        rs.setMsg(String.valueOf(utilisateurDAO.getNbUser()));
        return gson.toJson(rs);
    }

    @POST
    @Path("{idUser}/change-password")
    @Produces(MediaType.APPLICATION_JSON)
    public String changepassword(@PathParam("idUser") Integer idUser,
                                 @QueryParam("oldPassword") String oldEncryptedPwd,
                                 @QueryParam("newPassword") String newEncryptedPwd){
        ReturnClass rs = new ReturnClass("changepassword", false, null, null);

        String oldDecryptedPwd = null;
        String newDecryptedPwd = null;

        // On décrypte l'ancien et nouveau mot de passe
        if (oldEncryptedPwd != null) {
            oldDecryptedPwd = Crypto.desDecryptIt(oldEncryptedPwd);
        }
        if (newEncryptedPwd != null) {
            newDecryptedPwd = Crypto.desDecryptIt(newEncryptedPwd);
        }

        if (utilisateurDAO.existById(idUser)) {
            // Vérification que l'ancien password est le même que celui qu'on a renvoyé
            if (utilisateurDAO.getPasswordByIdUser(idUser).equals(oldDecryptedPwd)) {
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
        ReturnClass rs = new ReturnClass("doLogin", false, null, null);

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
    public String lostpassword(@QueryParam("email") String email){
        ReturnClass rs = new ReturnClass("lostpassword", false, null, null);

        if (utilisateurDAO.existByEmail(email)) {
            UtilisateurDTO user = utilisateurDAO.getByEmail(email);

            rs.setStatus(true);
            String newPassword = SessionIdentifierGenerator.nextSessionId();
            utilisateurDAO.changePassword(user.getIdUTI(), newPassword);
            PropertiesMail propertiesMail = new PropertiesMail(Constants.email_from, Constants.smtp_host, Constants.smtp_port, Constants.email_username, Constants.email_password);

            SendMail.executeTLS(propertiesMail, email, "Application " + Constants.APPLI_NAME + " - Mot de passe perdu", "Voici votre mot de passe : " + newPassword);

        } else {
            rs.setMsg("Email inconnu dans la base de données");
        }
        return gson.toJson(rs);
    }

    @GET
    @Path("{idUser}/messages")
    @Produces(MediaType.APPLICATION_JSON)
    public String listMessages(@PathParam("idUser") Integer idUser) {
        ReturnClass rs = new ReturnClass("listMessages", false, null, null);
        rs.setMsg(gson.toJson(messageDAO.listByIdSender(idUser)));
        return gson.toJson(rs);
    }
}
