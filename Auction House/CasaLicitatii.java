import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class CasaLicitatii {
    String nume;
    List<Produs> produseActuale = Collections.synchronizedList(new CopyOnWriteArrayList<>()); //produse de vanzare
    List<Produs> produse = new ArrayList<>(); //toate produsele care au fost vreodata in sistem
    ArrayList<Client> clienti = new ArrayList<>();
    ArrayList<Licitatie> licitatiiActive = new ArrayList<>();

    void printProduse() {
        for (Produs p : this.produseActuale)
            p.printDetalii();
    }

    Produs parseazaProdus(String[] line_list, ProdusFactory fabrica) {
        //metoda in care parsam toate detaliile pentru produs de la input si ne folosim
        //de design pattern-ul factory pentru a crea produse
        ProdusFactory.TipProdus tip;
        if (line_list[1].startsWith("M"))
            tip = ProdusFactory.TipProdus.Mobila;
        else if(line_list[1].startsWith("T"))
            tip = ProdusFactory.TipProdus.Tablou;
        else tip = ProdusFactory.TipProdus.Bijuterie;
        return fabrica.creazaProdus(tip, CasaLicitatii.getInstance().produse.size() + 1,line_list[2],
                Double.parseDouble(line_list[3]), Double.parseDouble(line_list[4]),
                Integer.parseInt(line_list[5]), line_list[6], line_list[7]);
    }

    void solicitaAccesLicitatie(String[] line_list) {
        int idClient = Integer.parseInt(line_list[1]);
        int idProdus = Integer.parseInt(line_list[2]);
        Client client = this.clienti.get(idClient - 1);
        Licitatie licitatie = null;
        for (Licitatie l : this.licitatiiActive)
            if (l.idProdus == idProdus) {
                licitatie = l;
                break;
            }
        //vom verifica daca clientul care a solicitat licitatia participa deja la aceasta
        int participa = 0;
        for (Map.Entry<Client, Double> mapElement : licitatie.clientMap.entrySet()) {
            Client client1 = mapElement.getKey();
            if (client1.id == client.id) {
                participa = 1;
                break;
            }
        }//daca semaforul s-a actualizat vom afisa un mesaj corespunzator
        if (participa == 1) {
            System.out.println("Clientul cu ID-ul " + idClient + " participa deja la licitatia cu ID-ul " +
                    licitatie.id + ".");
        } else {
            //vom verifica daca licitatia s-a terminat, deci clientul nu ar mai putea sa participe
            //in acest caz
            int exista = 0;
            for (Produs p : this.produse)
                if (p.id == idProdus) {
                    exista = 1;
                    break;
                }
            if (exista == 0) {
                System.out.println("Clientul cu ID-ul " + idClient + " nu se poate inscrie.");
            } else {
                //daca clientul este validat pentru inscrierea in licitatie
                //ii vom introduce entry-uri in toate map-urile din licitatie
                //si vom da start licitatiei in caz ca s-a atins numarul minim de participanti
                licitatie.clientMap.put(client, (double) 0);
                int nrBroker = (int) ((Math.random() * (licitatie.brokers.size())));
                licitatie.stareParticipare.put(client, true);
                Broker b = licitatie.brokers.get(nrBroker);

                for(Map.Entry<Integer, CopyOnWriteArrayList<Client>> mapElement:b.lista_Clienti.entrySet())
                    if(licitatie.id == mapElement.getKey())
                        if(!(mapElement.getValue()).contains(client))
                            (mapElement.getValue()).add(client);
                client.bugetLicitatii.put(idProdus, new AbstractMap.SimpleEntry<>
                        (b, Double.parseDouble(line_list[3])));
                licitatie.cresteNrCurentParticipanti();
                client.increaseNrParticipari();
                if (licitatie.nrCurentParticipanti == licitatie.nrParticipanti) {
                    new Thread(licitatie).start();
                }
            }
        }
    }

    void addClient(String[] line_list) {
        //parsam toate detaliile date la input pentru client si vom folosi design pattern-ul
        //builder pentru a fi mai human-readable constructorul persoanei create
        if(line_list[1].equals("PF")) {
            PersoanaFizica pf = new PersoanaFizicaBuilder()
                    .setId(this.clienti.size() + 1)
                    .setNume(line_list[2])
                    .setDataNastere(line_list[4])
                    .setAdresa(line_list[3])
                    .build();
            this.clienti.add(pf);
        }
        else{
            PersoanaJuridica.Companie companie;
            if(line_list[4].equals("SRL"))
                companie = PersoanaJuridica.Companie.SRL;
            else
                companie = PersoanaJuridica.Companie.SA;

            PersoanaJuridica pj = new PersoanaJuridicaBuilder()
                    .setId(this.clienti.size() + 1)
                    .setNume(line_list[2])
                    .setCapitalSocial(Double.parseDouble(line_list[5]))
                    .setAdresa(line_list[3])
                    .setCompanie(companie)
                    .build();
            this.clienti.add(pj);
        }
    }

    private static final CasaLicitatii instance = new CasaLicitatii();

    private CasaLicitatii() {}

    public static CasaLicitatii getInstance() {
        return instance;
    }
}
