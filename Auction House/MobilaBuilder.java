public class MobilaBuilder {
    int id;
    String nume;
    double pretVanzare;
    double pretMinim;
    int an;
    String tip;
    String material;

    public MobilaBuilder() {
    }

    public MobilaBuilder(int id, String nume, double pretVanzare, double pretMinim, int an,
                         String tip, String material) {
        this.id = id;
        this.nume = nume;
        this.pretVanzare = pretVanzare;
        this.pretMinim = pretMinim;
        this.an = an;
        this.tip = tip;
        this.material = material;
    }

    public MobilaBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public MobilaBuilder setNume(String nume) {
        this.nume = nume;
        return this;
    }

    public MobilaBuilder setPretVanzare(double pretVanzare) {
        this.pretVanzare = pretVanzare;
        return this;
    }

    public MobilaBuilder setPretMinim(double pretMinim) {
        this.pretMinim = pretMinim;
        return this;
    }

    public MobilaBuilder setAn(int an) {
        this.an = an;
        return this;
    }

    public MobilaBuilder setTip(String tip) {
        this.tip = tip;
        return this;
    }

    public MobilaBuilder setMaterial(String material) {
        this.material = material;
        return this;
    }

    public Mobila build() {
        return new Mobila(id ,nume, pretVanzare, pretMinim, an, tip, material);
    }
}
