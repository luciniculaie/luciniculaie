

/**
 * The type Ambuteiaj.
 */
public class Ambuteiaj {
    private int sursa;
    private int destinatie;
    private String type;
    private int cost;

    /**
     * Instantiates a new Ambuteiaj.
     */
    public Ambuteiaj() {
    }

    /**
     * Instantiates a new Ambuteiaj.
     *
     * @param sursa      sursa
     * @param destinatie destinatia
     * @param type       tipul ambuteiajului
     * @param cost       costul ambuteiajului
     */
    public Ambuteiaj(int sursa, int destinatie, String type, int cost) {
        this.sursa = sursa;
        this.destinatie = destinatie;
        this.type = type;
        this.cost = cost;
    }

    /**
     * Intoarce sursa.
     *
     * @return sursa
     */
    public int getSursa() {
        return sursa;
    }

    /**
     * Intoarce destinatia.
     *
     * @return destinatie
     */
    public int getDestinatie() {
        return destinatie;
    }

    /**
     * Returneaza costul.
     *
     * @return cost
     */
    public int getCost() {
        return cost;
    }
}
