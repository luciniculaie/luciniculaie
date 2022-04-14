public class PersoanaJuridicaBuilder {
    int id;
    String nume;
    String adresa;
    int nrParticipari = 0;
    int nrLicitatiiCastigate = 0;
    PersoanaJuridica.Companie companie;
    double capitalSocial;

    public static enum Companie {
        SRL, SA
    }

    public PersoanaJuridicaBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public PersoanaJuridicaBuilder setNume(String nume) {
        this.nume = nume;
        return this;
    }

    public PersoanaJuridicaBuilder setAdresa(String adresa) {
        this.adresa = adresa;
        return this;
    }

    public PersoanaJuridicaBuilder setNrParticipari(int nrParticipari) {
        this.nrParticipari = nrParticipari;
        return this;
    }

    public PersoanaJuridicaBuilder setNrLicitatiiCastigate(int nrLicitatiiCastigate) {
        this.nrLicitatiiCastigate = nrLicitatiiCastigate;
        return this;
    }


    public PersoanaJuridicaBuilder setCompanie(PersoanaJuridica.Companie companie) {
        this.companie = companie;
        return this;
    }
    public PersoanaJuridicaBuilder setCapitalSocial(double capitalSocial) {
        this.capitalSocial = capitalSocial;
        return this;
    }

    public PersoanaJuridica build() {
        return new PersoanaJuridica(id, nume, adresa, nrParticipari, nrLicitatiiCastigate, companie, capitalSocial);
    }
}
