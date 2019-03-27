package dhbwka.wwi.vertsys.javee.filmsortierung.filme.ejb;

import dhbwka.wwi.vertsys.javaee.filmsortierung.common.ejb.EntityBean;
import dhbwka.wwi.vertsys.javaee.filmsortierung.tasks.jpa.Category;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;

/**
 * Einfache EJB mit den üblichen CRUD-Methoden für Kategorien.
 */
@Stateless
@RolesAllowed("app-user")
public class GenreBean extends EntityBean<Category, Long> {

    public GenreBean() {
        super(Category.class);
    }

    /**
     * Auslesen aller Kategorien, alphabetisch sortiert.
     *
     * @return Liste mit allen Kategorien
     */
    public List<Category> findAllSorted() {
        return this.em.createQuery("SELECT c FROM Category c ORDER BY c.name").getResultList();
    }
}
