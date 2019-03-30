package dhbwka.wwi.vertsys.javee.filmsortierung.filme.jpa;

import dhbwka.wwi.vertsys.javaee.filmsortierung.common.jpa.User;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Eine zu erledigende Aufgabe.
 */
@Entity
public class Film implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "film_ids")
    @TableGenerator(name = "film_ids", initialValue = 0, allocationSize = 50)
    private long id;

    @ManyToOne
    @NotNull(message = "Der Film sollte einem Benutzer zugeordnet werden.")
    private User owner;

    @ManyToOne
    private Genre genre;

    @Column(length = 50)
    @NotNull(message = "Der Film-Titel darf nicht leer sein. Sonst weißt du ja nicht was du anschauen willst!")
    @Size(min = 1, max = 50, message = "Der Film-Titel muss ausgefüllt werden (1-50 Zeichen), sonst weißt du ja nicht was du schauen möchtest.")
    private String name;

    @Lob
    @NotNull
    private String longText;

    @NotNull(message = "Das Datum darf nicht leer sein.")
    private Date dueDate;

    @NotNull(message = "Die Uhrzeit darf nicht leer sein.")
    private Time dueTime;

    @Enumerated(EnumType.STRING)
    @NotNull
    private FilmStatus status = FilmStatus.OPEN;

    @NotNull(message = "Der Film hat eine gewisse Spieldauer!")
    private float runTime;

    //<editor-fold defaultstate="collapsed" desc="Konstruktoren">
    public Film() {
    }

    public Film(User owner, Genre genre, String name, String longText, Date dueDate, Time dueTime, float runTime) {
        this.owner = owner;
        this.genre = genre;
        this.name = name;
        this.longText = longText;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.runTime = runTime;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Setter und Getter">
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongText() {
        return longText;
    }

    public void setLongText(String longText) {
        this.longText = longText;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Time getDueTime() {
        return dueTime;
    }

    public void setDueTime(Time dueTime) {
        this.dueTime = dueTime;
    }

    public FilmStatus getStatus() {
        return status;
    }

    public void setStatus(FilmStatus status) {
        this.status = status;
    }

    public float getRunTime() {
        return runTime;
    }

    public void setRunTime(float runTime) {
        this.runTime = runTime;
    }
    //</editor-fold>

}
