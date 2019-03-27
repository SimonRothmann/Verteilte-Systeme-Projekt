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
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.ejb.FilmBean;
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.jpa.Film;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 * @author vibach
 */
@Stateless
@Path("sucheFilm")

public class SucheFilmRest {

    @EJB
    private FilmBean filmBean;

    @GET
    public String doGet() {
        List<Film> filme = this.filmBean.findAll();
        Gson gson = new Gson();
        String json = gson.toJson(filme);
        return json;
    }

}
