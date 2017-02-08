package com.oliweb.restservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.oliweb.DB.DAO.AnnonceDAO;

//Path: http://localhost/<appln-folder-name>/search
@Path("/search")
public class Search {
	
	private static Gson gson = new Gson();
	
	/**
	 * Retrofit méthode pour retrouver les annonces par mots clés
	 * @param keyword
	 * @param page
	 * @return
	 */
	@GET 
	@Path("/dosearchwithpage")
	@Produces(MediaType.APPLICATION_JSON) 
	public String DoSearchWithPage(@QueryParam("keyword") String keyword, @QueryParam("page") Integer page){
		String response = "";

		try {
			response = gson.toJson(AnnonceDAO.searchByKeywordWithPage(keyword, page));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * 
	 * @param idCat
	 * @param minPrice
	 * @param maxPrice
	 * @param keyword
	 * @param photo
	 * @param page
	 * @return
	 */
	@GET 
	@Path("/dosearchmultiparam")
	@Produces(MediaType.APPLICATION_JSON) 
	public String dosearchmultiparam(@QueryParam("idCat") Integer idCat, @QueryParam("minPrice") Integer minPrice,
			@QueryParam("maxPrice") Integer maxPrice, @QueryParam("keyword") String keyword,
			@QueryParam("photo") boolean photo, @QueryParam("page") Integer page){
		
		String reponse = null;

		try {
			reponse = gson.toJson(AnnonceDAO.getMultiParam(idCat, keyword, minPrice, maxPrice, photo, page));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reponse;
	}
}
