package dhbwka.wwi.vertsys.javee.filmsortierung.filme.web;

import dhbwka.wwi.vertsys.javaee.filmsortierung.common.ejb.UserBean;
import dhbwka.wwi.vertsys.javaee.filmsortierung.common.ejb.ValidationBean;
import dhbwka.wwi.vertsys.javaee.filmsortierung.common.web.FormValues;
import dhbwka.wwi.vertsys.javaee.filmsortierung.common.web.WebUtils;
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.ejb.FilmBean;
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.ejb.GenreBean;
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.jpa.Film;
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.jpa.FilmStatus;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Seite zum Anlegen oder Bearbeiten einer Aufgabe.
 */
@WebServlet(urlPatterns = "/app/films/film/*")
public class FilmEditServlet extends HttpServlet {

    @EJB
    FilmBean filmBean;

    @EJB
    GenreBean genreBean;

    @EJB
    UserBean userBean;

    @EJB
    ValidationBean validationBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verfügbare Kategorien und Stati für die Suchfelder ermitteln
        request.setAttribute("genres", this.genreBean.findAllSorted());
        request.setAttribute("statuses", FilmStatus.values());

        // Zu bearbeitende Aufgabe einlesen
        HttpSession session = request.getSession();

        Film film = this.getRequestedFilm(request);
        request.setAttribute("edit", film.getId() != 0);

        if (session.getAttribute("film_form") == null) {
            // Keine Formulardaten mit fehlerhaften Daten in der Session,
            // daher Formulardaten aus dem Datenbankobjekt übernehmen
            request.setAttribute("film_form", this.createFilmForm(film));
        }

        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/films/film_edit.jsp").forward(request, response);

        session.removeAttribute("film_form");
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
            case "save":
                this.saveFilm(request, response);
                break;
            case "delete":
                this.deleteFilm(request, response);
                break;
        }
    }

    /**
     * Aufgerufen in doPost(): Neue oder vorhandene Aufgabe speichern
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void saveFilm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Formulareingaben prüfen
        List<String> errors = new ArrayList<>();

        String filmGenre = request.getParameter("film_genre");
        String filmDueDate = request.getParameter("film_due_date");
        String filmDueTime = request.getParameter("film_due_time");
        String filmStatus = request.getParameter("film_status");
        String filmShortText = request.getParameter("film_short_text");
        String filmLongText = request.getParameter("film_long_text");

        Film film = this.getRequestedFilm(request);

        if (filmGenre != null && !filmGenre.trim().isEmpty()) {
            try {
                film.setGenre(this.genreBean.findById(Long.parseLong(filmGenre)));
            } catch (NumberFormatException ex) {
                // Ungültige oder keine ID mitgegeben
            }
        }

        Date dueDate = WebUtils.parseDate(filmDueDate);
        Time dueTime = WebUtils.parseTime(filmDueTime);

        if (dueDate != null) {
            film.setDueDate(dueDate);
        } else {
            errors.add("Du musst ein Datum eintragen, dass dem Format dd.mm.yyyy entspricht.");
        }

        if (dueTime != null) {
            film.setDueTime(dueTime);
        } else {
            errors.add("Die Uhrzeit muss das Format hh:mm:ss haben.");
        }

        try {
            film.setStatus(FilmStatus.valueOf(filmStatus));
        } catch (IllegalArgumentException ex) {
            errors.add("Der ausgewählte Status ist nicht vorhanden.");
        }

        film.setShortText(filmShortText);
        film.setLongText(filmLongText);

        this.validationBean.validate(film, errors);

        // Datensatz speichern
        if (errors.isEmpty()) {
            this.filmBean.update(film);
        }

        // Weiter zur nächsten Seite
        if (errors.isEmpty()) {
            // Keine Fehler: Startseite aufrufen
            response.sendRedirect(WebUtils.appUrl(request, "/app/films/list/"));
        } else {
            // Fehler: Formuler erneut anzeigen
            FormValues formValues = new FormValues();
            formValues.setValues(request.getParameterMap());
            formValues.setErrors(errors);

            HttpSession session = request.getSession();
            session.setAttribute("film_form", formValues);

            response.sendRedirect(request.getRequestURI());
        }
    }

    /**
     * Aufgerufen in doPost: Vorhandene Aufgabe löschen
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    private void deleteFilm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Datensatz löschen
        Film film = this.getRequestedFilm(request);
        this.filmBean.delete(film);

        // Zurück zur Übersicht
        response.sendRedirect(WebUtils.appUrl(request, "/app/films/list/"));
    }

    /**
     * Zu bearbeitende Aufgabe aus der URL ermitteln und zurückgeben. Gibt
     * entweder einen vorhandenen Datensatz oder ein neues, leeres Objekt
     * zurück.
     *
     * @param request HTTP-Anfrage
     * @return Zu bearbeitende Aufgabe
     */
    private Film getRequestedFilm(HttpServletRequest request) {
        // Zunächst davon ausgehen, dass ein neuer Satz angelegt werden soll
        Film film = new Film();
        film.setOwner(this.userBean.getCurrentUser());
        film.setDueDate(new Date(System.currentTimeMillis()));
        film.setDueTime(new Time(System.currentTimeMillis()));

        // ID aus der URL herausschneiden
        String filmId = request.getPathInfo();

        if (filmId == null) {
            filmId = "";
        }

        filmId = filmId.substring(1);

        if (filmId.endsWith("/")) {
            filmId = filmId.substring(0, filmId.length() - 1);
        }

        // Versuchen, den Datensatz mit der übergebenen ID zu finden
        try {
            film = this.filmBean.findById(Long.parseLong(filmId));
        } catch (NumberFormatException ex) {
            // Ungültige oder keine ID in der URL enthalten
        }

        return film;
    }

    /**
     * Neues FormValues-Objekt erzeugen und mit den Daten eines aus der
     * Datenbank eingelesenen Datensatzes füllen. Dadurch müssen in der JSP
     * keine hässlichen Fallunterscheidungen gemacht werden, ob die Werte im
     * Formular aus der Entity oder aus einer vorherigen Formulareingabe
     * stammen.
     *
     * @param film Die zu bearbeitende Aufgabe
     * @return Neues, gefülltes FormValues-Objekt
     */
    private FormValues createFilmForm(Film film) {
        Map<String, String[]> values = new HashMap<>();

        values.put("film_owner", new String[]{
            film.getOwner().getUsername()
        });

        if (film.getGenre() != null) {
            values.put("film_genre", new String[]{
                "" + film.getGenre().getId()
            });
        }

        values.put("film_due_date", new String[]{
            WebUtils.formatDate(film.getDueDate())
        });

        values.put("film_due_time", new String[]{
            WebUtils.formatTime(film.getDueTime())
        });

        values.put("film_status", new String[]{
            film.getStatus().toString()
        });

        values.put("film_short_text", new String[]{
            film.getShortText()
        });

        values.put("film_long_text", new String[]{
            film.getLongText()
        });

        FormValues formValues = new FormValues();
        formValues.setValues(values);
        return formValues;
    }

}
