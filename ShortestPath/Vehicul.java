

/**
 * Tipul Vehicul.
 */
public abstract class Vehicul {
    private int cost;
    private int gabarit;

    /**
     * Instantiates a new Vehicul.
     */
    public Vehicul() {
    }

    /**
     * Instantiates a new Vehicul.
     *
     * @param gabarit gabaritul
     * @param cost    costul
     */
    public Vehicul(int gabarit, int cost) {
        this.gabarit = gabarit;
        this.cost = cost;
    }

    /**
     * Returneaza costul.
     *
     * @return cost
     */
    public int getCost() {
        return cost;
    }

    /**
     * Returneaza gabaritul.
     *
     * @return gabarit
     */
    public int getGabarit() {
        return gabarit;
    }
}
