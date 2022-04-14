

public class Manufacturer {
    String name;
    int countProducts;

    public Manufacturer(String name) {
        this.name = name;
        countProducts = 0;
    }

    void increaseCountProducts() {
        countProducts++;
    }

    public Manufacturer() {
    }
}
