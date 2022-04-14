public class BijuterieBuilder {
    int id;
    String nume;
    double pretVanzare;
    double pretMinim;
    int an;
    String material;
    boolean piatraPretioasa;

    public BijuterieBuilder() {
    }

    public BijuterieBuilder(int id, String nume, double pretVanzare, double pretMinim, int an,
                         String tip, String material) {
        this.id = id;
        this.nume = nume;
        this.pretVanzare = pretVanzare;
        this.pretMinim = pretMinim;
        this.an = an;
        this.material = material;
        this.piatraPretioasa = piatraPretioasa;
    }

    public BijuterieBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public BijuterieBuilder setNume(String nume) {
        this.nume = nume;
        return this;
    }

    public BijuterieBuilder setPretVanzare(double pretVanzare) {
        this.pretVanzare = pretVanzare;
        return this;
    }

    public BijuterieBuilder setPretMinim(double pretMinim) {
        this.pretMinim = pretMinim;
        return this;
    }

    public BijuterieBuilder setAn(int an) {
        this.an = an;
        return this;
    }

    public BijuterieBuilder setPiatraPretioasa(Boolean piatraPretioasa) {
        this.piatraPretioasa = piatraPretioasa;
        return this;
    }

    public BijuterieBuilder setMaterial(String material) {
        this.material = material;
        return this;
    }

    public Bijuterie build() {
        return new Bijuterie(id ,nume, pretVanzare, pretMinim, an, material, piatraPretioasa);
    }
}
