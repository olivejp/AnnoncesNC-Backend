package com.oliweb.restservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.oliweb.DB.DAO.UtilisateurDAO;
import com.oliweb.DB.utility.ReturnClass;
import com.oliweb.utility.Crypto;
import com.oliweb.utility.Utility;


//Path: http://localhost/<appln-folder-name>/register
@Path("/register")
public class Register {

	private static Gson gson = new Gson();


	// HTTP Get Method
	// Path: http://localhost/<appln-folder-name>/register/doregister
	// Produces JSON as response
	// Query parameters are parameters: http://localhost/<appln-folder-name>/register/doregister?name=pqrs&username=abc&password=xyz
	@GET 
	@Path("/doregister")
	@Produces(MediaType.APPLICATION_JSON) 
	public String doregister(@QueryParam("email") String email, @QueryParam("password") String encryptedPwd, @QueryParam("telephone") Integer telephone){
		String tag = "doregister";
		ReturnClass rs = new ReturnClass(tag, false, null, null);

		String decryptedPwd = null;

		// On d�crypte le mot de passe
		if (encryptedPwd != null){
			decryptedPwd = Crypto.desDecryptIt(encryptedPwd);
		}

		switch (registerUser(email, decryptedPwd, telephone)){
		case 1: 
			try {
				return Login.dologin(email, encryptedPwd);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				rs.setMsg("Une erreur a été récupérée sur le serveur.");
			}

			break;
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

	/**
	 * Method Register user
	 * -1 = Fail 
	 * -2 = Email déjà présent 
	 * -3 = erreur sql 
	 * -4 = email ou mot de passe vide
	 */
	private int registerUser(String email, String pwd, Integer telephone){
		// System.out.println("Dans l'enregistrement");
		int result = 0;
		if (Utility.isNotNull(email) && Utility.isNotNull(pwd)){
			try {
				result = UtilisateurDAO.insert(email, pwd, telephone);
			} catch(Exception sqle){
				System.out.println(sqle.getMessage());
				result = -3;
			}
		}else{
			result = -4;
		}
		return result;
	}
}
