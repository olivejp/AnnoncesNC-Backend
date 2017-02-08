package com.oliweb.restservice;

import java.sql.SQLException;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FilenameUtils;

import com.google.gson.Gson;
import com.oliweb.DB.DAO.AnnonceDAO;
import com.oliweb.DB.DAO.CategorieDAO;
import com.oliweb.DB.DAO.PhotoDAO;
import com.oliweb.DB.DAO.UtilisateurDAO;
import com.oliweb.DB.DTO.AnnonceDTO;
import com.oliweb.DB.DTO.PhotoDTO;
import com.oliweb.DB.DTO.UtilisateurDTO;
import com.oliweb.DB.utility.ConstantsDB;
import com.oliweb.DB.utility.ReturnClass;
import com.oliweb.S_MAIL.PropertiesMail;
import com.oliweb.S_MAIL.SendMail;
import com.oliweb.S_SMS.SendSms;
import com.oliweb.utility.Constants;
import com.oliweb.utility.Crypto;
import com.oliweb.utility.SessionIdentifierGenerator;

//Path: http://localhost/<appln-folder-name>/login
@Path("/post")
public class Post {

	private static Gson gson = new Gson();

	@GET
	@Path("/dopostannonce")
	@Produces(MediaType.APPLICATION_JSON) 
	public String dopostannonce(@QueryParam("idCat") Integer idCat, @QueryParam("idUser") Integer idUser, @QueryParam("idAnnonce") Integer idAnnonce, @QueryParam("titre") String titre 
			, @QueryParam("description") String description, @QueryParam("prix") Integer prix) throws SQLException {
		String tag = "dopostannonce";
		String response = "";
		ReturnClass rs = new ReturnClass(tag, false, null, null);

		try{
			// V�rification que l'utilisateur existe bien  && V�rification que la cat�gorie existe bien
			if (CategorieDAO.existById(idCat) && UtilisateurDAO.existById(idUser)){

				// R�cup�ration des donn�es
				AnnonceDTO annonce = new AnnonceDTO();
				annonce.setIdANO(idAnnonce);
				annonce.setTitreANO(titre);
				annonce.setDescriptionANO(description);
				annonce.setPriceANO(prix);
				annonce.setCategorieANO(CategorieDAO.getById(idCat));
				annonce.setOwnerANO(UtilisateurDAO.getById(idUser));
				
				if (AnnonceDAO.exist(idAnnonce)){
					// **** En mode MODIFICATION ****
					if (AnnonceDAO.update(annonce)){
						rs.setId(annonce.getIdANO());
						rs.setStatus(true);
					}else{
						rs.setMsg("L'annonce n'a pas pu être mise à jour dans la BD");
					}
				}else{
					// **** En mode CREATION ****
					// L'utilisateur n'a le droit de poster que 5 annonces sauf si c'est un Admin
					if (((AnnonceDAO.numberAnnonceByIdUser(idUser) < ConstantsDB.MAX_ANNONCE) || (UtilisateurDAO.checkAdmin(idUser)))){
						if (AnnonceDAO.insert(annonce)){
							// Dans l'insert, on r�cup�re l'ID de l'annonce qui vient d'�tre cr��e.
							// On renvoie l'ID 
							rs.setId(annonce.getIdANO());
							rs.setStatus(true);
						}else{
							rs.setMsg("L'annonce n'a pas pu être créée dans la BD");
						}
					}else{
						rs.setMsg("Vous avez atteint votre quota d'annonce. " +String.valueOf(ConstantsDB.MAX_ANNONCE) + " annonces maximum par utilisateur.");
					}
				}
			}
		}
		catch(Exception e){
			if (e instanceof SQLException){
				SQLException sqle = (SQLException) e;
				rs.setMsg(Constants.SERVER_EXCEPTION + " SQLState:" + sqle.getSQLState() + " SQLCode:" + sqle.getErrorCode());
			}else{
				rs.setMsg(Constants.SERVER_EXCEPTION);				
			}
			System.out.println(response);
		}
		return gson.toJson(rs);		
	}

