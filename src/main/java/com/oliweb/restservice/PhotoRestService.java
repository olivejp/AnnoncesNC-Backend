package com.oliweb.restservice;


import com.google.gson.Gson;
import com.oliweb.db.dao.AnnonceDAO;
import com.oliweb.db.dao.MyConnection;
import com.oliweb.db.dao.PhotoDAO;
import com.oliweb.db.dto.PhotoDTO;
import com.oliweb.utility.Proprietes;
import org.apache.commons.io.FilenameUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/photos")
public class PhotoRestService {

    private static Gson gson = new Gson();
    private AnnonceDAO annonceDAO = new AnnonceDAO(MyConnection.getInstance());
    private PhotoDAO photoDAO = new PhotoDAO(MyConnection.getInstance());

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String post(@QueryParam("idAnnonce") Integer idAnnonce,
                       @QueryParam("idPhoto") Integer idPhoto,
                       @QueryParam("nomPhoto") String nomPhoto) {
        PhotoDTO photoDTO;
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
        photoDTO = photoDAO.get(idPhoto);
        if (photoDTO == null) {
            // La photo n'existe pas, il faut la créer
            photoDTO = new PhotoDTO();
            photoDTO.setNamePhoto(Proprietes.getProperty(Proprietes.DIRECTORY_UPLOAD) + newName);
            photoDTO.setIdAnnoncePhoto(idAnnonce);
            if (photoDAO.save(photoDTO)) {
                rs.setStatus(true);
                rs.setId(photoDTO.getIdPhoto());
                rs.setMsg(nomPhoto);
            } else {
                rs.setMsg("La photo n'a pas pu être créée dans la BD");
            }
            return gson.toJson(rs);
        }

        // La photo existe déjà, on va vérifier si elle a changé de nom ou pas
        String presentName = FilenameUtils.getName(photoDTO.getNamePhoto());
        if (!presentName.equals(newName)) {
            photoDTO.setNamePhoto(directoryUpload.concat(newName));
            if (photoDAO.update(photoDTO)) {
                rs.setStatus(true);
                rs.setId(photoDTO.getIdPhoto());
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
