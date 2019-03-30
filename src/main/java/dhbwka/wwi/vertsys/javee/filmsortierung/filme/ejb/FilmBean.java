package dhbwka.wwi.vertsys.javee.filmsortierung.filme.ejb;

import dhbwka.wwi.vertsys.javaee.filmsortierung.common.ejb.EntityBean;
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.jpa.Film;
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.jpa.FilmStatus;
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.jpa.Genre;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Einfache EJB mit den üblichen CRUD-Methoden für Aufgaben
 */
@Stateless
@RolesAllowed("app-user")
public class FilmBean extends EntityBean<Film, Long> {

    public FilmBean() {
        super(Film.class);
    }

    /**
     * Alle Aufgaben eines Benutzers, nach Fälligkeit sortiert zurückliefern.
     *
     * @param username Benutzername
     * @return Alle Aufgaben des Benutzers
     */
    public List<Film> findByUsername(String username) {
        return em.createQuery("SELECT t FROM Film t WHERE t.owner.username = :username ORDER BY t.dueDate, t.dueTime")
                .setParameter("username", username)
                .getResultList();
    }

    /**
     * Suche nach Aufgaben anhand ihrer Bezeichnung, Kategorie und Status.
     *
     * Anders als in der Vorlesung behandelt, wird die SELECT-Anfrage hier mit
     * der CriteriaBuilder-API vollkommen dynamisch erzeugt.
     *
     * @param search In der Kurzbeschreibung enthaltener Text (optional)
     * @param genre Kategorie (optional)
     * @param status Status (optional)
     * @return Liste mit den gefundenen Aufgaben
     */
    public List<Film> search(String search, Genre genre, FilmStatus status) {
        // Hilfsobjekt zum Bauen des Query
        CriteriaBuilder cb = this.em.getCriteriaBuilder();

        // SELECT t FROM Film t
        CriteriaQuery<Film> query = cb.createQuery(Film.class);
        Root<Film> from = query.from(Film.class);
        query.select(from);

        // ORDER BY dueDate, dueTime
        query.orderBy(cb.asc(from.get("dueDate")), cb.asc(from.get("dueTime")));

        // WHERE t.shortText LIKE :search
        Predicate p = cb.conjunction();

        if (search != null && !search.trim().isEmpty()) {
            p = cb.and(p, cb.like(from.get("name"), "%" + search + "%"));
            query.where(p);
        }

        // WHERE t.genre = :genre
        if (genre != null) {
            p = cb.and(p, cb.equal(from.get("genre"), genre));
            query.where(p);
        }

        // WHERE t.status = :status
        if (status != null) {
            p = cb.and(p, cb.equal(from.get("status"), status));
            query.where(p);
        }

        return em.createQuery(query).getResultList();
    }
}
