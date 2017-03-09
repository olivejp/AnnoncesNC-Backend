package com.oliweb.restservice;


import com.google.gson.Gson;
import com.oliweb.db.dao.AnnonceDAO;
import com.oliweb.db.dao.MyConnection;
import com.oliweb.db.dao.PhotoDAO;
import com.oliweb.db.dto.Photo;
import com.oliweb.dto.ReturnWS;
import com.oliweb.utility.Proprietes;
import org.apache.commons.io.FilenameUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/photos")
public class PhotoRestService {

    private static Gson gson = new Gson();
    private AnnonceDAO annonceDAO = new AnnonceDAO(MyConnection.getInstance());
    private PhotoDAO photoDAO = new PhotoDAO(MyConnection.getInstance());

    @GET
    @Path("{idPhoto}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getById(@PathParam("idPhoto") Integer idPhoto) {
        ReturnWS rs = new ReturnWS("getById", true, null, null);
        rs.setMsg(gson.toJson(photoDAO.get(idPhoto)));
        return gson.toJson(rs);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String post(@QueryParam("idAnnonce") Integer idAnnonce,
                       @QueryParam("idPhoto") Integer idPhoto,
                       @QueryParam("nomPhoto") String nomPhoto) {
        Photo photo;
        ReturnWS rs = new ReturnWS("post", false, null, null);
        String directoryUpload = Proprietes.getProperty(Proprietes.DIRECTORY_UPLOAD);

        // Récupération du filename qu'on vient de recevoir
        String newName = FilenameUtils.getName(nomPhoto);

        // Vérification que l'annonce existe bien
        if (annonceDAO.get(idAnnonce) == null) {
            rs.setMsg("L'annonce n°:" + String.valueOf(idAnnonce) + " n'existe pas");
            return gson.toJson(rs);
        }

        // Si la photo n'existe pas encore, je vais la créer
        photo = photoDAO.get(idPhoto);
        if (photo == null) {
            // La photo n'existe pas, il faut la créer
            photo = new Photo();
            photo.setNamePhoto(Proprietes.getProperty(Proprietes.DIRECTORY_UPLOAD) + newName);
            photo.setIdAnnoncePhoto(idAnnonce);
            if (photoDAO.save(photo)) {
                rs.setStatus(true);
                rs.setIdServer(photo.getIdPhoto());
                rs.setMsg(nomPhoto);
            } else {
                rs.setMsg("La photo n'a pas pu être créée dans la BD");
            }
            return gson.toJson(rs);
        }

        // La photo existe déjà, on va vérifier si elle a changé de nom ou pas
        String presentName = FilenameUtils.getName(photo.getNamePhoto());
        if (!presentName.equals(newName)) {
            photo.setNamePhoto(directoryUpload.concat(newName));
            if (photoDAO.update(photo)) {
                rs.setStatus(true);
                rs.setIdServer(photo.getIdPhoto());
                rs.setMsg(nomPhoto);
            } else {
                rs.setMsg("La photo n'a pas pu être mise à jour dans la BD");
            }
        } else {
            // La photo n'a pas changée, elle existe déjà
            rs.setStatus(true);
            rs.setMsg(Proprietes.getProperty(Proprietes.PHOTO_ALREADY_EXIST));
        }
        return gson.toJson(rs);
    }

    @DELETE
    @Path("{idPhoto}")
    @Produces(MediaType.APPLICATION_JSON)
    public String delete(@PathParam("idPhoto") Integer idPhoto) {
        ReturnWS rs = new ReturnWS("deletePhoto", false, null, null);
        if (photoDAO.get(idPhoto) != null && photoDAO.delete(idPhoto)) {
            rs.setStatus(true);
            rs.setMsg("Photo bien supprimée.");
        }
        return gson.toJson(rs);
    }

    @GET
    @Path("/photoExist")
    @Produces(MediaType.APPLICATION_JSON)
    public String photoExist(@QueryParam("idAnnonce") Integer idAnnonce,
                             @QueryParam("idPhoto") Integer idPhoto,
                             @QueryParam("namePhoto") String namePhoto) {
        ReturnWS rs = new ReturnWS("photoexist", false, null, null);
        if (photoDAO.existByAnnonceAndName(idAnnonce, idPhoto, namePhoto)) {
            rs.setStatus(true);
            rs.setMsg(Proprietes.getProperty(Proprietes.SAME_NAME));
        } else {
            if (photoDAO.existWithDifferentName(idAnnonce, idPhoto, namePhoto)) {
                rs.setStatus(true);
                rs.setMsg(Proprietes.getProperty(Proprietes.DIFFRENT_NAME));
            }
        }
        return gson.toJson(rs);
    }
}
