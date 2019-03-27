package dhbwka.wwi.vertsys.javee.filmsortierung.filme.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Kategorien, die den Aufgaben zugeordnet werden können.
 */
@Entity
public class Genre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "genre_ids")
    @TableGenerator(name = "genre_ids", initialValue = 0, allocationSize = 50)
    private long id;

    @Column(length = 30)
    @NotNull(message = "Der Name darf nicht leer sein.")
    @Size(min = 3, max = 30, message = "Wessen Name kürzer als 3 Zeichen ist, darf sich hier nicht anmelden ;)")
    private String name;

    @OneToMany(mappedBy = "genre", fetch = FetchType.LAZY)
    List<Film> films = new ArrayList<>();

    //<editor-fold defaultstate="collapsed" desc="Konstruktoren">
    public Genre() {
    }

    public Genre(String name) {
        this.name = name;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Setter und Getter">
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Film> getFilms() {
        return films;
    }

    public void setFilms(List<Film> films) {
        this.films = films;
    }
    //</editor-fold>

}
