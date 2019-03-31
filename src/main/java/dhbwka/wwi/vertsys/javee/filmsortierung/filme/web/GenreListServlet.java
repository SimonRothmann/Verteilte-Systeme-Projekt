package dhbwka.wwi.vertsys.javee.filmsortierung.filme.web;

import dhbwka.wwi.vertsys.javaee.filmsortierung.common.ejb.ValidationBean;
import dhbwka.wwi.vertsys.javaee.filmsortierung.common.web.FormValues;
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.ejb.FilmBean;
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.ejb.GenreBean;
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.jpa.Film;
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.jpa.Genre;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Seite zum Anzeigen und Bearbeiten der Genre. Die Seite besitzt ein Formular,
 * mit dem ein neues Genre angelegt werden kann, sowie eine Liste, die zum
 * Löschen der Genre verwendet werden kann.
 */
@WebServlet(urlPatterns = {"/app/films/genre/"})
public class GenreListServlet extends HttpServlet {

    @EJB
    GenreBean genreBean;

    @EJB
    FilmBean filmBean;

    @EJB
    ValidationBean validationBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Alle vorhandenen Genre ermitteln
        request.setAttribute("genres", this.genreBean.findAllSorted());

        // Anfrage an dazugerhörige JSP weiterleiten
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/films/genre_list.jsp");
        dispatcher.forward(request, response);

        // Alte Formulardaten aus der Session entfernen
        HttpSession session = request.getSession();
        session.removeAttribute("genres_form");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Angeforderte Aktion ausführen
        String action = request.getParameter("action");

        if (action == null) {
            action = "";
        }

        switch (action) {
            case "create":
                this.createGenre(request, response);
                break;
            case "delete":
                this.deleteGenres(request, response);
                break;
        }
    }

    /**
     * Aufgerufen in doPost(): Neues Genre anlegen
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void createGenre(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Formulareingaben prüfen
        String name = request.getParameter("name");

        Genre genre = new Genre(name);
        List<String> errors = this.validationBean.validate(genre);

        // Neues Genre anlegen
        if (errors.isEmpty()) {
            this.genreBean.saveNew(genre);
        }

        // Browser auffordern, die Seite neuzuladen
        if (!errors.isEmpty()) {
            FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            formValues.setErrors(errors);

            HttpSession session = request.getSession();
            session.setAttribute("genres_form", formValues);
        }

        response.sendRedirect(request.getRequestURI());
    }

    /**
     * Aufgerufen in doPost(): Markierte Genre löschen
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void deleteGenres(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Markierte Genre IDs auslesen
        String[] genreIds = request.getParameterValues("genre");

        if (genreIds == null) {
            genreIds = new String[0];
        }

        // Genre löschen
        for (String genreId : genreIds) {
            // Zu löschende Genre ermitteln
            Genre genre;

            try {
                genre = this.genreBean.findById(Long.parseLong(genreId));
            } catch (NumberFormatException ex) {
                continue;
            }

            if (genre == null) {
                continue;
            }

            // Bei allen betroffenen Filmen, den Bezug zum Genre aufheben
            List<Film> films = genre.getFilms();

            if (films != null) {
                films.forEach((Film film) -> {
                    film.setGenre(null);
                    this.filmBean.update(film);
                });
            }

            // Und weg damit
            this.genreBean.delete(genre);
        }

        // Browser auffordern, die Seite neuzuladen
        response.sendRedirect(request.getRequestURI());
    }

}