	@GET
	@Path("/dodeleteallphoto")
	@Produces(MediaType.APPLICATION_JSON) 
	public String dodeleteallphoto(@QueryParam("idAnnonce") Integer idAnnonce) throws SQLException {
		String tag = "dodeleteallphoto";
		ReturnClass rs = new ReturnClass(tag, false, null, null);

		try{
			// V�rification que l'annonce existe bien
			if (AnnonceDAO.exist(idAnnonce)){
				if (PhotoDAO.deleteByIdAnnonce(idAnnonce)){
					rs.setStatus(true);
				}else{
					rs.setMsg("Les photos de l'annonce n'ont pas pu être supprimées.");
				}
			}
		}
		catch(Exception e){
			if (e instanceof SQLException){
				SQLException sqle = (SQLException) e;
				rs.setMsg(Constants.SERVER_EXCEPTION + " SQLState:" + sqle.getSQLState() + " SQLCode:" + sqle.getErrorCode());
			}else{
				rs.setMsg(Constants.SERVER_EXCEPTION); 
			}
			e.printStackTrace();
		}

		return gson.toJson(rs);	
	}
	
	
	@GET
	@Path("/dodeletephoto")
	@Produces(MediaType.APPLICATION_JSON) 
	public String dodeletephoto(@QueryParam("idAnnonce") Integer idAnnonce, @QueryParam("idPhoto") Integer idPhoto) throws SQLException {
		String tag = "dodeletephoto";
		ReturnClass rs = new ReturnClass(tag, false, null, null);

		try{
			// V�rification que l'annonce existe bien
			if (AnnonceDAO.exist(idAnnonce)){
				if (PhotoDAO.delete(idAnnonce, idPhoto)){
					rs.setStatus(true);
				}else{
					rs.setMsg("La photo de l'annonce n'a pas pu être supprimée.");
				}
			}
		}
		catch(Exception e){
			if (e instanceof SQLException){
				SQLException sqle = (SQLException) e;
				rs.setMsg(Constants.SERVER_EXCEPTION + " SQLState:" + sqle.getSQLState() + " SQLCode:" + sqle.getErrorCode());
			}else{
				rs.setMsg(Constants.SERVER_EXCEPTION); 
			}
			e.printStackTrace();
		}

		return gson.toJson(rs);	
	}

	@GET
	@Path("/dopostphoto")
	@Produces(MediaType.APPLICATION_JSON) 
	public String dopostphoto(@QueryParam("idAnnonce") Integer idAnnonce, @QueryParam("idPhoto") Integer idPhoto, @QueryParam("nomPhoto") String nomPhoto) throws SQLException {
		PhotoDTO photoDTO;
		String tag = "dopostphoto";
		ReturnClass rs = new ReturnClass(tag, false, null, null);

		// R�cup�ration du filename qu'on vient de recevoir
		String newName = FilenameUtils.getName(nomPhoto);
		
		try{
			// V�rification que l'annonce existe bien
			if (AnnonceDAO.exist(idAnnonce)){

				// V�rification si la photo existe déjà
				photoDTO = PhotoDAO.getById(idAnnonce, idPhoto);
				if (photoDTO!=null){
					// La photo existe déjà, on va vérifier si elle a changé de nom ou pas
					String presentName = FilenameUtils.getName(photoDTO.getNamePhoto()); 
					if (!presentName.equals(newName)){
						// La photo existe mais ne porte pas le même nom qu'auparavant
						// On va mettre à jour notre photo
						photoDTO.setNamePhoto(ConstantsDB.upload_directory + newName);
						if (PhotoDAO.update(photoDTO)){
							rs.setStatus(true);
							rs.setId(photoDTO.getIdPhoto());
							rs.setMsg(nomPhoto);
						}else{
							rs.setMsg("La photo n'a pas pu �tre mise � jour dans la BD");
						}
					}else{
						// La photo n'a pas changée, elle existe déjà
						rs.setStatus(true);
						rs.setMsg(Constants.PHOTO_ALREADY_EXIST);
					}
				}else{
					// La photo n'existe pas, il faut la créer
					// TODO - Dans le cas où la photo n'a pas pu être créée il faut gérer l'erreur
					photoDTO = new PhotoDTO();
					photoDTO.setNamePhoto(ConstantsDB.upload_directory + newName);
					photoDTO.setIdAnnoncePhoto(idAnnonce);
					Integer newId = PhotoDAO.insert(photoDTO);
					if (newId!=0){
						photoDTO.setIdPhoto(newId);
						rs.setStatus(true);
						rs.setId(newId);
						rs.setMsg(nomPhoto);
					}else{
						rs.setMsg("La photo n'a pas pu �tre cr��e dans la BD");
					}
				}
			}else{
				rs.setMsg("L'annonce n°:" + String.valueOf(idAnnonce) + " n'existe pas");
			}

		}
		catch(Exception e){
			if (e instanceof SQLException){
				SQLException sqle = (SQLException) e;
				rs.setMsg(Constants.SERVER_EXCEPTION + " SQLState:" + sqle.getSQLState() + " SQLCode:" + sqle.getErrorCode());
			}else{
				rs.setMsg(Constants.SERVER_EXCEPTION); 
			}
			e.printStackTrace();
		}

		return gson.toJson(rs);	
	}

