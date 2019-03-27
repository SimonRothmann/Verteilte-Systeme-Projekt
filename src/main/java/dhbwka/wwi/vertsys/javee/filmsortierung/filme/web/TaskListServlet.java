package dhbwka.wwi.vertsys.javee.filmsortierung.filme.web;

/**
 *
 * @author simon
 */
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.ejb.GenreBean;
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.ejb.TaskBean;
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.jpa.Genre;
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.jpa.Task;
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.jpa.TaskStatus;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet für die tabellarische Auflisten der Aufgaben.
 */
@WebServlet(urlPatterns = {"/app/tasks/list/"})
public class TaskListServlet extends HttpServlet {

    @EJB
    private GenreBean genreBean;

    @EJB
    private TaskBean taskBean;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verfügbare Kategorien und Stati für die Suchfelder ermitteln
        request.setAttribute("genres", this.genreBean.findAllSorted());
        request.setAttribute("statuses", TaskStatus.values());

        // Suchparameter aus der URL auslesen
        String searchText = request.getParameter("search_text");
        String searchGenre = request.getParameter("search_genre");
        String searchStatus = request.getParameter("search_status");

        // Anzuzeigende Aufgaben suchen
        Genre genre = null;
        TaskStatus status = null;

        if (searchGenre != null) {
            try {
                genre = this.genreBean.findById(Long.parseLong(searchGenre));
            } catch (NumberFormatException ex) {
                genre = null;
            }
        }

        if (searchStatus != null) {
            try {
                status = TaskStatus.valueOf(searchStatus);
            } catch (IllegalArgumentException ex) {
                status = null;
            }

        }

        List<Task> tasks = this.taskBean.search(searchText, genre, status);
        request.setAttribute("tasks", tasks);

        // Anfrage an die JSP weiterleiten
        request.getRequestDispatcher("/WEB-INF/tasks/task_list.jsp").forward(request, response);
    }
}
