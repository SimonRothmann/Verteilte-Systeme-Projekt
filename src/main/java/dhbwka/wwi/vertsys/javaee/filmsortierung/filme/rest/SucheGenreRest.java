/*
 * Copyright © 2019 Dennis Schulmeister-Zimolong
 *
 * E-Mail: dhbw@windows3.de
 * Webseite: https://www.wpvs.de/
 *
 * Dieser Quellcode ist lizenziert unter einer
 * Creative Commons Namensnennung 4.0 International Lizenz.
 */
package dhbwka.wwi.vertsys.javaee.filmsortierung.filme.rest;

import com.google.gson.Gson;
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.ejb.GenreBean;
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.jpa.Genre;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;

/**
 *
 * @author Julia Becker
 */
@Stateless
@Path("sucheGenre")
public class SucheGenreRest {

    @EJB
    private GenreBean genreBean;

    @GET
    public String doGet() {
        List<Genre> genre = this.genreBean.findAll();
        Gson gson = new Gson();
        String json = gson.toJson(genre);
        return json;
    }
}
