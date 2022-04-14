public class Tablou extends Produs {
    String numePictor;
    Culori culoare;

    public Tablou(int id , String nume, double pretVanzare, double pretMinim,
                  int an, String numePictor, Culori culoare) {
        this.id = id;
        this.nume = nume;
        this.pretVanzare = pretVanzare;
        this.pretMinim = pretMinim;
        this.an = an;
        this.numePictor = numePictor;
        this.culoare = culoare;
    }
}