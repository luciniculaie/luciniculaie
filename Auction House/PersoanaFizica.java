public class PersoanaFizica extends Client {
    String dataNastere;

    public PersoanaFizica(int id, String nume, String adresa, int nrParticipari,
                          int nrLicitatiiCastigate, String dataNastere) {
        this.dataNastere = dataNastere;
        this.id = id;
        this.nume = nume;
        this.adresa = adresa;
        this.nrParticipari = nrParticipari;
        this.nrLicitatiiCastigate = nrLicitatiiCastigate;
    }
}

