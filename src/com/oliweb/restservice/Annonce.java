package com.oliweb.restservice;

import com.google.gson.Gson;
import com.oliweb.DB.DAO.AnnonceDAO;
import com.oliweb.DB.DTO.AnnonceDTO;
import com.oliweb.DB.utility.ReturnClass;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/annonces")
public class Annonce {
    private static Gson gson = new Gson();

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
    @Produces(MediaType.APPLICATION_JSON)
    public String listAnnonce(@QueryParam("page") Integer page){
        ReturnClass rs = new ReturnClass("list_annonce", false, null, null);

        ArrayList<AnnonceDTO> myList = null;

        try {
            myList = AnnonceDAO.getListAnnonce(page);
        } catch (Exception e) {
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

    @GET
    @Path("/listannoncebycategory")
    @Produces(MediaType.APPLICATION_JSON)
    public String listAnnonceByCategory(@QueryParam("idCategory") Integer idCategory){
        String reponse = null;

        try {
            reponse = gson.toJson(AnnonceDAO.getByIdCategory(idCategory));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reponse;
    }

    @GET
    @Path("/listannoncebycategorywithpage")
    @Produces(MediaType.APPLICATION_JSON)
    public String listAnnonceByCategorywithpage(@QueryParam("idCategory") Integer idCategory, @QueryParam("page") Integer page){
        String reponse = null;

        try {
            reponse = gson.toJson(AnnonceDAO.getByIdCategory1(idCategory, page));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reponse;
    }

    @GET
    @Path("/listannoncebyuserwithpage")
    @Produces(MediaType.APPLICATION_JSON)
    public String listAnnonceByUser(@QueryParam("idUser") Integer idUser, @QueryParam("page") Integer page){
        String reponse = null;

        try {
            reponse = gson.toJson(AnnonceDAO.getByIdUser(idUser, page));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reponse;
    }
}
