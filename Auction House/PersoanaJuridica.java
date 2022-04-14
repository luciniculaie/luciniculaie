public class PersoanaJuridica extends Client {

    Companie companie;
    double capitalSocial;

    public static enum Companie  {
        SRL, SA
    }

    public PersoanaJuridica(int id, String nume, String adresa, int nrParticipari,
                            int nrLicitatiiCastigate, Companie companie, double capitalSocial) {
        this.companie = companie;
        this.capitalSocial = capitalSocial;
        this.id = id;
        this.nume = nume;
        this.adresa = adresa;
        this.nrParticipari = nrParticipari;
        this.nrLicitatiiCastigate = nrLicitatiiCastigate;
    }
}
