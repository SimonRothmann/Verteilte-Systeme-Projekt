package dhbwka.wwi.vertsys.javee.filmsortierung.filme.ejb;

import dhbwka.wwi.vertsys.javaee.filmsortierung.common.ejb.EntityBean;
import dhbwka.wwi.vertsys.javee.filmsortierung.filme.jpa.Genre;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;

/**
 * Einfache EJB mit den üblichen CRUD-Methoden für Kategorien.
 */
@Stateless
@RolesAllowed("app-user")
public class GenreBean extends EntityBean<Genre, Long> {

    public GenreBean() {
        super(Genre.class);
    }

    /**
     * Auslesen aller Kategorien, alphabetisch sortiert.
     *
     * @return Liste mit allen Kategorien
     */
    public List<Genre> findAllSorted() {
        return this.em.createQuery("SELECT c FROM Genre c ORDER BY c.name").getResultList();
    }
}
