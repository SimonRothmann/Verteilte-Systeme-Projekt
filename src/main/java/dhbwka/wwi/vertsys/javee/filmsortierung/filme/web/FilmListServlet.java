package dhbwka.wwi.vertsys.javee.filmsortierung.filme.web;

/**
 *
 * @author simon
 */
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.ejb.FilmBean;
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.ejb.GenreBean;
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.jpa.Film;
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.jpa.FilmStatus;
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.jpa.Genre;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet für die tabellarische Auflisten der Filme.
 */
@WebServlet(urlPatterns = {"/app/films/list/"})
public class FilmListServlet extends HttpServlet {

    @EJB
    private GenreBean genreBean;

    @EJB
    private FilmBean filmBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verfügbare Kategorien und Stati für die Suchfelder ermitteln
        request.setAttribute("genres", this.genreBean.findAllSorted());
        request.setAttribute("statuses", FilmStatus.values());

        // Suchparameter aus der URL auslesen
        String searchText = request.getParameter("search_text");
        String searchGenre = request.getParameter("search_genre");
        String searchStatus = request.getParameter("search_status");

        // Anzuzeigende Filme suchen
        Genre genre = null;
        FilmStatus status = null;

        if (searchGenre != null) {
            try {
                genre = this.genreBean.findById(Long.parseLong(searchGenre));
            } catch (NumberFormatException ex) {
                genre = null;
            }
        }

        if (searchStatus != null) {
            try {
                status = FilmStatus.valueOf(searchStatus);
            } catch (IllegalArgumentException ex) {
                status = null;
            }

        }

        List<Film> films = this.filmBean.search(searchText, genre, status);
        request.setAttribute("films", films);

        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/films/film_list.jsp").forward(request, response);
    }
}
