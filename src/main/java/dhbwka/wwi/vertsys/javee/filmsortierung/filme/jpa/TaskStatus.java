package dhbwka.wwi.vertsys.javee.filmsortierung.filme.jpa;

public enum TaskStatus {
    OPEN, IN_PROGRESS, FINISHED, CANCELED, POSTPONED;

    /**
     * Bezeichnung ermitteln
     *
     * @return Bezeichnung
     */
    public String getLabel() {
        switch (this) {
            case OPEN:
                return "Offen";
            case IN_PROGRESS:
                return "In Bearbeitung";
            case FINISHED:
                return "Erledigt";
            case CANCELED:
                return "Abgebrochen";
            case POSTPONED:
                return "Zurückgestellt";
            default:
                return this.toString();
        }
    }

}
