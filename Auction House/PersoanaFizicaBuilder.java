public class PersoanaFizicaBuilder {
    int id;
    String nume;
    String adresa;
    int nrParticipari = 0;
    int nrLicitatiiCastigate = 0;
    String dataNastere;

    public PersoanaFizicaBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public PersoanaFizicaBuilder setNume(String nume) {
        this.nume= nume;
        return this;
    }

    public PersoanaFizicaBuilder setAdresa(String adresa) {
        this.adresa = adresa;
        return this;
    }

    public PersoanaFizicaBuilder setNrParticipari(int nrParticipari) {
        this.nrParticipari = nrParticipari;
        return this;
    }

    public PersoanaFizicaBuilder setNrLicitatiiCastigate(int nrLicitatiiCastigate) {
        this.nrLicitatiiCastigate = nrLicitatiiCastigate;
        return this;
    }

    public PersoanaFizicaBuilder setDataNastere(String dataNastere) {
        this.dataNastere = dataNastere;
        return this;
    }

    public PersoanaFizica build() {
        return new PersoanaFizica(id, nume, adresa, nrParticipari, nrLicitatiiCastigate, dataNastere);
    }


}
