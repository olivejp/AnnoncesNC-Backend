package com.oliweb.restservice;


import com.google.gson.Gson;
import com.oliweb.DB.DAO.CategorieDAO;
import com.oliweb.DB.DTO.CategorieDTO;
import com.oliweb.DB.utility.ReturnClass;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/categories")
public class Categorie {

    private static Gson gson = new Gson();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listCategorie(){
        ReturnClass rs = new ReturnClass("listcategory", false, null, null);

        ArrayList<CategorieDTO> myList = null;

        try {
            myList = CategorieDAO.getCompleteList();
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
}
