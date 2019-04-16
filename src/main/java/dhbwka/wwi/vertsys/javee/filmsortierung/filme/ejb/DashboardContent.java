package dhbwka.wwi.vertsys.javee.filmsortierung.filme.ejb;

import dhbwka.wwi.vertsys.javaee.filmsortierung.common.web.WebUtils;
import dhbwka.wwi.vertsys.javaee.filmsortierung.dashboard.ejb.DashboardContentProvider;
import dhbwka.wwi.vertsys.javaee.filmsortierung.dashboard.ejb.DashboardSection;
import dhbwka.wwi.vertsys.javaee.filmsortierung.dashboard.ejb.DashboardTile;
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.jpa.FilmStatus;
import static dhbwka.wwi.vertsys.javee.filmsortierung.filme.jpa.FilmStatus.*;
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.jpa.Genre;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * EJB zur Definition der Dashboard-Kacheln für Filme.
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
        // Zunächst einen Abschnitt mit einer Gesamtübersicht aller Filme
        // in allen Genre erzeugen
        DashboardSection section = this.createSection(null);
        sections.add(section);

        // Anschließend je Genre einen weiteren Abschnitt erzeugen
        List<Genre> genres = this.genreBean.findAllSorted();

        for (Genre genre : genres) {
            section = this.createSection(genre);
            sections.add(section);
        }
    }

    /**
     * Hilfsmethode, die für die übergebene Genre eine neue Rubrik mit Kacheln
     * im Dashboard erzeugt. Je Filmstatus wird eine Kachel erzeugt. Zusätzlich
     * eine Kachel für alle Filme innerhalb des jeweiligen Genre.
     *
     * Ist das Genre null, bedeutet dass, dass eine Rubrik für alle Filme aus
     * allen Genre erzeugt werden soll.
     *
     * @param genre Film-Genre, für die Kacheln erzeugt werden sollen
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

        // Eine Kachel für alle Filme in dieser Rubrik erzeugen
        DashboardTile tile = this.createTile(genre, null, "Alle", cssClass + " status-all", "calendar");
        section.getTiles().add(tile);

        // Ja Filmstatus eine weitere Kachel erzeugen
        for (FilmStatus status : FilmStatus.values()) {
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
    private DashboardTile createTile(Genre genre, FilmStatus status, String label, String cssClass, String icon) {
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
