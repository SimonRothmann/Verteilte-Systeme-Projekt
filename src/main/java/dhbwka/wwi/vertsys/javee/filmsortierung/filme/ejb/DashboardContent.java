package dhbwka.wwi.vertsys.javee.filmsortierung.filme.ejb;

import dhbwka.wwi.vertsys.javaee.filmsortierung.common.web.WebUtils;
import dhbwka.wwi.vertsys.javaee.filmsortierung.dashboard.ejb.DashboardContentProvider;
import dhbwka.wwi.vertsys.javaee.filmsortierung.dashboard.ejb.DashboardSection;
import dhbwka.wwi.vertsys.javaee.filmsortierung.dashboard.ejb.DashboardTile;
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.jpa.Genre;
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.jpa.TaskStatus;
import static dhbwka.wwi.vertsys.javee.filmsortierung.filme.jpa.TaskStatus.*;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * EJB zur Definition der Dashboard-Kacheln für Aufgaben.
 */
@Stateless(name = "films")
public class DashboardContent implements DashboardContentProvider {

    @EJB
    private GenreBean genreBean;

    @EJB
    private FilmBean filmBean;

    /**
     * Vom Dashboard aufgerufenen Methode, um die anzuzeigenden Rubriken und
     * Kacheln zu ermitteln.
     *
     * @param sections Liste der Dashboard-Rubriken, an die die neuen Rubriken
     * angehängt werden müssen
     */
    @Override
    public void createDashboardContent(List<DashboardSection> sections) {
        // Zunächst einen Abschnitt mit einer Gesamtübersicht aller Aufgaben
        // in allen Kategorien erzeugen
        DashboardSection section = this.createSection(null);
        sections.add(section);

        // Anschließend je Kategorie einen weiteren Abschnitt erzeugen
        List<Genre> genres = this.genreBean.findAllSorted();

        for (Genre genre : genres) {
            section = this.createSection(genre);
            sections.add(section);
        }
    }

    /**
     * Hilfsmethode, die für die übergebene Aufgaben-Kategorie eine neue Rubrik
     * mit Kacheln im Dashboard erzeugt. Je Aufgabenstatus wird eine Kachel
     * erzeugt. Zusätzlich eine Kachel für alle Aufgaben innerhalb der
     * jeweiligen Kategorie.
     *
     * Ist die Kategorie null, bedeutet dass, dass eine Rubrik für alle Aufgaben
     * aus allen Kategorien erzeugt werden soll.
     *
     * @param genre Aufgaben-Kategorie, für die Kacheln erzeugt werden sollen
     * @return Neue Dashboard-Rubrik mit den Kacheln
     */
    private DashboardSection createSection(Genre genre) {
        // Neue Rubrik im Dashboard erzeugen
        DashboardSection section = new DashboardSection();
        String cssClass = "";

        if (genre != null) {
            section.setLabel(genre.getName());
        } else {
            section.setLabel("Alle Kategorien");
            cssClass = "overview";
        }

        // Eine Kachel für alle Aufgaben in dieser Rubrik erzeugen
        DashboardTile tile = this.createTile(genre, null, "Alle", cssClass + " status-all", "calendar");
        section.getTiles().add(tile);

        // Ja Aufgabenstatus eine weitere Kachel erzeugen
        for (TaskStatus status : TaskStatus.values()) {
            String cssClass1 = cssClass + " status-" + status.toString().toLowerCase();
            String icon = "";

            switch (status) {
                case OPEN:
                    icon = "doc-text";
                    break;
                case IN_PROGRESS:
                    icon = "rocket";
                    break;
                case FINISHED:
                    icon = "ok";
                    break;
                case CANCELED:
                    icon = "cancel";
                    break;
                case POSTPONED:
                    icon = "bell-off-empty";
                    break;
            }

            tile = this.createTile(genre, status, status.getLabel(), cssClass1, icon);
            section.getTiles().add(tile);
        }

        // Erzeugte Dashboard-Rubrik mit den Kacheln zurückliefern
        return section;
    }

    /**
     * Hilfsmethode zum Erzeugen einer einzelnen Dashboard-Kachel. In dieser
     * Methode werden auch die in der Kachel angezeigte Anzahl sowie der Link,
     * auf den die Kachel zeigt, ermittelt.
     *
     * @param genre
     * @param status
     * @param label
     * @param cssClass
     * @param icon
     * @return
     */
    private DashboardTile createTile(Genre genre, TaskStatus status, String label, String cssClass, String icon) {
        int amount = filmBean.search(null, genre, status).size();
        String href = "/app/films/list/";

        if (genre != null) {
            href = WebUtils.addQueryParameter(href, "search_genre", "" + genre.getId());
        }

        if (status != null) {
            href = WebUtils.addQueryParameter(href, "search_status", status.toString());
        }

        DashboardTile tile = new DashboardTile();
        tile.setLabel(label);
        tile.setCssClass(cssClass);
        tile.setHref(href);
        tile.setIcon(icon);
        tile.setAmount(amount);
        tile.setShowDecimals(false);
        return tile;
    }

}
