package com.oliweb.restservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.oliweb.DB.DAO.UtilisateurDAO;
import com.oliweb.DB.DTO.UtilisateurDTO;
import com.oliweb.DB.utility.ReturnClass;
import com.oliweb.utility.Constants;
import com.oliweb.utility.Crypto;
import com.oliweb.utility.Utility;

//Path: http://localhost/<appln-folder-name>/login
@Path("/login")
public class Login {

	private static Gson gson = new Gson();
		
	@GET 
	@Path("/dologin")
	@Produces(MediaType.APPLICATION_JSON) 
	public static String dologin(@QueryParam("email") String email, @QueryParam("password") String encryptedPwd) throws Exception{
		ReturnClass rs = new ReturnClass("dologin", false, null, null);
		String decryptPwd = null;
		
		// On d�crypte le mot de passe
		if (encryptedPwd != null){
			decryptPwd = Crypto.desDecryptIt(encryptedPwd);
		}
		
		if(checkCredentials(email, decryptPwd)){
			// On sait que l'utilisateur existe, on va le r�cup�rer et renvoyer les donn�es
			try{
				// R�cup�ration de l'utilisateur dans la base de donn�es
				UtilisateurDTO utilisateur = UtilisateurDAO.getByEmail(email);
				rs.setStatus(true);
				rs.setMsg(gson.toJson(utilisateur));
				
				// Mise � jour de la date de derni�re connexion de l'utilisateur
				UtilisateurDAO.updateDateLastConnexion(utilisateur.getIdUTI());
			}
			catch(Exception e){
				rs.setMsg(Constants.SERVER_EXCEPTION);
				throw e;
			}
		}else{
			rs.setMsg("Email ou Mot de passe incorrect");
		}
		System.out.println(rs.toString());
		return gson.toJson(rs);		
	}
	
	/**
	 * M�thode pour v�rifier que les identifiants rentr�s sont valides
	 * @param email
	 * @param pwd
	 * @return
	 */
	private static boolean checkCredentials(String email, String pwd){
		System.out.println("V�rification authentification");
		boolean result = false;
		if(Utility.isNotNull(email) && Utility.isNotNull(pwd)){
			try {
				result = UtilisateurDAO.checkLogin(email, pwd);
				System.out.println("Inside checkCredentials try "+result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Inside checkCredentials catch : " + e.getMessage());
				result = false;
			}
		}else{
			result = false;
		}

		return result;
	}

}
