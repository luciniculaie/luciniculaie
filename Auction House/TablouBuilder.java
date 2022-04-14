public class TablouBuilder {
    int id;
    String nume;
    double pretVanzare;
    double pretMinim;
    int an;
    String numePictor;
    Culori culoare;

    public TablouBuilder() {
    }

    public TablouBuilder(int id, String nume, double pretVanzare, double pretMinim, int an,
                         String numePictor, Culori culoare) {
        this.id = id;
        this.nume = nume;
        this.pretVanzare = pretVanzare;
        this.pretMinim = pretMinim;
        this.an = an;
        this.numePictor = numePictor;
        this.culoare = culoare;
    }

    public TablouBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public TablouBuilder setNume(String nume) {
        this.nume = nume;
        return this;
    }

    public TablouBuilder setPretVanzare(double pretVanzare) {
        this.pretVanzare = pretVanzare;
        return this;
    }

    public TablouBuilder setPretMinim(double pretMinim) {
        this.pretMinim = pretMinim;
        return this;
    }

    public TablouBuilder setAn(int an) {
        this.an = an;
        return this;
    }

    public TablouBuilder setNumePictor(String numePictor) {
        this.numePictor = numePictor;
        return this;
    }

    public TablouBuilder setCuloare(Culori culoare) {
        this.culoare = culoare;
        return this;
    }

    public Tablou build() {
        return new Tablou(id ,nume, pretVanzare, pretMinim, an, numePictor, culoare);
    }
}
