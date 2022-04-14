public class Administrator extends Angajat {
    public Administrator() {
    }

    public Administrator(String nume) {
        this.nume = nume;
    }

    void adaugaProdus(Produs p) {
        CasaLicitatii.getInstance().produseActuale.add(p);
    }
}
