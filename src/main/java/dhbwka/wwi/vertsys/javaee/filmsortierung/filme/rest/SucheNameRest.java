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
import dhbwka.wwi.vertsys.javaee.filmsortierung.common.ejb.UserBean;
import dhbwka.wwi.vertsys.javaee.filmsortierung.common.jpa.User;
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.ejb.FilmBean;
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.ejb.GenreBean;
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.jpa.Film;
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.jpa.Genre;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;

/**
 *
 * @author simon
 */
@Stateless
@Path("sucheName/{name}")
public class SucheNameRest {

    @EJB
    private GenreBean genreBean;

    @EJB
    private FilmBean filmBean;

    @EJB
    private UserBean userBean;

    @GET
    public String doGet(@HeaderParam("Authorization") String authorization, @PathParam("name") String name) {
        System.out.println("Test");
        if (authorization != null && authorization.toLowerCase().startsWith("basic")) {
            // Authorization: Basic base64credentials
            String base64Credentials = authorization.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            // credentials = username:password
            final String[] values = credentials.split(":", 2);

            // Error: 0 = kein User; 2 = falsches Passwort
            List<User> user = userBean.findUser(values[0]);
            if (user.size() < 1) {
                return "0";
            }
            if (user.get(0).checkPassword(values[1])) {
                String[] param = name.split(":");
                if ("film".equals(param[0])) {
                    List<Film> genre = this.filmBean.findAllByName(param[1]);
                    Gson gson = new Gson();
                    String json = gson.toJson(genre);
                    return json;
                } else if ("genre".equals(param[0])) {
                    List<Genre> genre = this.genreBean.findAllByName(param[1]);
                    Gson gson = new Gson();
                    String json = gson.toJson(genre);
                    return json;
                }
            } else {
                return "2";
            }
        }
        return null;

    }
}
