package com.oliweb.restservice;

import java.sql.Connection;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.oliweb.DB.DAO.MyConnection;
import com.oliweb.DB.DAO.UtilisateurDAO;
import com.oliweb.utility.Constants;
import com.oliweb.utility.Utility;

//Path: http://localhost/<appln-folder-name>/search
@Path("/sql")
public class Sql {
	// HTTP Get Method
	// Path: http://localhost/<appln-folder-name>/search/dosearch
	// Produces JSON as response
	// Query parameters are parameters: http://localhost/<appln-folder-name>/search/dosearch?keyword=abc
	@GET 
	@Path("/dosql")
	@Produces(MediaType.APPLICATION_JSON) 
	public String doSql(@QueryParam("email") String email, @QueryParam("password") String password, @QueryParam("query") String query){
		// TODO - Permet d'ouvrir un acc�s aux admins pour faire des MAJ directement sur la base de donn�es
		String retour = null;
		String tag = "doSql";
		try {
			if (UtilisateurDAO.checkAdmin(email, password)){
				Connection dbConn = MyConnection.getInstance();
				Statement stmt = dbConn.createStatement();
				stmt.executeUpdate(query);
				stmt.close();
				retour = Utility.constructJSON(tag, true, "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			retour = Utility.constructJSON(tag, false, Constants.SERVER_EXCEPTION);
		}
		return retour;
	}
}