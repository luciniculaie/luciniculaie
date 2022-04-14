public abstract class Produs {
    int id;
    String nume;
    double pretVanzare;
    double pretMinim;
    int an;

    public Produs() {
    }

    void printDetalii() {
        System.out.println("ID: " + id + " | Nume: " + nume + " | " + "PretMinim: " + pretMinim + " | An: " +an);
    }

}