	@DELETE
	@Path("/deleteannonce")
	@Produces(MediaType.APPLICATION_JSON) 
	public String deleteannonce(@QueryParam("idAnnonce") Integer idAnnonce) throws SQLException {
		String tag = "deleteannonce";
		ReturnClass rs = new ReturnClass(tag, false, null, null);

		try{
			// Vérification que l'annonce existe bien
			if (AnnonceDAO.exist(idAnnonce)){	
				if (AnnonceDAO.delete(idAnnonce)){
					rs.setStatus(true);
					rs.setMsg("Annonce bien supprimée.");
				}
			}
		}
		catch(Exception e){
			if (e instanceof SQLException){
				SQLException sqle = (SQLException) e;
				rs.setMsg(Constants.SERVER_EXCEPTION + " SQLState:" + sqle.getSQLState() + " SQLCode:" + sqle.getErrorCode());
			}else{
				rs.setMsg(Constants.SERVER_EXCEPTION); 
			}
			e.printStackTrace();
		}

		return gson.toJson(rs);	
	}

	@GET
	@Path("/updateannonce")
	@Produces(MediaType.APPLICATION_JSON) 
	public String updateannonce(@QueryParam("idAnnonce") Integer idAnnonce,@QueryParam("idCat") Integer idCat, @QueryParam("titre") String titre 
			, @QueryParam("description") String description, @QueryParam("prix") Integer prix) throws SQLException {
		String tag = "updateannonce";
		ReturnClass rs = new ReturnClass(tag, false, null, null);

		try{
			// V�rification que l'annonce existe bien
			if (AnnonceDAO.exist(idAnnonce)){

				// TODO - Dans le cas o� la photo n'a pas pu �tre cr��e il faut g�rer l'erreur
				AnnonceDTO annonce = AnnonceDAO.getById(idAnnonce);
				annonce.setCategorieANO(CategorieDAO.getById(idCat));
				annonce.setTitreANO(titre);
				annonce.setPriceANO(prix);
				annonce.setDescriptionANO(description);

				if (AnnonceDAO.update(annonce)){
					rs.setStatus(true);
				}else{
					rs.setMsg("L'annonce n'a pas pu �tre mise à jour dans la BD");
				}

			}
		}
		catch(Exception e){
			if (e instanceof SQLException){
				SQLException sqle = (SQLException) e;
				rs.setMsg(Constants.SERVER_EXCEPTION + " SQLState:" + sqle.getSQLState() + " SQLCode:" + sqle.getErrorCode());
			}else{
				rs.setMsg(Constants.SERVER_EXCEPTION); 
			}
			e.printStackTrace();
		}

		return gson.toJson(rs);	
	}

	@GET
	@Path("/lostpassword")
	@Produces(MediaType.APPLICATION_JSON) 
	public String lostpassword(@QueryParam("email") String email) throws SQLException {
		String tag = "lostpassword";
		ReturnClass rs = new ReturnClass(tag, false, null, null);

		try{
			// V�rification que l'utilisateur existe bien
			if (UtilisateurDAO.existByEmail(email)){

				UtilisateurDTO user = UtilisateurDAO.getByEmail(email);

				// V�rification qu'il existe un mot de passe perdu pour cet email et qu'il n'est pas trait�
				/*		if (!LostPasswordDAO.existNonTraiteByEmail(email)){
					if (LostPasswordDAO.insert(email)){*/
				rs.setStatus(true);
				String newPassword = SessionIdentifierGenerator.nextSessionId();
				UtilisateurDAO.changePassword(user.getIdUTI(), newPassword);
				PropertiesMail propertiesMail = new PropertiesMail(Constants.email_from, Constants.smtp_host,Constants.smtp_port,Constants.email_username,Constants.email_password);
				
				SendMail.executeTLS(propertiesMail, email, "Application " + Constants.APPLI_NAME + " - Mot de passe perdu", "Voici votre mot de passe : " + newPassword);
				/*	}else{
						rs.setMsg("La demande n'a pas abouti !");
					}
				}else{
					rs.setMsg("Une demande a d�j� �t� faite pour ce compte");	
				}*/

				// Suppression du mot de passe perdu.
				// LostPasswordDAO.deleteByEmail(email);
			}else{
				rs.setMsg("Email inconnu dans la base de données");
			}
		}
		catch(Exception e){
			if (e instanceof SQLException){
				SQLException sqle = (SQLException) e;
				rs.setMsg(Constants.SERVER_EXCEPTION + " SQLState:" + sqle.getSQLState() + " SQLCode:" + sqle.getErrorCode());
			}else{
				rs.setMsg(Constants.SERVER_EXCEPTION); 
			}
			e.printStackTrace();
		}

		return gson.toJson(rs);	
	}

