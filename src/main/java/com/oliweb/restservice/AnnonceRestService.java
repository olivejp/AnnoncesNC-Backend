package com.oliweb.restservice;

import com.google.gson.Gson;
import com.oliweb.db.dao.*;
import com.oliweb.db.dto.AnnonceDTO;
import com.oliweb.utility.Proprietes;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;

@Path("/annonces")
public class AnnonceRestService {
    private static Gson gson = new Gson();
    private CategorieDAO categorieDAO = new CategorieDAO(MyConnection.getInstance());
    private AnnonceDAO annonceDAO = new AnnonceDAO(MyConnection.getInstance());
    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO(MyConnection.getInstance());
    private PhotoDAO photoDAO = new PhotoDAO(MyConnection.getInstance());

    @GET
    @Path("{idAnnonce}")
    @Produces(MediaType.APPLICATION_JSON)
    public String annonceById(@PathParam("idAnnonce") Integer idAnnonce) {
        ReturnClass rs = new ReturnClass("annonceById", true, null, null);
        rs.setMsg(gson.toJson(annonceDAO.get(idAnnonce)));
        return gson.toJson(rs);
    }

    @PUT
    @Path("{idAnnonce}")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateannonce(@PathParam("idAnnonce") Integer idAnnonce,
                                @QueryParam("idCat") Integer idCat,
                                @QueryParam("titre") String titre,
                                @QueryParam("description") String description,
                                @QueryParam("prix") Integer prix) {
        ReturnClass rs = new ReturnClass("updateannonce", false, null, null);
        if (annonceDAO.get(idAnnonce) != null) {
            AnnonceDTO annonce = annonceDAO.get(idAnnonce);
            annonce.setCategorieANO(categorieDAO.get(idCat));
            annonce.setTitreANO(titre);
            annonce.setPriceANO(prix);
            annonce.setDescriptionANO(description);

            if (annonceDAO.update(annonce)) {
                rs.setStatus(true);
            } else {
                rs.setMsg("L'annonce n'a pas pu être mise à jour dans la BD");
            }
        }
        return gson.toJson(rs);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String postannonce(@FormParam("idCat") Integer idCat,
                              @FormParam("idUser") Integer idUser,
                              @FormParam("titre") String titre,
                              @FormParam("description") String description,
                              @FormParam("prix") Integer prix) {

        ReturnClass rs = new ReturnClass("dopostannonce", false, null, null);

        if (categorieDAO.existById(idCat) && utilisateurDAO.existById(idUser)) {
            AnnonceDTO annonce = new AnnonceDTO();
            annonce.setTitreANO(titre);
            annonce.setDescriptionANO(description);
            annonce.setPriceANO(prix);
            annonce.setCategorieANO(categorieDAO.get(idCat));
            annonce.setOwnerANO(utilisateurDAO.getById(idUser));

            // L'utilisateur n'a le droit de poster que 5 annonces sauf si c'est un Admin
            Integer nbMax = Integer.valueOf(Proprietes.getProperty(Proprietes.MAX_ANNONCE));
            if ((annonceDAO.numberAnnonceByIdUser(idUser) < nbMax) || (utilisateurDAO.checkAdmin(idUser))) {
                if (annonceDAO.save(annonce)) {
                    rs.setId(annonce.getIdANO());
                    rs.setStatus(true);
                } else {
                    rs.setMsg("L'annonce n'a pas pu être créée dans la BD");
                }
            } else {
                rs.setMsg("Vous avez atteint votre quota d'annonce. " + String.valueOf(Proprietes.MAX_ANNONCE) + " annonces maximum par utilisateur.");
            }
        }
        return gson.toJson(rs);
    }

    @DELETE
    @Path("{idAnnonce}")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteannonce(@PathParam("idAnnonce") Integer idAnnonce) {
        ReturnClass rs = new ReturnClass("deleteannonce", false, null, null);
        if (annonceDAO.get(idAnnonce) != null && annonceDAO.delete(idAnnonce)) {
            rs.setStatus(true);
            rs.setMsg("Annonce bien supprimée.");
        }
        return gson.toJson(rs);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listAnnonce(@QueryParam("page") Integer page) {
        ReturnClass rs = new ReturnClass("list_annonce", false, null, null);
        ArrayList<AnnonceDTO> myList = (ArrayList<AnnonceDTO>) annonceDAO.getListAnnonce(page);
        if (myList != null && !myList.isEmpty()) {
            rs.setStatus(true);
            rs.setMsg(gson.toJson(myList));
        }
        return gson.toJson(rs);
    }

    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    public String getnbannonce() {
        ReturnClass rs = new ReturnClass("getnbannonce", true, String.valueOf(annonceDAO.getNbAnnonce()), null);
        return gson.toJson(rs);
    }

    @DELETE
    @Path("{idAnnonce}/photos")
    @Produces(MediaType.APPLICATION_JSON)
    public String doDeletePhoto(@PathParam("idAnnonce") Integer idAnnonce,
                                @QueryParam("idPhoto") Integer idPhoto) {
        ReturnClass rs = new ReturnClass("doDeletePhoto", false, null, null);

        // Vérification que l'annonce existe bien
        if (annonceDAO.get(idAnnonce) != null) {
            boolean delete;
            if (idPhoto != null) {
                delete = photoDAO.deleteByIdAnnonce(idAnnonce); // Suppression de toutes les photos de l'annonce
            } else {
                delete = photoDAO.delete(idPhoto); // Suppression d'une photo en particulier
            }
            if (delete) {
                rs.setStatus(true);
            } else {
                rs.setMsg("Les photos de l'annonce n'ont pas pu être supprimées.");
            }
        }
        return gson.toJson(rs);
    }

    @GET
    @Path("/dosearchmultiparam")
    @Produces(MediaType.APPLICATION_JSON)
    public String doSearchMultiparam(@QueryParam("idCat") Integer idCat,
                                     @QueryParam("minPrice") Integer minPrice,
                                     @QueryParam("maxPrice") Integer maxPrice,
                                     @QueryParam("keyword") String keyword,
                                     @QueryParam("photo") boolean photo,
                                     @QueryParam("page") Integer page) {

        ReturnClass rs = new ReturnClass("doSearchMultiparam", false, null, null);
        ArrayList<AnnonceDTO> myList = (ArrayList<AnnonceDTO>) annonceDAO.getMultiParam(idCat, keyword, minPrice, maxPrice, photo, page);
        if (myList != null) {
            rs.setStatus(true);
            if (!myList.isEmpty()) {
                rs.setMsg(gson.toJson(myList));
            }
        }
        return gson.toJson(rs);
    }
}