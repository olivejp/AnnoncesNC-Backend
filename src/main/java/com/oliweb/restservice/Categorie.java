package com.oliweb.restservice;


import com.google.gson.Gson;
import com.oliweb.DB.DAO.AnnonceDAO;
import com.oliweb.DB.DAO.CategorieDAO;
import com.oliweb.DB.DTO.AnnonceDTO;
import com.oliweb.DB.DTO.CategorieDTO;
import com.oliweb.DB.utility.ReturnClass;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/categories")
public class Categorie {

    private static Gson gson = new Gson();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listCategorie() {
        ReturnClass rs = new ReturnClass("listcategory", false, null, null);
        ArrayList<CategorieDTO> myList = CategorieDAO.getCompleteList();
        if (myList != null) {
            rs.setStatus(true);
            if (!myList.isEmpty()) {
                rs.setMsg(gson.toJson(myList));
            }
        }
        return gson.toJson(rs);
    }

    @GET
    @Path("{idCategory}/annonces")
    @Produces(MediaType.APPLICATION_JSON)
    public String listAnnonceByCategoryWithPage(@PathParam("idCategory") Integer idCategory, @QueryParam("page") Integer page) {

        ReturnClass rs = new ReturnClass("listAnnonceByCategoryWithPage", false, null, null);
        ArrayList<AnnonceDTO> myList;

        if (page != null) {
            myList = AnnonceDAO.getByIdCategoryWithPage(idCategory, page);
        } else {
            myList = AnnonceDAO.getByIdCategory(idCategory);
        }

        if (myList != null) {
            rs.setStatus(true);
            if (!myList.isEmpty()) {
                rs.setMsg(gson.toJson(myList));
            }
        }
        return gson.toJson(rs);
    }
}