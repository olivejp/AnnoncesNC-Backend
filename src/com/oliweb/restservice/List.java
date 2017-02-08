package com.oliweb.restservice;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.oliweb.DB.DAO.AnnonceDAO;
import com.oliweb.DB.DAO.CategorieDAO;
import com.oliweb.DB.DAO.MessageDAO;
import com.oliweb.DB.DAO.MyConnection;
import com.oliweb.DB.DAO.TransactionDAO;
import com.oliweb.DB.DTO.CategorieDTO;
import com.oliweb.DB.utility.ReturnClass;

//Path: http://localhost/<appln-folder-name>/login
@Path("/list")
public class List {

	private static Gson gson = new Gson();

	/**
	 * Retrofit m�thode pour r�up�ration de la liste des cat�gories
	 * @return
	 */
	@GET 
	@Path("/listcategory")
	@Produces(MediaType.APPLICATION_JSON) 
	public String listcategory() {
		ReturnClass rs = new ReturnClass("listcategory", false, null, null);

		// R�cup�ration de la liste des cat�gories dans la DB
		ArrayList<CategorieDTO> myList = null;

		try {
			myList = CategorieDAO.getCompleteList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (myList != null){
			if(!myList.isEmpty()){
				rs.setStatus(true);
				rs.setMsg(gson.toJson(myList));
			}
		}

		return gson.toJson(rs);	
	}


	/**
	 * RETROFIT M�thode pour r�cup�rer une liste d'annonce d'une cat�gorie pr�cise
	 * @param idCategory
	 * @return
	 */
	@GET 
	@Path("/listannoncebycategory")
	@Produces(MediaType.APPLICATION_JSON) 
	public String listAnnonceByCategory(@QueryParam("idCategory") Integer idCategory){
		String reponse = null;

		try {
			reponse = gson.toJson(AnnonceDAO.getByIdCategory(idCategory));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reponse;
	}


	// HTTP Get Method
	// Path: http://localhost/<appln-folder-name>/list/listannoncebycategorywithpage
	// Produces JSON as response
	// Query parameters are parameters: http://localhost/<appln-folder-name>/login/dologin?username=abc&password=xyz
	@GET 
	@Path("/listannoncebycategorywithpage")
	@Produces(MediaType.APPLICATION_JSON) 
	public String listAnnonceByCategorywithpage(@QueryParam("idCategory") Integer idCategory, @QueryParam("page") Integer page){
		String reponse = null;

		try {
			reponse = gson.toJson(AnnonceDAO.getByIdCategory1(idCategory, page));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return reponse;
	}

	/**
	 * RETROFIT M�thode pour r�cup�rer une liste d'annonce d'un utilisateur
	 * @param idUser
	 * @return
	 */
	@GET 
	@Path("/listannoncebyuserwithpage")
	@Produces(MediaType.APPLICATION_JSON) 
	public String listAnnonceByUser(@QueryParam("idUser") Integer idUser, @QueryParam("page") Integer page){
		String reponse = null;

		try {
			reponse = gson.toJson(AnnonceDAO.getByIdUser(idUser, page));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reponse;
	}

	
	/**
	 * RETROFIT M�thode pour r�cup�rer la liste des transactions pas encore r�cup�r�es.
	 * Renvoie au format JSON, un ArrayList de TransactionDTO
	 */
	@GET 
	@Path("/listtransaction")
	@Produces(MediaType.APPLICATION_JSON) 
	public String listtransaction(){
		String reponse = null;

		try {
			reponse = gson.toJson(TransactionDAO.getList(MyConnection.getInstance()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reponse;
	}

	
	/**
	 * RETROFIT M�thode pour r�cup�rer la liste des messages d'un utilisateur.
	 * Renvoie au format JSON, un ArrayList de MessageDTO
	 */
	@GET 
	@Path("/listmessage")
	@Produces(MediaType.APPLICATION_JSON) 
	public String listmessage(@QueryParam("idSender") Integer idSender){
		String reponse = null;

		try {
			reponse = gson.toJson(MessageDAO.listByIdSender(idSender));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reponse;
	}

}
