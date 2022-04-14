
public class Currency {
    String name;
    String symbol;
    double parityToEur;

    void updateParity(double parityToEur) {
        this.parityToEur = parityToEur;
    }

    public Currency() {
    }

    public Currency(String name, String symbol, double parityToEur) {
        this.name = name;
        this.symbol = symbol;
        this.parityToEur = parityToEur;
    }
}
