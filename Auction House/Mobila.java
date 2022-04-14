public class Mobila extends Produs {
    String tip;
    String material;

    public Mobila(int id , String nume, double pretVanzare, double pretMinim,
                  int an, String tip, String material) {
        this.id = id;
        this.nume = nume;
        this.pretVanzare = pretVanzare;
        this.pretMinim = pretMinim;
        this.an = an;
        this.tip = tip;
        this.material = material;
    }
}
