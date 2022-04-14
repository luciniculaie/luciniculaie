public class Bijuterie extends Produs {
    String material;
    boolean piatraPretioasa;

    public Bijuterie(int id, String nume, double pretVanzare, double pretMinim,
                     int an, String material, boolean piatraPretioasa) {
        this.id = id;
        this.nume = nume;
        this.pretVanzare = pretVanzare;
        this.pretMinim = pretMinim;
        this.an = an;
        this.piatraPretioasa = piatraPretioasa;
        this.material = material;
    }
}
