package dhbwka.wwi.vertsys.javee.filmsortierung.filme.jpa;

public enum FilmStatus {
    OPEN, IN_PROGRESS, FINISHED, CANCELED, POSTPONED;

    /**
     * Bezeichnung ermitteln
     *
     * @return Bezeichnung
     */
    public String getLabel() {
        switch (this) {
            case OPEN:
                return "Noch nicht gesehen";
            case IN_PROGRESS:
                return "Am schauen";
            case FINISHED:
                return "Gesehen";
            case CANCELED:
                return "Interessiert mich nicht";
            case POSTPONED:
                return "Möchte ich noch sehen";
            default:
                return this.toString();
        }
    }

}