	@GET
	@Path("/updateuser")
	@Produces(MediaType.APPLICATION_JSON) 
	public String updateuser(@QueryParam("idUser") Integer idUser, @QueryParam("emailUser") String emailUser, @QueryParam("telephoneUser") Integer telephoneUser) throws SQLException {
		String tag = "updateuser";
		ReturnClass rs = new ReturnClass(tag, false, null, null);

		try{
			// V�rification que l'utilisateur existe bien
			if (UtilisateurDAO.existById(idUser)){

				UtilisateurDTO user = UtilisateurDAO.getById(idUser);
				user.setEmailUTI(emailUser);
				user.setTelephoneUTI(telephoneUser);

				if (UtilisateurDAO.update(user)){
					rs.setStatus(true);
				}else{
					rs.setMsg("L'utilisateur n'a pas pu �tre mis à jour dans la BD");
				}
			}
		}
		catch(Exception e){
			if (e instanceof SQLException){
				SQLException sqle = (SQLException) e;
				rs.setMsg(Constants.SERVER_EXCEPTION + " SQLState:" + sqle.getSQLState() + " SQLCode:" + sqle.getErrorCode());
			}else{
				rs.setMsg(Constants.SERVER_EXCEPTION); 
			}
			e.printStackTrace();
		}

		return gson.toJson(rs);	
	}

	@GET
	@Path("/unregister")
	@Produces(MediaType.APPLICATION_JSON) 
	public String unregister(@QueryParam("idUser") Integer idUser) throws SQLException {
		String tag = "unregister";
		ReturnClass rs = new ReturnClass(tag, false, null, null);
		System.out.println("Tentative de désinscription");

		try{
			// Vérification que l'utilisateur existe bien
			if (UtilisateurDAO.existById(idUser)){
				if (UtilisateurDAO.unregisterUser(idUser)){
					rs.setStatus(true);
				}else{
					rs.setMsg("L'utilisateur n'a pas pu être supprimé");
				}
			}
		}
		catch(Exception e){
			if (e instanceof SQLException){
				SQLException sqle = (SQLException) e;
				rs.setMsg(Constants.SERVER_EXCEPTION + " SQLState:" + sqle.getSQLState() + " SQLCode:" + sqle.getErrorCode());
			}else{
				rs.setMsg(Constants.SERVER_EXCEPTION); 
			}
			e.printStackTrace();
		}

		return gson.toJson(rs);	
	}

	@GET
	@Path("/changepassword")
	@Produces(MediaType.APPLICATION_JSON) 
	public String changepassword(@QueryParam("idUser") Integer idUser, @QueryParam("oldPassword") String oldEncryptedPwd, @QueryParam("newPassword") String newEncryptedPwd) throws SQLException {
		String tag = "changepassword";
		ReturnClass rs = new ReturnClass(tag, false, null, null);

		String oldDecryptedPwd = null;
		String newDecryptedPwd = null;

		// On d�crrypte l'ancien et nouveau mot de passe 
		if (oldEncryptedPwd != null){
			oldDecryptedPwd = Crypto.desDecryptIt(oldEncryptedPwd);
		}
		if (newEncryptedPwd != null){
			newDecryptedPwd = Crypto.desDecryptIt(newEncryptedPwd);
		}

		try{
			// V�rification que l'utilisateur existe bien
			if (UtilisateurDAO.existById(idUser)){
				// V�rification que l'ancien password est le m�me que celui qu'on a renvoy�
				if (UtilisateurDAO.getPasswordByIdUser(idUser).equals(oldDecryptedPwd)){
					if (UtilisateurDAO.changePassword(idUser, newDecryptedPwd)){
						rs.setStatus(true);
					}else{
						rs.setMsg("Le mot de passe n'a pas pu être changé.");
					}
				}else{
					rs.setMsg("Erreur sur l'ancien mot de passe.");
				}
			}else{
				rs.setMsg("L'utilisateur est inconnu.");
			}
		}
		catch(Exception e){
			if (e instanceof SQLException){
				SQLException sqle = (SQLException) e;
				rs.setMsg(Constants.SERVER_EXCEPTION + " SQLState:" + sqle.getSQLState() + " SQLCode:" + sqle.getErrorCode());
			}else{
				rs.setMsg(Constants.SERVER_EXCEPTION); 
			}
			e.printStackTrace();
		}

		return gson.toJson(rs);	
	}

	@GET
	@Path("/sendsms")
	@Produces(MediaType.APPLICATION_JSON) 
	public String SendSms(@QueryParam("to") String to, @QueryParam("from") String from, @QueryParam("body") String body) {
		String tag = "sendsms";
		boolean retour = false;
		String message;
		ReturnClass rs = new ReturnClass(tag, false, null, null);
		message = SendSms.send(from, to, body, retour);
		rs.setMsg(message);
		rs.setStatus(retour);
		return gson.toJson(rs);	
	}
}
