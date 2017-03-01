package com.oliweb.restservice;


import com.google.gson.Gson;
import com.oliweb.db.dao.AnnonceDAO;
import com.oliweb.db.dao.CategorieDAO;
import com.oliweb.db.dao.MyConnection;
import com.oliweb.db.dto.AnnonceDTO;
import com.oliweb.db.dto.CategorieDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/categories")
public class CategorieRestService {

    private static Gson gson = new Gson();
    private CategorieDAO categorieDAO = new CategorieDAO(MyConnection.getInstance());
    private AnnonceDAO annonceDAO = new AnnonceDAO(MyConnection.getInstance());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getListCategorie() {
        ReturnWS rs = new ReturnWS("listcategory", false, null, null);
        List<CategorieDTO> myList = categorieDAO.getCompleteList();
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
    public String getAnnoncesByIdCategorieWithPage(@PathParam("idCategory") Integer idCategory,
                                                   @QueryParam("page") Integer page) {
        ReturnWS rs = new ReturnWS("getAnnoncesByIdCategorieWithPage", false, null, null);
        List<AnnonceDTO> myList;

        if (page != null) {
            myList = annonceDAO.getByIdCategoryWithPage(idCategory, page);
        } else {
            myList = annonceDAO.getByIdCategory(idCategory);
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
