package com.oliweb.restservice;

import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.oliweb.DB.DAO.AnnonceDAO;
import com.oliweb.DB.DAO.PhotoDAO;
import com.oliweb.DB.DAO.UtilisateurDAO;
import com.oliweb.DB.utility.ReturnClass;
import com.oliweb.utility.Constants;

//Path: http://localhost:8080/<appln-folder-name>/get
@Path("/get")
public class Get {

	private Gson gson = new Gson();

	/** 
	 * Méthode de vérification d'existence d'une photo
	 * @param idAnnonce
	 * @return
	 */
	@GET 
	@Path("/photoexist")
	@Produces(MediaType.APPLICATION_JSON) 
	public String photoexist(@QueryParam("idAnnonce") Integer idAnnonce, @QueryParam("idPhoto") Integer idPhoto, @QueryParam("namePhoto") String namePhoto){
		ReturnClass rs = new ReturnClass("photoexist", false, null, null);
		boolean exist = false;

		try {
			exist = PhotoDAO.existByAnnonceAndName(idAnnonce, idPhoto, namePhoto);
			if (exist){
				rs.setStatus(exist);
				rs.setMsg(Constants.same_name);
			}else{
				exist = PhotoDAO.existWithDifferentName(idAnnonce, idPhoto, namePhoto);
				rs.setStatus(exist);
				rs.setMsg(Constants.different_name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gson.toJson(rs);
	}
	
	/**
	 * 
	 * @param idAnnonce
	 * @return
	 */
	@GET
	@Path("/annoncebyid")
	@Produces(MediaType.APPLICATION_JSON) 
	public String annoncebyid(@QueryParam("idAnnonce") Integer idAnnonce){
		String response = "";

		try {
			response = gson.toJson(AnnonceDAO.getById(idAnnonce));
		} catch (Exception e) {
			System.out.println(response);
		}
		return response;
	}

	@GET 
	@Path("/checkconnection")
	@Produces(MediaType.APPLICATION_JSON) 
	public String CheckConnection(){
		ReturnClass rs = new ReturnClass("checkconnection", true, null, null);
		return gson.toJson(rs);
	}

	@GET 
	@Path("/getnbuser")
	@Produces(MediaType.APPLICATION_JSON) 
	public String getnbuser(){
		ReturnClass rs = new ReturnClass("getnbuser", false, null, null);
		
		try{
			 rs.setStatus(true);
			 rs.setMsg(String.valueOf(UtilisateurDAO.getNbUser()));
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
	@Path("/getnbannonce")
	@Produces(MediaType.APPLICATION_JSON) 
	public String getnbannonce(){
		ReturnClass rs = new ReturnClass("getnbannonce", false, null, null);
		
		try{
			 rs.setStatus(true);
			 rs.setMsg(String.valueOf(AnnonceDAO.getNbAnnonce()));
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
}
